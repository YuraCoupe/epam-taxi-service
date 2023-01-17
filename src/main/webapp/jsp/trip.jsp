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
        <% com.epam.rd.java.basic.taxiservice.model.Trip trip = (com.epam.rd.java.basic.taxiservice.model.Trip) request.getAttribute("trip"); %>


        <c:import url="navibar.jsp"/>
        <div class="container">
            <div class="row">
                <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                    <div class="btn-group me-2" role="group" aria-label="Second group">
                        <a href="/trips/list" type="button" class="btn btn-success"><fmt:message key="label.back.to.trips"/></a>
                    </div>
                </div>
            </div><br>
            <form action="/trips" method="post">
                <div class="form-group">
                    <div class="row">
                        <label for="id"><fmt:message key="label.trip.id"/></label><br>
                        <fmt:message key="label.trip.enter.id" var="enterId"/>
                        <input type="number" readonly="readonly" class="form-control" id="tripId" placeholder="${enterId}" name="tripId" value="${trip.id}"> <br>

                        <label for="departureAddress"><fmt:message key="label.trip.departure.address"/></label><br>
                        <select class="form-control" id="departureStreetId" name="departureStreetId" onchange="submit()">
                            <option disabled selected value><fmt:message key="label.trip.select.street"/></option>
                            <c:forEach items="${streets}" var="street">
                                <option value="${street.id}" ${street.id == trip.departureAddress.street.id ? 'selected="selected"' : ''}>
                                    <c:out value="${street.title}"/>
                                    <c:out value="${street.streetType}"/>
                                </option>
                            </c:forEach>
                        </select><br>
                        <select class="form-control" id="departureAddressId" name="departureAddressId">
                            <option disabled selected value><fmt:message key="label.trip.select.building"/></option>
                            <c:forEach items="${departureAddresses}" var="address">
                                <option value="${address.id}" ${address.id == trip.departureAddress.id ? 'selected="selected"' : ''}>
                                    <c:out value="${address.building}"/>
                                </option>
                            </c:forEach>
                        </select><br>

                        <label for="destinationAddress"><fmt:message key="label.trip.destination.address"/></label><br>
                        <select class="form-control" id="destinationStreetId" name="destinationStreetId" onchange="submit()">
                            <option disabled selected value><fmt:message key="label.trip.select.street"/></option>
                            <c:forEach items="${streets}" var="street">
                                <option value="${street.id}" ${street.id == trip.destinationAddress.street.id ? 'selected="selected"' : ''}>
                                    <c:out value="${street.title}"/>
                                    <c:out value="${street.streetType}"/>
                                </option>
                            </c:forEach>
                        </select><br>
                        <select class="form-control" id="destinationAddressId" name="destinationAddressId">
                            <option disabled selected value><fmt:message key="label.trip.select.building"/></option>
                            <c:forEach items="${destinationAddresses}" var="address">
                                <option value="${address.id}" ${address.id == trip.destinationAddress.id ? 'selected="selected"' : ''}>
                                    <c:out value="${address.building}"/>
                                </option>
                            </c:forEach>
                        </select><br>

                        <label for="passengersNumber"><fmt:message key="label.trip.passengers.number" var="passengersNumber"/>${passengersNumber}</label><br>
                        <select class="form-control" id="passengersNumber" name="passengersNumber">
                            <option disabled selected value><fmt:message key="label.trip.select.passengers.number"/></option>
                            <c:forEach var = "i" begin = "3" end = "7">
                                <option value="${i}" ${i == trip.numberOfPassengers ? 'selected="selected"' : ''}>
                                    <c:out value="${i}"/>
                                </option>
                            </c:forEach>
                        </select><br>

                        <div class="form-check form-switch">
                          <input class="form-check-input" type="checkbox" role="switch" id="allowSeveralCars" name="allowSeveralCars">
                          <label class="form-check-label" for="flexSwitchCheckDefault">
                            <fmt:message key="label.trip.switch.several.cars"/>
                          </label>
                        </div>

                        <label for="category"><fmt:message key="label.trip.category"/></label><br>
                        <select class="form-control" id="categoryId" name="categoryId">
                            <option disabled selected value><fmt:message key="label.trip.select.category"/></option>
                            <c:forEach items="${categories}" var="category">
                                <option value="${category.id}" ${category.id == trip.category.id ? 'selected="selected"' : ''}>
                                    <c:out value="${category.title}"/>
                                </option>
                            </c:forEach>
                        </select><br>
                    </div>

                    <div class="row">
                        <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                            <div class="btn-group me-2" role="group" aria-label="Second group">
                                <button type="submit" value="Submit" class="btn btn-primary"><fmt:message key="label.save"/></button>
                                <a class="btn btn-secondary" href="/trips" role="button"><fmt:message key="label.cancel"/></a>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${not empty errorMessage}">
                    <c:forEach items="${errorMessage.errors}" var="error">
                        <p style="color:red">${error}</p>
                    </c:forEach>
                </c:if>
                <c:if test="${noCarAvailable == 'true'}">
                    <fmt:message key="label.trip.select.another.category"/>
                </c:if>
            </form>
        </div>
    </body>
</html>
