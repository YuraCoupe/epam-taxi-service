<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="labels" var="label"/>
<fmt:setBundle basename="messages" var = "message"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
    <head>
         <c:import url="header.jsp"/>
    </head>

    <body>
        <c:import url="navibar.jsp"/>

        <div class="container">
            <c:choose>
                <c:when test = "${isLoggedIn == true}">
                    <fmt:message key="message.welcome.loggedIn" bundle = "${message}"/>
                </c:when>
                <c:otherwise>
                    <fmt:message key="message.welcome.guest" bundle = "${message}"/><a href="/loginPage.do"> <fmt:message key="label.login" bundle = "${label}"/></a>
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>
