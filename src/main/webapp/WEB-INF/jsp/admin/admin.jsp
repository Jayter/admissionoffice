<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="functions" uri="http://com.jayton.admissionoffice.functions" %>

<jsp:include page="../fragments/headTag.jsp"/>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="admin.title" var="title"/>
<fmt:message bundle="${loc}" key="admin.handle_applications" var="handleApps"/>
<fmt:message bundle="${loc}" key="admin.create_university" var="createUniv"/>
<fmt:message bundle="${loc}" key="admin.create_user" var="createUser"/>
<fmt:message bundle="${loc}" key="admin.session_start" var="sessionStart"/>
<fmt:message bundle="${loc}" key="admin.session_end" var="sessionEnd"/>

<body>
    <jsp:include page="../fragments/header.jsp"/>
    <div class="edit_outer">
        <table class="info">
            <tr>
                <th>${sessionStart}</th>
                <th>${sessionEnd}</th>
            </tr>
            <tr>
                <td>${functions:formatDateTime(applicationScope.sessionTerms.sessionStart)}</td>
                <td>${functions:formatDateTime(applicationScope.sessionTerms.sessionEnd)}</td>
            </tr>
        </table>
        <button onclick="location.href='Controller?command=handle-applications'">${handleApps}</button>
        <button onclick="location.href='Controller?command=edit-user'">${createUser}</button>
        <button onclick="location.href='Controller?command=edit-university'">${createUniv}</button>
    </div>
</body>