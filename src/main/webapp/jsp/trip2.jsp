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

         <script type='text/javascript'>
             function GetMap() {
                 Microsoft.Maps.loadModule('Microsoft.Maps.AutoSuggest', {
                     callback: function () {
                         var viewRect = Microsoft.Maps.LocationRect.fromCorners(new Microsoft.Maps.Location(50.585184, 30.288197),
                                     new Microsoft.Maps.Location(50.207098, 30.738492));
                         var manager = new Microsoft.Maps.AutosuggestManager({
                             placeSuggestions: false,
                             countryCode: 'UA',
                             bounds: viewRect
                             });
                         manager.attachAutosuggest('#searchBox', '#searchBoxContainer', selectedSuggestion);
                        var manager2 = new Microsoft.Maps.AutosuggestManager({
                             placeSuggestions: false,
                             countryCode: 'UA',
                             bounds: viewRect
                             });
                         manager2.attachAutosuggest('#searchBox2', '#searchBoxContainer', selectedSuggestion2);
                     },
                     errorCallback: function(msg){
                         alert(msg);
                     },
                     credentials: "${msKey}"
                 });
             }

             function selectedSuggestion(result) {
                 //Populate the address textbox values.
                 document.getElementById('addressLineTbx').value = result.address.addressLine || '';
             }

            function selectedSuggestion2(result) {
                 //Populate the address textbox values.
                 document.getElementById('addressLineTbx2').value = result.address.addressLine || '';
             }
         </script>

         <script type='text/javascript' src='http://www.bing.com/api/maps/mapcontrol?callback=GetMap' async defer></script>

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
                    <div class="row" id='searchBoxContainer'>
                        <label for="id"><fmt:message key="label.trip.id"/></label><br>
                        <fmt:message key="label.trip.enter.id" var="enterId"/>
                        <input type="number" readonly="readonly" class="form-control" id="tripId" placeholder="${enterId}" name="tripId" value="${trip.id}"> <br>

                        <label for="departureAddress"><fmt:message key="label.trip.departure.address"/></label><br>
                        <fmt:message key="label.trip.select.departure.address" var="selectDepartureAddress"/>
                        <input type="text" class="form-control" id="searchBox" placeholder="${selectDepartureAddress}"> <br>
                        <input type="text" readonly="readonly" class="form-control" id="addressLineTbx"
                            name="departureAddress" value="${trip.departureAddress}" placeholder="${selectDepartureAddress}"> <br>

                        <label for="destinationAddress"><fmt:message key="label.trip.destination.address"/></label><br>
                         <fmt:message key="label.trip.select.destination.address" var="selectDestinationAddress"/>
                         <input type="text" class="form-control" id="searchBox2" placeholder="${selectDestinationAddress}"> <br>
                         <input type="text" readonly="readonly" class="form-control" id="addressLineTbx2"
                             name="destinationAddress" value="${trip.destinationAddress}" placeholder="${selectDepartureAddress}"> <br>

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
