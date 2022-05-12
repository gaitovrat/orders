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

public class LoginServlet extends HttpServlet {
    private UserSQL userSQL;

    @Override
    public void init() throws ServletException {
        super.init();

        this.userSQL = new UserSQL();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String login = req.getParameter(Attribute.LOGIN.toString());
        String password = req.getParameter(Attribute.PASSWORD.toString());

        User user = this.userSQL.select(login, password);

        if (user == null) {
            session.setAttribute(Attribute.ERROR.toString(), Error.USER_NOT_FOUND);
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        } else {
            session.setAttribute(Attribute.USER.toString(), user);
            session.setAttribute(Attribute.ERROR.toString(), null);
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
