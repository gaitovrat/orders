package vsb.gai0010.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import vsb.gai0010.system.Error;
import vsb.gai0010.util.ThrowingSupplier;
import vsb.gai0010.util.XML;

@Log4j2
@AllArgsConstructor
public class Database implements AutoCloseable {
    private static Database instance;
    private Connection connection;
    private String url;
    private String username;
    private String password;

    private Database() {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("configuration.xml")) {
            Map<String, String> properties = XML.toMap(inputStream);

            this.url = "jdbc:oracle:thin:@" +
                    properties.get("serveraddress") + ":" +
                    properties.get("port") + ":" +
                    properties.get("sid");
            this.username = properties.get("username");
            this.password = properties.get("password");

            log.info("Database url: {}", this.url);
            log.info("Username: {}", this.username);
            log.info("Password: {}", this.password);
        } catch (IOException e) {
            log.error(e.getMessage());
            System.exit(Error.IO.getNumber());
        } catch (ParserConfigurationException | SAXException e) {
            log.error(e.getMessage());
            System.exit(Error.PARSER.getNumber());
        }
    }

    private Database(String url, String username, String password) {
        this(null, url, username, password);
    }

    public static Database getConnection() throws SQLException {
        if (instance == null) {
            instance = new Database();
        } else {
            instance = new Database(instance.url, instance.username, instance.password);
        }

        instance.open();

        return instance;
    }

    public void open() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
            log.debug("Opened");
        }
    }

    @Override
    public void close() throws SQLException {
        if (!this.connection.isClosed()) {
            this.connection.close();
            log.debug("Closed");
        }
    }

    public ResultSet execute(String sql) throws SQLException {
    	Statement statement = this.connection.createStatement();
    	return statement.executeQuery(sql);
    }

    public PreparedStatement create(String sql) throws SQLException {
        return this.connection.prepareStatement(sql);
    }

    public PreparedStatement create(String sql, String[] columnNames) throws SQLException {
        return this.connection.prepareStatement(sql, columnNames);
    }

    public void rollback() throws SQLException {
        this.connection.rollback();
    }

    public void commit() throws SQLException {
        this.connection.commit();
    }

    public void startTransaction() throws SQLException {
        this.connection.setAutoCommit(false);
    }

    public void endTransaction() throws SQLException {
        this.commit();
    }

    public CallableStatement createCall(String sql) throws SQLException {
        return this.connection.prepareCall(sql);
    }
    
    public int executeTransaction(ThrowingSupplier<Integer> transaction) throws SQLException {
    	int out = -1;
    	
    	try {
    		this.startTransaction();
    		
    		out = transaction.get();
    		
    		this.endTransaction();
    	} catch (SQLException e) {
    		log.error(e.getMessage());
    		this.rollback();
    	}
    	
    	return out;
    }
}
