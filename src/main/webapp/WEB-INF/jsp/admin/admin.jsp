<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="admin.title" var="title"/>
<fmt:message bundle="${loc}" key="admin.handle_applications" var="handleApps"/>
<fmt:message bundle="${loc}" key="admin.create_university" var="createUniv"/>
<fmt:message bundle="${loc}" key="admin.create_user" var="createUser"/>

<html>
<head>
    <title>${title}</title>
</head>
<body>
    <jsp:include page="../fragments/header.jsp"/>
    <p/>
    <button onclick="location.href='Controller?command=handle-applications'">${handleApps}</button>
    <p/>
    <button onclick="location.href='Controller?command=edit-user'">${createUser}</button>
    <p/>
    <button onclick="location.href='Controller?command=edit-university'">${createUniv}</button>
</body>
</html>