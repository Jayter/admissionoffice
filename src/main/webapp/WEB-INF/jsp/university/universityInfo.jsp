<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="paginator" uri="http://com.jayton.admissionoffice.paginator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../fragments/headTag.jsp"/>

<fmt:setLocale value="${sessionScope.locale}"/>
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

<body>
<jsp:include page="../fragments/header.jsp"/>
<div class="outer">
    <jsp:useBean id="university" type="com.jayton.admissionoffice.model.university.University" scope="request"/>
    <div class="inner_info">
        <table class="info">
            <caption>${title}</caption>
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
            <button onclick="location.href='Controller?command=edit-university&id=${university.id}'" class="button">
                ${edit}</button>
        </c:if>
    </div>
    <table class="entries">
        <caption>${faculties}</caption>
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
    <paginator:display url="Controller?command=edit-faculty&universityId=${university.id}" currentPage="${requestScope.page}"
                       totalPagesCount="${requestScope.pagesCount}" linksCount="${requestScope.count}"/>
    <c:if test="${sessionScope.isAuthorizedAdmin}">
        <button onclick="location.href='Controller?command=edit-faculty&universityId=${university.id}'" class="button">
            ${add}</button>
    </c:if>
</div>
</body>