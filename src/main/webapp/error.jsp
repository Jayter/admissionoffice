<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Exception</title>
</head>
<body>
    <h2>Exception page</h2>

    <h3>${requestScope.errorMessage}</h3>
    <h3>${requestScope.exception.message}</h3>
    <c:if test="${not empty requestScope.exception.cause.message}">
        <h4>Cause1: ${requestScope.exception.cause.message}</h4>
    </c:if>
    <c:if test="${not empty requestScope.exception.cause.cause.message}">
        <h4>Cause2: ${requestScope.exception.cause.cause.message}</h4>
    </c:if>
    <c:if test="${not empty requestScope.exception.cause.cause.cause.message}">
        <h4>Cause3: ${requestScope.exception.cause.cause.cause.message}</h4>
    </c:if>
    <c:if test="${not empty requestScope.exception.cause.cause.cause.cause.message}">
        <h4>Cause4: ${requestScope.exception.cause.cause.cause.cause.message}</h4>
    </c:if>
</body>
</html>