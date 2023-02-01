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
                        <a href="/cars/list.do" type="button" class="btn btn-success"><fmt:message key="label.back.to.cars"/></a>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <fmt:message key="label.car"/> ${trip.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title"><fmt:message key="label.car.saved"/></h5>

                        <p class="card-text"><fmt:message key="label.car.model"/></p>
                        <c:out value="${car.model.brand} ${car.model.model}"/><br><br>
                        <p class="card-text"><fmt:message key="label.car.capacity"/></p>
                        <c:out value="${car.capacity}"/><br><br>
                         <p class="card-text"><fmt:message key="label.car.category"/></p>
                         <c:out value="${car.category.title}"/><br><br>
                        <p class="card-text"><fmt:message key="label.car.status"/></p>
                        <c:out value="${car.status.title}"/><br><br>
                        <c:if test="${trip.driver != 0}">
                            <p class="card-text"><fmt:message key="label.car.driver"/></p>
                            <c:out value="${car.driver.firstName} ${car.driver.lastName}"/><br><br>
                        </c:if>

                        <a href="/cars/list.do" class="btn btn-success"><fmt:message key="label.back.to.cars"/></a>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
