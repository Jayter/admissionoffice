<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dateFunctions" uri="http://com.jayton.admissionoffice.functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="paginator" uri="http://com.jayton.admissionoffice.paginator" %>
<%@ taglib prefix="functions" uri="http://com.jayton.admissionoffice.functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../fragments/headTag.jsp"/>

<fmt:setLocale value="${sessionScope.locale}"/>
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

<body>
    <jsp:include page="../fragments/header.jsp"/>
    <c:set scope="request" var="isWithinSessionTerms" value="${dateFunctions:isWithinSessionTerms(sessionScope.sessionTerms)}"/>
    <c:set scope="request" var="isBeyondSessionTerms" value="${dateFunctions:isBeyondSessionTerms(sessionScope.sessionTerms)}"/>
    <div class="outer">
        <jsp:useBean id="faculty" type="com.jayton.admissionoffice.model.university.Faculty" scope="request"/>
        <div class="inner_info">
            <table class="info">
                <caption>${title}</caption>
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
            <c:if test="${sessionScope.isAuthorizedAdmin and isBeyondSessionTerms}">
                <button onclick="location.href='Controller?command=edit-faculty&id=${faculty.id}'" class="button">
                    ${edit}</button>
            </c:if>
        </div>
        <table class="entries">
            <caption>${facultyDirections}</caption>
            <tr>
                <th>${directionName}</th>
                <th>${directionCoefficient}</th>
                <th>${directionCount}</th>
                <th></th>
            </tr>
            <c:forEach items="${requestScope.directions}" var="direction">
                <tr>
                    <td><a href="Controller?command=get-direction&id=${direction.id}">${direction.name}</a></td>
                    <td>${functions:formatDecimal(direction.averageCoefficient)}</td>
                    <td>${direction.countOfStudents}</td>
                    <td>
                        <c:if test="${sessionScope.isAuthorizedUser and isWithinSessionTerms and
                         dateFunctions:containsAll(direction, sessionScope.user)}">
                            <form method="post" action="Controller?command=user-apply">
                                <input type="hidden" name="directionId" value="${direction.id}"/>
                                <input type="submit" value="${apply}"/>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <paginator:display url="Controller?command=get-faculty&id=${faculty.id}" currentPage="${requestScope.page}"
                           totalPagesCount="${requestScope.pagesCount}" linksCount="${requestScope.count}"/>
        <c:if test="${sessionScope.isAuthorizedAdmin and isBeyondSessionTerms}">
            <button onclick="location.href='Controller?command=edit-direction&facultyId=${faculty.id}'" class="button">
                ${add}</button>
        </c:if>
    </div>
</body>