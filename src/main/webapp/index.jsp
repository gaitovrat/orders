<%@ page import="vsb.gai0010.orm.ClothSQL" %>
<%@ page import="vsb.gai0010.model.Cloth" %>
<%@ page import="vsb.gai0010.servlet.Attribute" %>
<%@ page import="vsb.gai0010.model.User" %>
<%@ page import="vsb.gai0010.model.OrderStatus" %>
<%@ page import="vsb.gai0010.model.Role" %>

<%
    ClothSQL clothSQL = new ClothSQL();
    User user = (User) session.getAttribute(Attribute.USER.toString());
%>

<html>
<head>
    <jsp:include page="/common/head.jsp">
        <jsp:param name="title" value="Home"/>
    </jsp:include>
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
        <%
            for (Cloth cloth : clothSQL.select()) {
        %>
            <div class="col">
                <div class="card shadow-sm">
                    <img class="bd-placeholder-img card-img-top" width="100%" height="225" src="${pageContext.request.contextPath}/img/base.jpeg"  alt="Item"/>
                    <div class="card-body">
                        <h5 class="card-title" id="cloth-name"><%=cloth.getName()%></h5>
                        <p class="card-text"><%=cloth.getDescription()%></p>
                        <p class="card-text pt-1"><%=cloth.getPrice()%></p>
                        <div class="d-flex justify-content-between align-items-center">
                            <%
                                if (user != null && user.getRole() == Role.USER) {
                            %>
                                <form class="btn-group" action="${pageContext.request.contextPath}/order" method="post">
                                    <input type="hidden" name="clothId" value="<%=cloth.getId()%>">
                                    <input type="hidden" name="orderStatusId" value="<%=OrderStatus.WAITING_FOR_PAYMENT.getId()%>">

                                    <label>
                                        <input style="width: 3rem" class="me-1" type="number" name="count" value="1" min="1">
                                    </label>
                                    <button type="submit" class="btn btn-sm btn-outline-secondary">Buy</button>
                                </form>
                            <%
                                } else {
                            %>
                                <div class="btn-group">
                                    <label>
                                        <input style="width: 3rem" class="me-1" type="number" name="count" value="1" min="1">
                                    </label>
                                    <button onclick="alert('Please, login')" type="button" class="btn btn-sm btn-outline-secondary toastTrigger">Buy</button>
                                </div>
                            <%
                                }
                            %>
                        </div>
                    </div>
                </div>
            </div>
        <%
            }
        %>
    </div>
    <jsp:include page="/common/body.jsp" />
</body>
</html>
