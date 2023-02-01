<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="labels"/>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<!DOCTYPE html>
<html>
    <head>
         <c:import url="header.jsp"/>
    </head>

    <body>

        <c:import url="navbar.jsp"/>

        <div class="container">
            <div class="row">
                <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                    <div class="btn-group me-2" role="group" aria-label="Second group">
                        <a href="/clients/list.do" type="button" class="btn btn-success"><fmt:message key="label.back.to.users"/></a>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <fmt:message key="label.user"/> ${trip.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title"><fmt:message key="label.user.saved"/></h5>

                        <p class="card-text"><fmt:message key="label.user.phoneNumber"/></p>
                        <c:out value="${user.phoneNumber}"/><br><br>
                        <p class="card-text"><fmt:message key="label.user.firstName"/> <fmt:message key="label.user.lastName"/></p>
                        <c:out value="${user.firstName} ${user.lastName}"/><br><br>
                         <p class="card-text"><fmt:message key="label.user.sumSpent"/></p>
                         <c:out value="${user.sumSpent} UAH"/><br><br>
                        <p class="card-text"><fmt:message key="label.user.discountRate"/></p>
                        <c:out value="${user.discountRate}%"/><br><br>

                        <a href="/clients/list.do" class="btn btn-success"><fmt:message key="label.back.to.users"/></a>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
