<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "ctg" uri = "custom.tld"%>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="labels"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
    <head>
         <c:import url="header.jsp"/>
    </head>

    <body>
        <c:import url="navbar.jsp"/>
        <div class="container">
            <form action="/trips">
                <div class="form-group">
                    <label for="id"><fmt:message key="label.trip"/></label><br>
                    <select class="form-control" id="id" name="id">
                        <option disabled selected value><fmt:message key="label.select.trip"/></option>
                        <c:forEach items="${trips}" var="trip">
                            <option value="${trip.id}">
                                <c:out value="${trip.id}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                    <input type="submit" value="Search">
            </form><br>
                <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                    <div class="btn-group me-2" role="group" aria-label="Second group">
                        <c:choose>
                            <c:when test = "${not empty sessionScope.activeTripId}">
                                <a href="/trips/view.do?id=${activeTripId}" type="button" class="btn btn-success"><fmt:message key="label.trip.active"/></a>
                            </c:when>
                            <c:otherwise>
                                <a href="/trips/new.do" type="button" class="btn btn-primary"><fmt:message key="label.add"/></a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

            <table class="table table-hover">
                <thead>
                    <tr>
                        <td><fmt:message key="label.user"/></td>
                        <td><fmt:message key="label.trip.departure.address"/></td>
                        <td><fmt:message key="label.trip.destination.address"/></td>
                        <td>
                            <a href = "?tripsFieldToSort=open_time&tripsChangeSortOrder=true">
                                <fmt:message key="label.trip.openTime"/>
                                <c:choose>
                                    <c:when test = "${sessionScope.tripsSortOrder == 'DESC'}">
                                        <i class="fa fa-caret-down"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa fa-caret-up"></i>
                                    </c:otherwise>
                                </c:choose>
                            </a>
                        </td>
                        <td>
                            <a href = "?tripsFieldToSort=price&tripsChangeSortOrder=true">
                                <fmt:message key="label.trip.price"/>
                                <c:choose>
                                    <c:when test = "${sessionScope.tripsSortOrder == 'DESC'}">
                                        <i class="fa fa-caret-down"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="fa fa-caret-up"></i>
                                    </c:otherwise>
                                </c:choose>
                            </a>
                        </td>
                        <td><fmt:message key="label.trip.category"/></td>
                        <td><fmt:message key="label.trip.status"/></td>
                        <td><fmt:message key="label.trip.distance"/></td>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${trips}" var="trip">
                    <tr>
                        <td>
                            <c:out value="${trip.user.phoneNumber}"/>
                            <c:out value="${trip.user.firstName}"/>
                            <c:out value="${trip.user.lastName}"/>
                        </td>
                        <td>
                            <c:out value="${trip.departureAddress}"/>
                        </td>
                        <td>
                            <c:out value="${trip.destinationAddress}"/>
                        </td>
                        <td>
                            <c:out value="${trip.openTime}"/>
                        </td>
                        <td>
                            <c:out value="${trip.price}"/>
                        </td>
                        <td>
                            <c:out value="${trip.category.title}"/>
                        </td>
                        <td>
                            <c:out value="${trip.status.title}"/>
                        </td>
                        <td>
                            <c:out value="${trip.distance}"/>
                        </td>
                        <td>
                            <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                                <div class="btn-group me-2" role="group" aria-label="Second group">
                                     <a href="/trips/view.do?id=${trip.id}" type="button" class="btn btn-info"><fmt:message key="label.view"/></a>
                                     <a href="/trips/edit.do?id=${trip.id}" type="button" class="btn btn-warning"><fmt:message key="label.edit"/></a>
                                     <a href="/trips/delete.do?id=${trip.id}" type="button" class="btn btn-danger"><fmt:message key="label.remove"/></a>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>

            <ctg:pagination page = "${page}" pageCount = "${pageCount}" />

        </div>

    </body>
</html>
