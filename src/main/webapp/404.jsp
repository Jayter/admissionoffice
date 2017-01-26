<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="WEB-INF/jsp/fragments/headTag.jsp"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="404.page_not_found" var="not_found"/>
<fmt:message bundle="${loc}" key="exception.redirect" var="redirect"/>
<fmt:message bundle="${loc}" key="exception.seconds" var="seconds"/>

<body onload='startTimer()'>
    <jsp:include page="WEB-INF/jsp/fragments/header.jsp"/>
    <div class="outer">
        <img class="centred" src="resources/img/404.png"/>
        <h2>${not_found}</h2>
        <h2>${redirect}
            <span id="my_timer">8</span>
            ${seconds}
        </h2>
    </div>
</body>