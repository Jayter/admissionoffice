<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="../fragments/headTag.jsp"/>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="admin.title" var="title"/>
<fmt:message bundle="${loc}" key="admin.handle_applications" var="handleApps"/>
<fmt:message bundle="${loc}" key="admin.create_university" var="createUniv"/>
<fmt:message bundle="${loc}" key="admin.create_user" var="createUser"/>

<body>
    <jsp:include page="../fragments/header.jsp"/>
    <div class="edit_outer">
        <button onclick="location.href='Controller?command=handle-applications'">${handleApps}</button>
        <button onclick="location.href='Controller?command=edit-user'">${createUser}</button>
        <button onclick="location.href='Controller?command=edit-university'">${createUniv}</button>
    </div>
</body>