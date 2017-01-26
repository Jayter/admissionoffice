<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="header.login" var="login"/>
<fmt:message bundle="${loc}" key="header.password" var="password"/>
<fmt:message bundle="${loc}" key="header.main" var="main"/>
<fmt:message bundle="${loc}" key="header.home" var="home"/>
<fmt:message bundle="${loc}" key="header.log_in" var="log_in"/>
<fmt:message bundle="${loc}" key="header.log_out" var="log_out"/>

<div class="header_outer" align="center">
    <a href="Controller?command=change-locale&locale=uk-UA">
        <img class="header_icon" src="${pageContext.request.contextPath}/resources/img/ua-icon.png"/>
    </a>
    <a href="Controller?command=change-locale&locale=us-US">
        <img class="header_icon" src="${pageContext.request.contextPath}/resources/img/en-icon.png"/>
    </a>
    <div class="header_inner">
        <button class="header_button" onclick="location.href='Controller?command=load-main'">${main}</button>
        <c:choose>
            <c:when test="${not sessionScope.isAuthorizedAdmin and not sessionScope.isAuthorizedUser}">
                <form action="Controller?command=authorize" method="post">
                    <input type="text" name="login" size="20" placeholder="${login}">
                    <input type="password" name="password" size="20" placeholder="${password}">
                    <input type="submit" value="${log_in}">
                </form>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${sessionScope.isAuthorizedUser}">
                        <button class="header_button"
                            onclick="location.href='Controller?command=get-user&id=${sessionScope.user.id}'">${home}</button>
                    </c:when>
                    <c:otherwise>
                        <button class="header_button" onclick="location.href='Controller?command=admin-page'">${home}</button>
                    </c:otherwise>
                </c:choose>
                <button class="header_button" onclick="location.href='Controller?command=logout'">${log_out}</button>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<c:if test="${not empty requestScope.shownException}">
    <c:set var="error" value="${requestScope.shownException.message}"/>
    <c:set var="redirectPath" value="${requestScope.redirectPath}"/>
    <c:choose>
        <c:when test="${redirectPath != null}">
            <script type="text/javascript">
                var error = '${error}';
                var redirectPath = '${redirectPath}';
                showAndRedirect(error, redirectPath);
            </script>
        </c:when>
        <c:otherwise>
            <script type="text/javascript">
                var error = '${error}';
                showAuthErr(error);
            </script>
        </c:otherwise>
    </c:choose>
</c:if>