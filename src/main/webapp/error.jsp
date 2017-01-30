<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="WEB-INF/jsp/fragments/headTag.jsp"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="exception.title" var="title"/>
<fmt:message bundle="${loc}" key="exception.description" var="description"/>
<fmt:message bundle="${loc}" key="exception.cause" var="cause"/>
<fmt:message bundle="${loc}" key="exception.redirect" var="redirect"/>
<fmt:message bundle="${loc}" key="exception.seconds" var="seconds"/>

<body onload='startTimer()'>
    <jsp:include page="WEB-INF/jsp/fragments/header.jsp"/>
    <div class="outer">
        <h4>${description}</h4>
        <h3>${requestScope.errorMessage}</h3>
        <h3>${requestScope.exception.message}</h3>
        <c:if test="${not empty requestScope.exception.cause.message}">
            <h4>${cause} 1: ${requestScope.exception.cause.message}</h4>
        </c:if>
        <c:if test="${not empty requestScope.exception.cause.cause.message}">
            <h4>${cause} 2: ${requestScope.exception.cause.cause.message}</h4>
        </c:if>
        <c:if test="${not empty requestScope.exception.cause.cause.cause.message}">
            <h4>${cause} 3: ${requestScope.exception.cause.cause.cause.message}</h4>
        </c:if>
        <c:if test="${not empty requestScope.exception.cause.cause.cause.cause.message}">
            <h4>${cause} 4: ${requestScope.exception.cause.cause.cause.cause.message}</h4>
        </c:if>
        <h4>${redirect}
            <span id="my_timer">8</span>
            ${seconds}
        </h4>
    </div>
</body>