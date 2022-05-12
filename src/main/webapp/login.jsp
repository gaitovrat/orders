<%@ page import="vsb.gai0010.servlet.Attribute" %>
<%@ page import="vsb.gai0010.model.User" %>
<%
  User user = (User) session.getAttribute(Attribute.USER.toString());
  if (user != null) {
    response.sendRedirect(request.getContextPath() + "/");
  }

  Object error = session.getAttribute(Attribute.ERROR.toString());
%>
<html>
<head>
  <jsp:include page="/common/head.jsp">
    <jsp:param name="title" value="Login"/>
  </jsp:include>
</head>
<body>
<jsp:include page="/common/header.jsp" />
<div class="form-login block">
  <form method="post" action="${pageContext.request.contextPath}/login">
    <h1 class="h3 my-3 fw-normal" style="text-align: center;">Please sign in</h1>
    <div class="form-floating mb-1">
      <input type="text" class="form-control" id="floatingInput" placeholder="Login" name="login">
      <label for="floatingInput">Login</label>
    </div>
    <div class="form-floating mb-3">
      <input type="password" class="form-control" id="floatingPassword" placeholder="Password" name="password">
      <label for="floatingPassword">Password</label>
    </div>
    <div class="fs-6 text-center" style="color: red"><%= error == null ? "" : error %></div>
    <button class="w-100 btn btn-lg btn-primary" type="submit">Login</button>
  </form>
</div>
<jsp:include page="/common/body.jsp" />
</body>
</html>
