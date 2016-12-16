<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="faculty.edit" var="editFaculty"/>
<fmt:message bundle="${loc}" key="faculty.create" var="createFaculty"/>
<fmt:message bundle="${loc}" key="faculty.name" var="facultyName"/>
<fmt:message bundle="${loc}" key="faculty.phone" var="facultyPhone"/>
<fmt:message bundle="${loc}" key="faculty.email" var="facultyEmail"/>
<fmt:message bundle="${loc}" key="faculty.address" var="facultyAddress"/>
<fmt:message bundle="${loc}" key="button.edit" var="edit"/>
<fmt:message bundle="${loc}" key="button.delete" var="delete"/>

<html>
<head>
    <title>${editFaculty}</title>
</head>
<body>
    <jsp:include page="../fragments/header.jsp"/>
    <c:choose>
        <c:when test="${not empty requestScope.id}">
            <h2>${editFaculty}</h2>
        </c:when>
        <c:otherwise>
            <h2>${createFaculty}</h2>
        </c:otherwise>
    </c:choose>
    <form method="post" action=${empty requestScope.id ? "Controller?command=create-faculty" :
            "Controller?command=update-faculty"}>
        <input type="hidden" name="id" value="${requestScope.id}">
        ${requestScope.id}
        <input type="hidden" name="universityId" value="${requestScope.universityId}">
        <dl>
            <dt>${facultyName}</dt>
            <dd><input type="text" name="name" size="60" value="${requestScope.name}"></dd>
        </dl>
        <dl>
            <dt>${facultyPhone}</dt>
            <dd><input type="text" name="phone" value="${requestScope.phone}"></dd>
        </dl>
        <dl>
            <dt>${facultyEmail}</dt>
            <dd><input type="text" name="email" value="${requestScope.email}"></dd>
        </dl>
        <dl>
            <dt>${facultyAddress}</dt>
            <dd><input type="text" name="address" value="${requestScope.address}"></dd>
        </dl>
        <button type="submit">${edit}</button>
    </form>
    <c:if test="${not empty requestScope.id}">
        <form method="post" action="Controller?command=delete-faculty">
            <input type="hidden" name="id" value="${requestScope.id}"/>
            <input type="submit" value="${delete}"/>
        </form>
    </c:if>
</body>
</html>