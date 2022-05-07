package vsb.gai0010.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vsb.gai0010.orm.UserSQL;
import vsb.gai0010.model.User;
import vsb.gai0010.system.Error;

import java.io.IOException;
import java.io.PrintWriter;

public class LoginServlet extends HttpServlet {
    private UserSQL userSQL;

    @Override
    public void init() throws ServletException {
        super.init();

        this.userSQL = new UserSQL();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("Hello world");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String login = req.getParameter(LoginAttribute.LOGIN.toString());
        String password = req.getParameter(LoginAttribute.PASSWORD.toString());

        User user = this.userSQL.select(login, password);
        if (user == null) {
            session.setAttribute(LoginAttribute.ERROR.toString(), Error.USER_NOT_FOUND);
            resp.sendRedirect(req.getContextPath() + "/signin.jsp");
        } else {
            session.setAttribute(LoginAttribute.LOGIN.toString(), login);
            session.setAttribute(LoginAttribute.ERROR.toString(), null);
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
