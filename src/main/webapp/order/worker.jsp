<%@ page import="vsb.gai0010.model.OrderStatus" %>
<%@ page import="vsb.gai0010.orm.OrderSQL" %>
<%@ page import="vsb.gai0010.model.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="vsb.gai0010.servlet.Attribute" %>
<%@ page import="vsb.gai0010.model.User" %>
<%@ page import="vsb.gai0010.model.Role" %>

<%
    User user = (User) session.getAttribute(Attribute.USER.toString());
    if (user == null || user.getRole() == Role.USER) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }

    OrderSQL orderSQL = new OrderSQL();
    List<Order> orders = orderSQL.select();

%>

<html>
<head>
    <jsp:include page="/common/head.jsp">
        <jsp:param name="title" value="Orders"/>
    </jsp:include>
</head>
<body>
<jsp:include page="/common/header.jsp" />
<table class="table">
    <thead>
    <tr>
        <th scope="col">#</th>
        <th scope="col">Name</th>
        <th scope="col">Address</th>
        <th scope="col"></th>
    </tr>
    </thead>
    <tbody>
    <%
        for (Order order : orders) {
    %>
        <tr>
            <th scope="row"><%=order.getId()%></th>
            <td><%=order.getCustomer().getFullName()%></td>
            <td><%=order.getCustomer().getAddress()%></td>
            <td>
                <form action="${pageContext.request.contextPath}/order" method="post">
                    <input type="hidden" name="orderId" value="<%=order.getId()%>">
                    <button type="submit" name="orderStatusId" value="<%=OrderStatus.APPROVED.getId()%>"
                            class="btn btn-primary">Choose</button>
                </form>
            </td>
        </tr>
    <%
        }
    %>
    </tbody>
</table>
<jsp:include page="/common/body.jsp" />
</body>
</html>
