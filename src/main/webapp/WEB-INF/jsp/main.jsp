<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="paginator" uri="http://com.jayton.admissionoffice.paginator" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="fragments/headTag.jsp"/>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="main.title" var="title"/>
<fmt:message bundle="${loc}" key="main.universities" var="universities"/>
<fmt:message bundle="${loc}" key="university.name" var="universityName"/>
<fmt:message bundle="${loc}" key="university.city" var="city"/>
<fmt:message bundle="${loc}" key="button.next" var="next"/>
<fmt:message bundle="${loc}" key="button.previous" var="previous"/>
<fmt:message bundle="${loc}" key="button.add" var="add"/>

<body>
    <jsp:include page="fragments/header.jsp"/>
    <div class="outer">
        <table class="entries">
            <caption>${universities}</caption>
            <tr>
                <th>${universityName}</th>
                <th>${city}</th>
            </tr>
            <c:forEach items="${requestScope.universities}" var="university">
                <tr>
                    <td><a href="Controller?command=get-university&id=${university.id}">${university.name}</a></td>
                    <td>${university.city}</td>
                </tr>
            </c:forEach>
        </table>
        <paginator:display url="Controller?command=load-main" currentPage="${requestScope.page}"
                           totalPagesCount="${requestScope.pagesCount}" linksCount="${requestScope.count}"/>
        <c:if test="${sessionScope.isAuthorizedAdmin}">
            <button onclick="location.href='Controller?command=edit-university'" class="button">${add}</button>
        </c:if>
    </div>
</body>