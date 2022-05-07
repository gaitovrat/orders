<%@ page import="vsb.gai0010.orm.OrderSQL" %>
<%@ page import="vsb.gai0010.servlet.Attribute" %>
<%@ page import="vsb.gai0010.model.User" %>
<%@ page import="vsb.gai0010.model.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="vsb.gai0010.model.OrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    OrderSQL orderSQL = new OrderSQL();
    List<Order> orders = orderSQL.select((User) session.getAttribute(Attribute.USER.toString()));
    request.setAttribute("status", OrderStatus.WAITING_FOR_ORDER_APPROVAL);
%>

<html>
<head>
    <jsp:include page="base/head.jsp"/>
    <title>My orders</title>
</head>
<body>
<jsp:include page="base/header.jsp"/>
<h1>My orders</h1>
<%
    for (Order order : orders.stream().filter(el -> el.getOrderStatus() == OrderStatus.WAITING_FOR_PAYMENT).toList()) {
%>
<div><%= order %></div>
<%
    }
%>
<form action="${pageContext.request.contextPath}/updateOrder?status=${status.id}" method="post">
    <button type="submit">Buy</button>
</form>
<form action=""></form>
<h1>Others</h1>
<%
    for (Order order : orders.stream().filter(el -> el.getOrderStatus() != OrderStatus.WAITING_FOR_PAYMENT).toList()) {
%>
    <div><%= order %></div>
<%
    }
%>
</body>
</html>
