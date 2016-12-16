<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="main.title" var="title"/>
<fmt:message bundle="${loc}" key="university.name" var="universityName"/>
<fmt:message bundle="${loc}" key="university.city" var="city"/>
<fmt:message bundle="${loc}" key="button.next" var="next"/>
<fmt:message bundle="${loc}" key="button.previous" var="previous"/>
<fmt:message bundle="${loc}" key="button.add" var="add"/>

<html>
<head>
    <title>${title}</title>
</head>
<body>
    <jsp:include page="fragments/header.jsp"/>
    <table>
        <tr>
            <td>${universityName}</td>
            <td>${city}</td>
        </tr>
        <c:forEach items="${requestScope.universities}" var="university">
                <tr>
                    <td><a href="Controller?command=get-university&id=${university.id}">${university.name}</a></td>
                    <td>${university.city}</td>
                </tr>
        </c:forEach>
    </table>
    <c:if test="${requestScope.offset gt 0}">
        <a href="Controller?command=load-main&offset=${requestScope.offset - requestScope.count}&count=${requestScope.count}">
            ${previous}
        </a>
    </c:if>
    <c:if test="${requestScope.offset + requestScope.count lt requestScope.totalCount}">
        <a href="Controller?command=load-main&offset=${requestScope.offset + requestScope.count}&count=${requestScope.count}">
            ${next}
        </a>
    </c:if>
    <c:if test="${sessionScope.isAuthorizedAdmin}">
        <button onclick="location.href='Controller?command=edit-university'">${add}</button>
    </c:if>
</body>
</html>