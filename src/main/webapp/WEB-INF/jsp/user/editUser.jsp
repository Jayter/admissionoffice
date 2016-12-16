<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="user.edit" var="editUser"/>
<fmt:message bundle="${loc}" key="user.create" var="createUser"/>
<fmt:message bundle="${loc}" key="user.name" var="userName"/>
<fmt:message bundle="${loc}" key="user.last_name" var="userlastName"/>
<fmt:message bundle="${loc}" key="user.email" var="email"/>
<fmt:message bundle="${loc}" key="user.password" var="password"/>
<fmt:message bundle="${loc}" key="user.address" var="address"/>
<fmt:message bundle="${loc}" key="user.phone" var="phone"/>
<fmt:message bundle="${loc}" key="user.birth" var="birthDate"/>
<fmt:message bundle="${loc}" key="user.average" var="average"/>
<fmt:message bundle="${loc}" key="button.edit" var="edit"/>
<fmt:message bundle="${loc}" key="button.create" var="create"/>
<fmt:message bundle="${loc}" key="button.delete" var="delete"/>

<html>
<head>
    <title>${editUser}</title>
</head>
<body>
<jsp:include page="../fragments/header.jsp"/>
    <c:choose>
        <c:when test="${not empty requestScope.id}">
            <h2>${editUser}</h2>
        </c:when>
        <c:otherwise>
            <h2>${createUser}</h2>
        </c:otherwise>
    </c:choose>
    <form method="post" action=${not empty requestScope.id ? "Controller?command=update-user" : "Controller?command=create-user"}>
        <input type="hidden" name="id" value="${requestScope.id}">
        <dl>
            <dt>${userName}</dt>
            <dd><input type="text" name="name" size="60" value="${requestScope.name}"></dd>
        </dl>
        <dl>
            <dt>${userlastName}</dt>
            <dd><input type="text" name="lastName" size="60" value="${requestScope.lastName}"></dd>
        </dl>
        <dl>
            <dt>${address}</dt>
            <dd><input type="text" name="address" value="${requestScope.address}"></dd>
        </dl>
        <dl>
            <dt>${email}</dt>
            <c:choose>
                <c:when test="${not empty requestScope.id}">
                    <dd><input type="text" value="${requestScope.email}" disabled></dd>
                </c:when>
                <c:otherwise>
                    <dd><input type="text" name="login"></dd>
                </c:otherwise>
            </c:choose>
        </dl>
        <c:if test="${empty requestScope.id}">
            <dl>
                <dt>${password}</dt>
                <dd><input type="text" name="password"></dd>
            </dl>
        </c:if>
        <dl>
            <dt>${phone}</dt>
            <dd><input type="text" name="phone" value="${requestScope.phone}"></dd>
        </dl>
        <dl>
            <dt>${birthDate}</dt>
            <dd><input type="date" name="birthDate" value="${requestScope.birthDate}"></dd>
        </dl>
        <dl>
            <dt>${average}</dt>
            <dd><input type="text" name="mark" value="${requestScope.mark}" minlength="1" maxlength="2"></dd>
        </dl>
        <button type="submit" value="">${edit}</button>
    </form>
    <c:if test="${not empty requestScope.id}">
        <button onclick="location.href='Controller?command=delete-user&id=${requestScope.id}'">${delete}</button>
    </c:if>
</body>
</html>