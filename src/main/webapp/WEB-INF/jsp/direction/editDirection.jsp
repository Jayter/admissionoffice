<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="direction.edit" var="editDirection"/>
<fmt:message bundle="${loc}" key="direction.name" var="directionName"/>
<fmt:message bundle="${loc}" key="direction.coefficient" var="directionCoefficient"/>
<fmt:message bundle="${loc}" key="direction.count" var="directionCount"/>
<fmt:message bundle="${loc}" key="button.edit" var="edit"/>
<fmt:message bundle="${loc}" key="button.delete" var="delete"/>

<html>
<head>
    <title>${editDirection}</title>
</head>
<body>
    <jsp:include page="../fragments/header.jsp"/>
    <jsp:useBean id="direction" type="com.jayton.admissionoffice.model.university.Direction" scope="request"/>
    <h2>${editDirection}</h2>
    <form method="post" action="Controller?command=update-direction">
        <input type="hidden" name="id" value="${direction.id}">
        <input type="hidden" name="facultyId" value="${direction.facultyId}">
        <dl>
            <dt>${directionName}</dt>
            <dd><input type="text" name="name" size="60" value="${direction.name}"></dd>
        </dl>
        <dl>
            <dt>${directionCoefficient}</dt>
            <dd><input type="text" name="coefficient" value="${direction.averageCoefficient}"></dd>
        </dl>
        <dl>
            <dt>${directionCount}</dt>
            <dd><input type="text" name="countOfStudents" value="${direction.countOfStudents}"></dd>
        </dl>
        <button type="submit">${edit}</button>
    </form>
    <button onclick="location.href='Controller?command=delete-direction&id=${direction.id}'">${delete}</button>
</body>
</html>