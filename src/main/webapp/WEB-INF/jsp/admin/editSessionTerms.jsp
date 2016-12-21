<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="../fragments/headTag.jsp"/>

<fmt:setBundle basename="locale.locale" var="loc"/>

<fmt:message bundle="${loc}" key="admin.session_start" var="sessionStart"/>
<fmt:message bundle="${loc}" key="admin.session_end" var="sessionEnd"/>
<fmt:message bundle="${loc}" key="admin.year" var="year"/>
<fmt:message bundle="${loc}" key="button.edit" var="edit"/>
<fmt:message bundle="${loc}" key="button.add" var="add"/>

<body>
    <jsp:include page="../fragments/header.jsp"/>
    <div class="edit_outer">
        <form method="post" action="${requestScope.isNew ? "Controller?command=create-session-terms"
                                                            : "Controller?command=update-session-terms"}">
            <dl>
                <dt>${year}</dt>
                <dd><input type="text" value="${requestScope.year}" disabled></dd>
            </dl>
            <dl>
                <dt>${sessionStart}</dt>
                <dd><input type="datetime-local" name="sessionStart" value="${requestScope.sessionStart}"></dd>
            </dl>
            <dl>
                <dt>${sessionEnd}</dt>
                <dd><input type="datetime-local" name="sessionEnd" value="${requestScope.sessionEnd}"></dd>
            </dl>
            <button type="submit">${requestScope.isNew ? add : edit}</button>
        </form>
    </div>
</body>