<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="header.login" var="login"/>
<fmt:message bundle="${loc}" key="header.password" var="password"/>
<fmt:message bundle="${loc}" key="header.main" var="main"/>
<fmt:message bundle="${loc}" key="header.home" var="home"/>
<fmt:message bundle="${loc}" key="header.log_in" var="log_in"/>
<fmt:message bundle="${loc}" key="header.log_out" var="log_out"/>

<div id="outer" align="center">
    <div id="inner">
        <c:choose>
            <c:when test="${not sessionScope.isAuthorizedAdmin and not sessionScope.isAuthorizedUser}">
                <form action="Controller?command=authorize" method="post">
                    ${login}:
                    <input type="text" name="login" size="20">
                    ${password}:
                    <input type="password" name="password" size="20">
                    <input type="submit" value="${log_in}">
                </form>
            </c:when>
            <c:otherwise>
                <button id="button" onclick="location.href='Controller?command=load-main'">${main}</button>
                <c:choose>
                    <c:when test="${sessionScope.isAuthorizedUser}">
                        <button id="button"
                            onclick="location.href='Controller?command=get-user&id=${sessionScope.user.id}'">${home}</button>
                    </c:when>
                    <c:otherwise>
                        <button id="button" onclick="location.href='Controller?command=admin-page'">${home}</button>
                    </c:otherwise>
                </c:choose>
                <button id="button" onclick="location.href='Controller?command=logout'">${log_out}</button>
            </c:otherwise>
        </c:choose>
    </div>
</div>
    <c:if test="${not empty requestScope.shownException}">
        <c:set var="error" value="${requestScope.shownException.message}"/>
        <script type="text/javascript">
            function showAuthErr(arg){
                window.alert(arg);
            }
            var error = '${error}';
            showAuthErr(error);
        </script>
    </c:if>
<style type="text/css">
    #outer {
        width: 100%;
        height: 23px;
        background: #cfcfd8;
        padding: 5px;
        padding-right: 20px;
        border: solid 1px rgba(0, 0, 0, 0.3);
    }
    #inner {
        float: inherit;
        width: 60%;
        height: 100%;
    }
    #button {
        width: 30%;
        float: left;
        height: 100%;
        border: solid 1px rgba(0, 0, 0, 0.3);
    }
</style>