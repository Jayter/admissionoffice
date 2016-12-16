<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="functions" uri="http://com.jayton.admissionoffice.functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="university.edit" var="editUniversity"/>
<fmt:message bundle="${loc}" key="university.create" var="createUniversity"/>
<fmt:message bundle="${loc}" key="university.name" var="universityName"/>
<fmt:message bundle="${loc}" key="university.city" var="universityCity"/>
<fmt:message bundle="${loc}" key="university.address" var="universityAddress"/>
<fmt:message bundle="${loc}" key="button.edit" var="edit"/>
<fmt:message bundle="${loc}" key="button.delete" var="delete"/>

<html>
<head>
    <title>${editUniversity}</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>
    <c:choose>
        <c:when test="${not empty requestScope.id}">
            <h2>${editUniversity}</h2>
        </c:when>
        <c:otherwise>
            <h2>${createUniversity}</h2>
        </c:otherwise>
    </c:choose>
    <form method="post" action=${empty requestScope.id ? "Controller?command=create-university" :
                                        "Controller?command=update-university"}>
        <input type="hidden" name="id" value="${requestScope.id}">
        <dl>
            <dt>${universityName}</dt>
            <dd><input type="text" name="name" size="60" value="${requestScope.name}"></dd>
        </dl>
        <dl>
            <dt>${universityCity}</dt>
            <dd><input type="text" name="city" value="${requestScope.city}"></dd>
        </dl>
        <dl>
            <dt>${universityAddress}</dt>
            <dd><input type="text" name="address" value="${requestScope.address}"></dd>
        </dl>
        <button type="submit">${edit}</button>
    </form>
    <c:if test="${not empty requestScope.id}">
        <button onclick="location.href='Controller?command=delete-university&id=${requestScope.id}'">${delete}</button>
    </c:if>
</body>
</html>