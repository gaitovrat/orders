<%@ page import="vsb.gai0010.servlet.Attribute" %>
<%@ page import="vsb.gai0010.model.User" %>

<%
    User user = (User) session.getAttribute(Attribute.USER.toString());
%>

<header class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
    <a href="${pageContext.request.contextPath}/" class="d-flex align-items-center col-md-3 mb-2 mb-md-0 text-dark text-decoration-none">
        <span class="fs-4">DS2 project</span>
    </a>

    <ul class="nav col-12 col-md-auto mb-2 justify-content-center mb-md-0">
        <li><a href="${pageContext.request.contextPath}/" class="nav-link px-2 link-secondary">Home</a></li>
        <%
            if (user != null) {
        %>
            <li><a href="${pageContext.request.contextPath}/order" class="nav-link px-2 link-dark">Order</a></li>
        <%
            }
        %>
    </ul>

    <%
        if (user == null) {
    %>
        <div class="col-md-3 text-end">
            <button onclick="location.href = '${pageContext.request.contextPath}/login.jsp'" type="button" class="btn btn-outline-primary me-2">Login</button>
        </div>
    <%
        } else {
    %>
        <div class="col-md-3 text-end">
            <span>Hello, <%=user.getLogin()%></span>
            <button onclick="location.href = '${pageContext.request.contextPath}/logout'" class="btn btn-outline-primary ms-2">Log out</button>
        </div>
    <%
        }
    %>
</header>