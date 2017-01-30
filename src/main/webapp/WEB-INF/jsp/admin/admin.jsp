<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="functions" uri="http://com.jayton.admissionoffice.functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dateFunctions" uri="http://com.jayton.admissionoffice.functions" %>

<jsp:include page="../fragments/headTag.jsp"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="admin.title" var="title"/>
<fmt:message bundle="${loc}" key="admin.handle_applications" var="handleApps"/>
<fmt:message bundle="${loc}" key="admin.create_university" var="createUniv"/>
<fmt:message bundle="${loc}" key="admin.create_user" var="createUser"/>
<fmt:message bundle="${loc}" key="admin.session_start" var="sessionStart"/>
<fmt:message bundle="${loc}" key="admin.session_end" var="sessionEnd"/>
<fmt:message bundle="${loc}" key="button.admin.edit" var="edit"/>
<fmt:message bundle="${loc}" key="button.admin.create" var="create"/>

<body>
    <jsp:include page="../fragments/header.jsp"/>
    <c:set scope="request" var="isBeforeSessionStart" value="${dateFunctions:isBeforeSessionStart(sessionScope.sessionTerms)}"/>
    <c:set scope="request" var="isAfterSessionEnd" value="${dateFunctions:isBeforeSessionStart(sessionScope.sessionTerms)}"/>
    <c:set scope="request" var="isBeyondSessionTerms" value="${dateFunctions:isBeyondSessionTerms(sessionScope.sessionTerms)}"/>
    <div class="edit_outer">
        <c:if test="${not empty sessionScope.sessionTerms}">
            <table class="info">
                <tr>
                    <th>${sessionStart}</th>
                    <th>${sessionEnd}</th>
                </tr>
                <tr>
                    <td>${functions:formatDateTime(sessionScope.sessionTerms.sessionStart)}</td>
                    <td>${functions:formatDateTime(sessionScope.sessionTerms.sessionEnd)}</td>
                </tr>
            </table>
        </c:if>
        <c:if test="${isBeforeSessionStart}">
            <button onclick="location.href='Controller?command=edit-session-terms'">
                    ${not empty sessionScope.sessionTerms ? edit: create}</button>
        </c:if>
        <c:if test="${isAfterSessionEnd}">
            <button onclick="location.href='Controller?command=handle-applications'">${handleApps}</button>
        </c:if>
        <c:if test="${isBeyondSessionTerms}">
            <button onclick="location.href='Controller?command=edit-user'">${createUser}</button>
            <button onclick="location.href='Controller?command=edit-university'">${createUniv}</button>
        </c:if>
    </div>
</body>