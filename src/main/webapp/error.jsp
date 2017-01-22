<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="exception.title" var="title"/>
<fmt:message bundle="${loc}" key="exception.description" var="description"/>
<fmt:message bundle="${loc}" key="exception.cause" var="cause"/>

<html>
<head>
    <title>${title}</title>
</head>
<body>
    <h2>${description}</h2>
    <h3>${requestScope.errorMessage}</h3>
    <h3>${requestScope.exception.message}</h3>
    <c:if test="${not empty requestScope.exception.cause.message}">
        <h4>${cause} 1: ${requestScope.exception.cause.message}</h4>
    </c:if>
    <c:if test="${not empty requestScope.exception.cause.cause.message}">
        <h4>${cause} 2: ${requestScope.exception.cause.cause.message}</h4>
    </c:if>
    <c:if test="${not empty requestScope.exception.cause.cause.cause.message}">
        <h4>${cause} : ${requestScope.exception.cause.cause.cause.message}</h4>
    </c:if>
    <c:if test="${not empty requestScope.exception.cause.cause.cause.cause.message}">
        <h4>${cause} : ${requestScope.exception.cause.cause.cause.cause.message}</h4>
    </c:if>
</body>
</html>