<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="dateFunctions" uri="http://com.jayton.admissionoffice.functions" %>
<%@ taglib prefix="functions" uri="http://com.jayton.admissionoffice.functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="paginator" uri="http://com.jayton.admissionoffice.paginator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../fragments/headTag.jsp"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="direction.title" var="title"/>
<fmt:message bundle="${loc}" key="direction.name" var="directionName"/>
<fmt:message bundle="${loc}" key="direction.coefficient" var="directionCoefficient"/>
<fmt:message bundle="${loc}" key="direction.count" var="directionCount"/>
<fmt:message bundle="${loc}" key="direction.subjects" var="directionSubjects"/>
<fmt:message bundle="${loc}" key="direction.empty_subjects" var="directionEmptySubjects"/>
<fmt:message bundle="${loc}" key="direction.applications" var="directionApplications"/>
<fmt:message bundle="${loc}" key="direction.empty_applications" var="directionEmptyApplications"/>
<fmt:message bundle="${loc}" key="subject.name" var="subjectName"/>
<fmt:message bundle="${loc}" key="subject.coefficient" var="subjectCoefficient"/>
<fmt:message bundle="${loc}" key="application.user" var="applicationUser"/>
<fmt:message bundle="${loc}" key="application.created" var="applicationCreated"/>
<fmt:message bundle="${loc}" key="application.status" var="applicationStatus"/>
<fmt:message bundle="${loc}" key="application.mark" var="applicationMark"/>
<fmt:message bundle="${loc}" key="button.next" var="next"/>
<fmt:message bundle="${loc}" key="button.previous" var="previous"/>
<fmt:message bundle="${loc}" key="button.add" var="add"/>
<fmt:message bundle="${loc}" key="button.apply" var="apply"/>
<fmt:message bundle="${loc}" key="button.edit" var="edit"/>
<fmt:message bundle="${loc}" key="button.delete" var="delete"/>

<body>
<jsp:include page="../fragments/header.jsp"/>
<div class="outer">
    <jsp:useBean id="direction" type="com.jayton.admissionoffice.model.university.Direction" scope="request"/>
    <div class="inner_info">
        <table class="info">
            <caption>${title}</caption>
            <tr>
                <th>${directionName}</th>
                <td>${direction.name}</td>
            </tr>
            <tr>
                <th>${directionCount}</th>
                <td>${direction.countOfStudents}</td>
            </tr>
            <tr>
                <th>${directionCoefficient}</th>
                <td>${functions:formatDecimal(direction.averageCoefficient)}</td>
            </tr>
        </table>
        <c:if test="${sessionScope.isAuthorizedAdmin}">
            <button onclick="location.href='Controller?command=edit-direction&id=${direction.id}'" class="button">
                ${edit}</button>
        </c:if>
    </div>
    <div class="inner_info">
        <table class="entries">
            <caption>${directionSubjects}</caption>
            <tr>
                <th>${subjectName}</th>
                <th>${subjectCoefficient}</th>
                <th></th>
            </tr>
            <c:forEach items="${direction.entranceSubjects}" var="entry">
                <tr>
                    <td>${requestScope.subjects[entry.key].name}</td>
                    <td>${functions:formatDecimal(entry.value)}</td>
                    <td>
                        <c:if test="${sessionScope.isAuthorizedAdmin}">
                            <button onclick="location.href='Controller?command=delete-entrance-subject&directionId=${direction.id}&subjectId=${entry.key}'">
                                ${delete}</button>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </table>
        <c:if test="${sessionScope.isAuthorizedUser and dateFunctions:isBetween(sessionScope.sessionTerms.sessionStart,
            sessionScope.sessionTerms.sessionEnd)}">
            <form method="post" action="Controller?command=user-apply">
                <input type="hidden" name="directionId" value="${direction.id}"/>
                <input type="submit" value="${apply}"/>
            </form>
        </c:if>
        <c:if test="${sessionScope.isAuthorizedAdmin && direction.entranceSubjects.size() lt 3}">
            <form method="post" action="Controller?command=add-entrance-subject">
                <input type="hidden" name="directionId" value="${direction.id}"/>
                <select name="subjectId">
                    <option disabled>Choose a subject</option>
                    <c:forEach items="${requestScope.subjects}" var="entry">
                        <c:if test="${not direction.entranceSubjects.keySet().contains(entry.key)}">
                            <option value="${entry.value.id}">${entry.value.name}</option>
                        </c:if>
                    </c:forEach>
                </select>
                <input type="text" name="coefficient" minlength="3" maxlength="4">
                <input type="submit" value="${add}"/>
            </form>
        </c:if>
    </div>
    <table class="entries">
        <caption>${directionApplications}</caption>
        <tr>
            <th>${applicationUser}</th>
            <th>${applicationCreated}</th>
            <th>${applicationStatus}</th>
            <th>${applicationMark}</th>
        </tr>
        <c:forEach var="application" items="${requestScope.applications}">
            <tr>
                <td><a href="Controller?command=get-user&id=${application.userId}">
                    ${requestScope.userNames[application.userId]}</a></td>
                <td>${functions:formatDateTime(application.creationTime)}</td>
                <td>${application.status}</td>
                <td>${functions:formatDecimal(application.mark)}</td>
            </tr>
        </c:forEach>
    </table>
    <paginator:display url="Controller?command=get-direction&id=${direction.id}" currentPage="${requestScope.page}"
                       totalPagesCount="${requestScope.pagesCount}" linksCount="${requestScope.count}"/>
</div>
</body>