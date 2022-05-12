<%@ page import="vsb.gai0010.servlet.Attribute" %>
<%@ page import="vsb.gai0010.orm.OrderSQL" %>
<%@ page import="vsb.gai0010.util.Pair" %>
<%@ page import="vsb.gai0010.model.*" %>

<%
    User user = (User) session.getAttribute(Attribute.USER.toString());
    if (user == null || user.getRole() == Role.USER) {
        response.sendRedirect(request.getContextPath() + "/");
        return;
    } else if (user.getStatus() != Status.BUSY) {
        response.sendRedirect(request.getContextPath() + "/order");
        return;
    }

    OrderSQL orderSQL = new OrderSQL();

    Order order = orderSQL.selectFor(user);
    User customer = order.getCustomer();

%>

<html>
<head>
    <jsp:include page="/common/head.jsp">
        <jsp:param name="title" value="Order #1"/>
    </jsp:include>
</head>
<body>
<jsp:include page="/common/header.jsp"/>
<div class="container">
    <div class="card w-100 my-2">
        <div class="card-header text-white bg-primary">Customer</div>
        <div class="card-body">
            <div class="row">
                <div class="col-3">First name:</div>
                <div class="col-9"><%=customer.getFirstName()%></div>
            </div>
            <div class="row">
                <div class="col-3">Second name:</div>
                <div class="col-9"><%=customer.getSecondName()%></div>
            </div>
            <div class="row">
                <div class="col-3">Email:</div>
                <div class="col-9"><%=customer.getEmail()%></div>
            </div>
            <div class="row">
                <div class="col-3">Phone number:</div>
                <div class="col-9"><%=customer.getPhoneNumber()%></div>
            </div>
            <div class="row">
                <div class="col-3">Country:</div>
                <div class="col-9"><%=customer.getCountry()%></div>
            </div>
            <div class="row">
                <div class="col-3">City:</div>
                <div class="col-9"><%=customer.getCity()%></div>
            </div>
            <div class="row">
                <div class="col-3">Home address:</div>
                <div class="col-9"><%=customer.getStreet()%>, <%=customer.getHouseNumber()%></div>
            </div>
            <div class="row">
                <div class="col-3">Zip:</div>
                <div class="col-9"><%=customer.getZip()%></div>
            </div>
        </div>
    </div>
    <div class="card w-100 my-2">
        <div class="card-header text-white bg-primary">Contents</div>
        <div class="card-body">
            <ul class="list-group">
                <%
                    for (Pair<Cloth, Integer> pair : order.getClothes()) {
                %>
                    <li class="list-group-item">
                        <h5 class="mb-1"><%=pair.getFirst().getName()%></h5>
                        <div class="d-flex w-100 justify-content-between">
                            <p>Quantity</p>
                            <small><%=pair.getSecond()%></small>
                        </div>
                    </li>
                <%
                    }
                %>
                <li class="list-group-item">
                    <div class="d-flex w-100 justify-content-between">
                        <p>Total</p>
                        <small>
                            <%
                                int sum = order.getClothes().stream().map(Pair::getSecond).reduce(0, Integer::sum);
                            %>
                            <%=sum%>
                        </small>
                    </div>
                </li>
            </ul>
            <form class="d-flex flex-row-reverse my-2" method="post" action="${pageContext.request.contextPath}/order">
                <input type="hidden" name="orderId" value="<%=order.getId()%>">
                <button type="submit" name="orderStatusId" value="<%=OrderStatus.SENT.getId()%>" class="btn btn-primary">Send</button>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/common/body.jsp"/>
</body>
</html>
