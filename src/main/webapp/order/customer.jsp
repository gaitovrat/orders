<%@ page import="vsb.gai0010.orm.OrderSQL" %>
<%@ page import="vsb.gai0010.servlet.Attribute" %>
<%@ page import="java.util.List" %>
<%@ page import="vsb.gai0010.util.Pair" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="vsb.gai0010.util.SortByOrderStatus" %>
<%@ page import="vsb.gai0010.model.*" %>

<%
    User user = (User) session.getAttribute(Attribute.USER.toString());
    if (user == null || user.getRole() != Role.USER) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    }

    OrderSQL orderSQL = new OrderSQL();
    List<Order> orders = orderSQL.select(user);
    orders.sort(new SortByOrderStatus());
    Order basketOrder = null;
    List<Pair<Cloth, Integer>> basket = null;

    try {
        basketOrder = orders.stream()
                .filter(el -> el.getOrderStatus() == OrderStatus.WAITING_FOR_PAYMENT)
                .toList().get(0);
        request.setAttribute("order", basketOrder);

        basket = basketOrder.getClothes();
    } catch (IndexOutOfBoundsException e) {
        basket = new ArrayList<>();
    }

    orders = orders.stream().filter(el -> el.getOrderStatus() != OrderStatus.WAITING_FOR_PAYMENT).toList();
%>

<html>
<head>
    <jsp:include page="/common/head.jsp">
        <jsp:param name="title" value="Orders"/>
    </jsp:include>
</head>
<body>
<jsp:include page="/common/header.jsp"/>
<div class="container">
    <%--        Basket--%>
    <div class="card text-dark bg-light mb-3 w-100">
        <div class="card-header text-white bg-primary">Basket</div>
        <div class="card-body">
            <ul class="list-group">
                <%
                    for (Pair<Cloth, Integer> pair : basket) {
                %>
                <li class="list-group-item">
                    <div class="mb-1 d-flex justify-content-between">
                        <h5><%=pair.getFirst().getName()%></h5>
                        <form action="${pageContext.request.contextPath}/orderItem" method="post">
                            <input type="hidden" name="orderId" value="<%=basketOrder.getId()%>">
                            <input type="hidden" name="clothId" value="<%=pair.getFirst().getId()%>">
                            <button class="btn btn-danger" type="submit">x</button>
                        </form>
                    </div>
                    <small><%=pair.getFirst().getPrice()%></small>
                    <p><%=pair.getFirst().getDescription()%></p>
                    <div class="d-flex w-100 justify-content-between">
                        <p>Quantity</p>
                        <small><%=pair.getSecond()%> x <%=pair.getFirst().getPrice()%>$:
                            <%=pair.getSecond() * pair.getFirst().getPrice()%>$</small>
                    </div>
                </li>
                <%
                    }
                %>
                <li class="list-group-item">
                    <div class="d-flex justify-content-between w-100">
                        <p>Total</p>
                        <small>
                            <%
                                Float price = basket.stream()
                                        .map(el -> el.getFirst().getPrice() * el.getSecond())
                                        .reduce(0.0f, Float::sum);
                            %>
                            $<%=price%>
                        </small>
                    </div>
                </li>
            </ul>
            <%
                if (basket.size() > 0) {
            %>
                <form class="d-flex flex-row-reverse mt-2" method="post" action="${pageContext.request.contextPath}/order">
                    <input type="hidden" name="orderId" value="${order.id}">
                    <button type="submit" name="orderStatusId" value="<%=OrderStatus.WAITING_FOR_ORDER_APPROVAL.getId()%>" class="btn-primary btn mx-1">Buy</button>
                    <button type="submit" name="orderStatusId" value="<%=OrderStatus.CANCELED.getId()%>" class="btn-danger btn mx-1">Cancel</button>
                </form>
            <%
                }
            %>
        </div>
    </div>

    <%--        Orders--%>
    <div class="card text-dark bg-light mb-3 w-100">
        <div class="card-header text-white bg-primary">Orders</div>
        <div class="card-body">
            <ul class="list-group">
                <%
                    for (Order order : orders) {
                %>
                    <li class="list-group-item">
                        <div class="card w-100">
                            <div class="card-header">Order <%=order.getId()%></div>
                            <div class="card-body">
                                <ul class="list-group">
                                    <%
                                        for (Pair<Cloth, Integer> pair : order.getClothes()) {
                                    %>
                                        <li class="list-group-item">
                                            <h5 class="mb-1"><%=pair.getFirst().getName()%></h5>
                                            <small><%=pair.getFirst().getPrice()%>$</small>
                                            <p><%=pair.getFirst().getDescription()%></p>
                                            <div class="d-flex w-100 justify-content-between">
                                                <p>Quantity</p>
                                                <small><%=pair.getSecond()%> x <%=pair.getFirst().getPrice()%>$:
                                                    <%=pair.getSecond() * pair.getFirst().getPrice()%>$</small>
                                            </div>
                                        </li>
                                    <%
                                        }
                                    %>
                                    <li class="list-group-item">
                                        <div class="d-flex justify-content-between w-100">
                                            <p>Total</p>
                                            <small>
                                                <%
                                                    Float price2 = order.getClothes().stream()
                                                        .map(el -> el.getFirst().getPrice() * el.getSecond())
                                                        .reduce(0.0f, Float::sum);
                                                %>
                                                <%=price2%>
                                            </small>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                            <div class="px-3">
                                <div class="progress my-2">
                                    <div class="progress-bar progress-bar-striped" role="progressbar"
                                         style="width: <%=order.getOrderStatus().getId() * 25%>%"
                                         aria-valuenow="<%=order.getOrderStatus().getId() * 25%>"
                                         aria-valuemin="0" aria-valuemax="100"></div>
                                </div>
                                <div class="text-muted fs-6">Status: <%=order.getOrderStatus()%></div>
                            </div>
                        </div>
                    </li>
                <%
                    }
                %>
            </ul>
        </div>
    </div>
</div>
<jsp:include page="/common/body.jsp"/>
</body>
</html>
