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

        <c:import url="navibar.jsp"/>

        <div class="container">
            <div class="row">
                <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                    <div class="btn-group me-2" role="group" aria-label="Second group">
                        <a href="/trips/list" type="button" class="btn btn-success"><fmt:message key="label.back.to.trips"/></a>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <fmt:message key="label.trip"/> ${trip.id}
                    </div>
                    <div class="card-body">
                        <h5 class="card-title"><fmt:message key="label.trip.confirmed"/></h5>
                        <p class="card-text"><fmt:message key="label.trip.departure.address"/></p>
                        <c:out value="${trip.departureAddress}"/><br><br>
                        <p class="card-text"><fmt:message key="label.trip.destination.address"/></p>
                        <c:out value="${trip.destinationAddress}"/><br><br>
                        <p class="card-text"><fmt:message key="label.trip.distance"/></p>
                        <c:out value="${trip.distance} km"/><br><br>
                        <p class="card-text"><fmt:message key="label.trip.category"/></p>
                        <c:out value="${trip.category.title}"/><br><br>
                        <p class="card-text"><fmt:message key="label.trip.passengers.number"/></p>
                        <c:out value="${trip.numberOfPassengers}"/><br><br>
                        <p class="card-text"><fmt:message key="label.trip.status"/></p>
                        <c:out value="${trip.status.title}"/><br><br>
                        <c:if test="${not empty trip.cars}">
                            <p class="card-text"><fmt:message key="label.cars"/></p>
                            <c:forEach items="${trip.cars}" var="car">
                                <p>${car.model.brand} ${car.model.model} ${car.licensePlate}</p>
                                <p><fmt:message key="label.car.driver"/> ${car.driver.firstName}</p>
                            </c:forEach>
                        </c:if>
                        <c:if test = "${user.role.title == 'ROLE_DRIVER'}">
                        <c:if test = "${trip.status.title == 'Open'}">
                            <form action="/trips/processing" method="post">
                                <div class="col-sm-12 controls">
                                    <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                                        <input type="hidden" id="tripId" name="tripId" value="${trip.id}">
                                        <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                                            <div class="btn-group me-2" role="group" aria-label="Second group">
                                                <button type="submit" value="Submit" class="btn btn-primary"><fmt:message key="label.trip.driver.arrive"/></button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </c:if>
                        <c:if test = "${trip.status.title == 'Processing' && not empty activeTripId}">
                            <form action="/trips/finish" method="post">
                                <div class="col-sm-12 controls">
                                    <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                                        <input type="hidden" id="tripId" name="tripId" value="${trip.id}">
                                        <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                                            <div class="btn-group me-2" role="group" aria-label="Second group">
                                                <button type="submit" value="Submit" class="btn btn-primary"><fmt:message key="label.trip.finish"/></button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </c:if>
                        </c:if>

                        <a href="/trips/list" class="btn btn-success"><fmt:message key="label.back.to.trips"/></a>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
