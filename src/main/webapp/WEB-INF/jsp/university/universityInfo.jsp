<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="university.title" var="title"/>
<fmt:message bundle="${loc}" key="university.name" var="universityName"/>
<fmt:message bundle="${loc}" key="university.city" var="universityCity"/>
<fmt:message bundle="${loc}" key="university.address" var="universityAddress"/>
<fmt:message bundle="${loc}" key="university.faculties" var="faculties"/>
<fmt:message bundle="${loc}" key="university.empty_faculties" var="emptyFaculties"/>
<fmt:message bundle="${loc}" key="faculty.name" var="facultyName"/>
<fmt:message bundle="${loc}" key="faculty.phone" var="facultyPhone"/>
<fmt:message bundle="${loc}" key="faculty.email" var="facultyEmail"/>
<fmt:message bundle="${loc}" key="faculty.address" var="facultyAddress"/>
<fmt:message bundle="${loc}" key="button.next" var="next"/>
<fmt:message bundle="${loc}" key="button.previous" var="previous"/>
<fmt:message bundle="${loc}" key="button.add" var="add"/>
<fmt:message bundle="${loc}" key="button.edit" var="edit"/>

<html>
<head>
    <title>${title}</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>
<jsp:useBean id="university" type="com.jayton.admissionoffice.model.university.University" scope="request"/>
<table>
    <tr>
        <th>${universityName}</th>
        <td>${university.name}</td>
    </tr>
    <tr>
        <th>${universityCity}</th>
        <td>${university.city}</td>
    </tr>
    <tr>
        <th>${universityAddress}</th>
        <td>${university.address}</td>
    </tr>
</table>
<c:if test="${sessionScope.isAuthorizedAdmin}">
    <button onclick="location.href='Controller?command=edit-university&id=${university.id}'">${edit}</button>
</c:if>
<p/>
${faculties}:
<c:choose>
    <c:when test="${not empty requestScope.faculties}">
        <table>
            <tr>
                <th>${facultyName}</th>
                <th>${facultyPhone}</th>
                <th>${facultyEmail}</th>
                <th>${facultyAddress}</th>
            </tr>
            <c:forEach items="${requestScope.faculties}" var="faculty">
                <tr>
                    <td><a href="Controller?command=get-faculty&id=${faculty.id}">${faculty.name}</a></td>
                    <td>${faculty.officePhone}</td>
                    <td>${faculty.officeEmail}</td>
                    <td>${faculty.officeAddress}</td>
                </tr>
            </c:forEach>
        </table>
        <c:if test="${requestScope.offset gt 0}">
            <a href="Controller?command=get-university&id=${university.id}&offset=${requestScope.offset
             - requestScope.count}&count=${requestScope.count}">
                ${previous}
            </a>
        </c:if>
        <c:if test="${requestScope.offset + requestScope.count lt requestScope.totalCount}">
            <a href="Controller?command=get-university&id=${university.id}&offset=${requestScope.offset
             + requestScope.count}&count=${requestScope.count}">
                ${next}
            </a>
        </c:if>
    </c:when>
    <c:otherwise>
        <p/>
        ${emptyFaculties}
    </c:otherwise>
</c:choose>
<c:if test="${sessionScope.isAuthorizedAdmin}">
    <button onclick="location.href='Controller?command=edit-faculty&universityId=${university.id}'">${add}</button>
</c:if>
</body>
</html>