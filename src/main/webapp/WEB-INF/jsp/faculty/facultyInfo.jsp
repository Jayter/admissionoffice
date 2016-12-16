<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dateFunctions" uri="http://com.jayton.admissionoffice.functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="faculty.title" var="title"/>
<fmt:message bundle="${loc}" key="faculty.name" var="facultyName"/>
<fmt:message bundle="${loc}" key="faculty.phone" var="facultyPhone"/>
<fmt:message bundle="${loc}" key="faculty.email" var="facultyEmail"/>
<fmt:message bundle="${loc}" key="faculty.address" var="facultyAddress"/>
<fmt:message bundle="${loc}" key="faculty.directions" var="facultyDirections"/>
<fmt:message bundle="${loc}" key="faculty.empty_directions" var="facultyEmptyDirections"/>
<fmt:message bundle="${loc}" key="direction.name" var="directionName"/>
<fmt:message bundle="${loc}" key="direction.coefficient" var="directionCoefficient"/>
<fmt:message bundle="${loc}" key="direction.count" var="directionCount"/>
<fmt:message bundle="${loc}" key="button.next" var="next"/>
<fmt:message bundle="${loc}" key="button.previous" var="previous"/>
<fmt:message bundle="${loc}" key="button.add" var="add"/>
<fmt:message bundle="${loc}" key="button.apply" var="apply"/>
<fmt:message bundle="${loc}" key="button.edit" var="edit"/>

<html>
<head>
    <title>${title}</title>
</head>
<body>
    <jsp:include page="../fragments/header.jsp"/>
    <jsp:useBean id="faculty" type="com.jayton.admissionoffice.model.university.Faculty" scope="request"/>
    <table>
        <tr>
            <th>${facultyName}</th>
            <td>${faculty.name}</td>
        </tr>
        <tr>
            <th>${facultyPhone}</th>
            <td>${faculty.officePhone}</td>
        </tr>
        <tr>
            <th>${facultyEmail}</th>
            <td>${faculty.officeEmail}</td>
        </tr>
        <tr>
            <th>${facultyAddress}</th>
            <td>${faculty.officeAddress}</td>
        </tr>
    </table>
    <c:if test="${sessionScope.isAuthorizedAdmin}">
        <form method="post" action="Controller?command=edit-faculty">
            <input type="hidden" name="id" value="${faculty.id}"/>
            <input type="submit" value="${edit}"/>
        </form>
    </c:if>
    <p/>
    ${facultyDirections}:
    <c:choose>
        <c:when test="${not empty requestScope.directions}">
            <table>
                <tr>
                    <th>${directionName}</th>
                    <th>${directionCoefficient}</th>
                    <th>${directionCount}</th>
                    <th></th>
                </tr>
                <c:forEach items="${requestScope.directions}" var="direction">
                    <tr>
                        <td><a href="Controller?command=get-direction&id=${direction.id}">${direction.name}</a></td>
                        <td>${direction.averageCoefficient}</td>
                        <td>${direction.countOfStudents}</td>
                        <td>
                            <c:if test="${sessionScope.isAuthorizedUser and dateFunctions:isBetween(applicationScope.sessionTerms.sessionStart,
                             applicationScope.sessionTerms.sessionEnd)}">
                                <form method="post" action="Controller?command=user-apply">
                                    <input type="hidden" name="directionId" value="${direction.id}"/>
                                    <input type="submit" value="${apply}"/>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <c:if test="${requestScope.offset gt 0}">
                <a href="Controller?command=get-faculty&id=${faculty.id}&offset=${requestScope.offset
             - requestScope.count}&count=${requestScope.count}">
                    ${previous}
                </a>
            </c:if>
            <c:if test="${requestScope.offset + requestScope.count lt requestScope.totalCount}">
                <a href="Controller?command=get-faculty&id=${faculty.id}&offset=${requestScope.offset
             + requestScope.count}&count=${requestScope.count}">
                    ${next}
                </a>
            </c:if>
        </c:when>
        <c:otherwise>
            <p/>
            ${facultyEmptyDirections}
        </c:otherwise>
    </c:choose>
    <c:if test="${sessionScope.isAuthorizedAdmin}">
        <form method="post" action="Controller?command=edit-direction">
            <input type="hidden" name="facultyId" value="${faculty.id}"/>
            <input type="submit" value="${add}"/>
        </form>
    </c:if>
</body>
</html>