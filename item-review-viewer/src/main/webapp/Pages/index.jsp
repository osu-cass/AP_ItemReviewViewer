
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html
        PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">

<head class="site-header-font">
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>Item Bank Viewer - Smarter Balanced</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/vendor.css"></link>
</head>
<body>
    <div id="react-app" class="app-loading"></div>
    <script src="${pageContext.request.contextPath}/dist/vendor.js"></script>

    <c:if test="${DEV}">
        <script src="http://localhost:8787/dist/main.js"></script>
    </c:if>

    <c:if test="${!DEV}">
        <script src="/dist/main.js"></script>
    </c:if>

</body>

</html>