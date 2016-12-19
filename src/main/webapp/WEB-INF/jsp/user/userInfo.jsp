<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="functions" uri="http://com.jayton.admissionoffice.functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="dateFunctions" uri="http://com.jayton.admissionoffice.functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../fragments/headTag.jsp"/>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="user.title" var="title"/>
<fmt:message bundle="${loc}" key="user.name" var="userName"/>
<fmt:message bundle="${loc}" key="user.last_name" var="userlastName"/>
<fmt:message bundle="${loc}" key="user.email" var="email"/>
<fmt:message bundle="${loc}" key="user.address" var="address"/>
<fmt:message bundle="${loc}" key="user.phone" var="phone"/>
<fmt:message bundle="${loc}" key="user.birth" var="birthDate"/>
<fmt:message bundle="${loc}" key="user.average" var="average"/>
<fmt:message bundle="${loc}" key="user.results" var="results"/>
<fmt:message bundle="${loc}" key="user.applications" var="applications"/>
<fmt:message bundle="${loc}" key="subject.name" var="subjectName"/>
<fmt:message bundle="${loc}" key="subject.result" var="subjectResult"/>
<fmt:message bundle="${loc}" key="application.direction" var="applicationDirection"/>
<fmt:message bundle="${loc}" key="application.created" var="applicationCreated"/>
<fmt:message bundle="${loc}" key="application.status" var="applicationStatus"/>
<fmt:message bundle="${loc}" key="application.mark" var="applicationMark"/>
<fmt:message bundle="${loc}" key="button.edit" var="edit"/>
<fmt:message bundle="${loc}" key="button.cancel" var="cancel"/>
<fmt:message bundle="${loc}" key="button.delete" var="delete"/>
<fmt:message bundle="${loc}" key="button.add" var="add"/>

<body>
<jsp:include page="../fragments/header.jsp"/>
<div class="outer">
    <jsp:useBean id="user" type="com.jayton.admissionoffice.model.user.User" scope="request"/>
    <div class="inner_info">
        <table class="info">
            <caption>${title}</caption>
            <tr>
                <th>${userName}</th>
                <td>${user.name}</td>
            </tr>
            <tr>
                <th>${userlastName}</th>
                <td>${user.lastName}</td>
            </tr>
            <tr>
                <th>${address}</th>
                <td>${user.address}</td>
            </tr>
            <tr>
                <th>${email}</th>
                <td>${user.email}</td>
            </tr>
            <tr>
                <th>${phone}</th>
                <td>${user.phoneNumber}</td>
            </tr>
            <tr>
                <th>${birthDate}</th>
                <td>${functions:formatDate(user.birthDate)}</td>
            </tr>
            <tr>
                <th>${average}</th>
                <td>${user.averageMark}</td>
            </tr>
        </table>
        <c:if test="${sessionScope.isAuthorizedAdmin}">
            <button onclick="location.href='Controller?command=edit-user&id=${user.id}'" class="button">${edit}</button>
        </c:if>
    </div>
    <div class="inner_info">
        <table class="entries">
            <caption>${results}</caption>
            <tr>
                <th>${subjectName}</th>
                <th>${subjectResult}</th>
                <c:if test="${sessionScope.isAuthorizedAdmin}">
                    <th></th>
                </c:if>
            </tr>
            <c:forEach var="pair" items="${user.results}">
                <tr>
                    <td>${applicationScope.subjects[pair.key].name}</td>
                    <td>${pair.value}</td>
                    <c:if test="${sessionScope.isAuthorizedAdmin}">
                        <td>
                            <button onclick="location.href='Controller?command=delete-user-result&id=${user.id}&subjectId=${pair.key}'">
                                    ${delete}</button>
                        </td>
                    </c:if>
                </tr>
            </c:forEach>
        </table>
        <c:if test="${user.results.size() lt 4}">
            <form method="post" action="Controller?command=add-user-result">
                <input type="hidden" name="id" value="${user.id}"/>
                <select name="subjectId">
                    <option disabled>Choose a subject</option>
                    <c:forEach items="${applicationScope.subjects}" var="entry">
                        <c:if test="${not user.results.keySet().contains(entry.key)}">
                            <option value="${entry.value.id}">${entry.value.name}</option>
                        </c:if>
                    </c:forEach>
                </select>
                <input type="text" name="mark" minlength="3" maxlength="6">
                <input type="submit" value="${add}"/>
            </form>
        </c:if>
    </div>
    <table class="entries">
        <caption>${applications}</caption>
        <tr>
            <th>${applicationDirection}</th>
            <th>${applicationCreated}</th>
            <th>${applicationStatus}</th>
            <th>${applicationMark}</th>
            <th></th>
        </tr>
        <c:forEach var="application" items="${requestScope.applications}">
            <tr>
                <td><a href="Controller?command=get-direction&id=${application.directionId}">
                        ${requestScope.directionNames[application.directionId]}</a></td>
                <td>${functions:formatDateTime(application.creationTime)}</td>
                <td>${application.status}</td>
                <td>${application.mark}</td>
                <c:if test="${sessionScope.isAuthorizedUser and dateFunctions:isBetween(applicationScope.sessionTerms.sessionStart,
                             applicationScope.sessionTerms.sessionEnd)}">
                    <td>
                        <button onclick="location.href='Controller?command=user-cancel-application&id=${application.id}'">
                                ${cancel}</button>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
    </table>
</div>
</body>