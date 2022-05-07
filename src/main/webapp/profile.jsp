<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="base/head.jsp"/>
    <title>Profile - ${sessionScope.get("user").login}</title>
</head>
<body>
<jsp:include page="base/header.jsp"/>
${sessionScope.get("user").firstName} - ${sessionScope.get("user").secondName}
</body>
</html>
