<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="direction.create" var="createDirection"/>
<fmt:message bundle="${loc}" key="direction.name" var="directionName"/>
<fmt:message bundle="${loc}" key="direction.coefficient" var="directionCoefficient"/>
<fmt:message bundle="${loc}" key="direction.count" var="directionCount"/>
<fmt:message bundle="${loc}" key="direction.subjects" var="directionSubjects"/>
<fmt:message bundle="${loc}" key="button.create" var="create"/>
<fmt:message bundle="${loc}" key="button.delete" var="delete"/>

<html>
<head>
    <title>${createDirection}</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>
    <h2>${createDirection}</h2>
    <form method="post" action="Controller?command=create-direction">
        <input type="hidden" name="facultyId" value="${requestScope.facultyId}">
        <dl>
            <dt>${directionName}</dt>
            <dd><input type="text" name="name" size="60"></dd>
        </dl>
        <dl>
            <dt>${directionCoefficient}</dt>
            <dd><input type="text" name="coefficient" minlength="3" maxlength="4"></dd>
        </dl>
        <dl>
            <dt>${directionCount}</dt>
            <dd><input type="text" name="countOfStudents"></dd>
        </dl>
        ${directionSubjects}
        <dl>
            <select name="subject1Id" id="subject1Id">
                <c:forEach items="${applicationScope.subjects}" var="entry">
                    <option value="${entry.key}">${entry.value.name}</option>
                </c:forEach>
            </select>
            <input type="text" name="subject1Coefficient" maxlength="4" minlength="3">
        </dl>
        <dl>
            <select name="subject2Id" id="subject2Id">
                <c:forEach items="${applicationScope.subjects}" var="entry">
                    <option value="${entry.key}">${entry.value.name}</option>
                </c:forEach>
            </select>
            <input type="text" name="subject2Coefficient" maxlength="4" minlength="3">
        </dl>
        <dl>
            <select name="subject3Id" id="subject3Id">
                <c:forEach items="${applicationScope.subjects}" var="entry">
                    <option value="${entry.key}">${entry.value.name}</option>
                </c:forEach>
            </select>
            <input type="text" name="subject3Coefficient" maxlength="4" minlength="3">
        </dl>
        <button type="submit">${create}</button>
    </form>
</body>
</html>