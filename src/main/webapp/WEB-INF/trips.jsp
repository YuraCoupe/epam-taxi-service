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
            <c:if test = "${user.role.title != 'ROLE_ADMINISTRATOR'}">
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
            </c:if>
            <hr>
            <form action="" class="form-inline" method="POST">
                <c:if test = "${user.role.title=='ROLE_ADMINISTRATOR'}">
                    <div class="form-group">
                        <label for="user"><fmt:message key="label.user"/></label>
                            <select class="form-control" id="selectedUserId" name="selectedUserId">
                                <option selected value="0"><fmt:message key="label.select.user"/></option>
                                <c:forEach items="${users}" var="user">
                                <option value="${user.id}" ${user.id == selectedUserId ? 'selected="selected"' : ''}>
                                    <c:out value="${user.phoneNumber}"/>
                                    <c:out value="${user.firstName}"/>
                                    <c:out value="${user.lastName}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                </c:if>
                <div class="form-group">
                    <label for="timeFrom"><fmt:message key="label.trips.select.time.from"/></label>
                    <input type="datetime-local" class="form-control" id="startTime" name="timeFrom" value="${timeFrom}">
                </div>
                <div class="form-group">
                    <label for="timeTo"><fmt:message key="label.trips.select.time.to"/></label>
                    <input type="datetime-local" class="form-control" id="endTime" name="timeTo" value="${timeTo}">
                </div>
                <button type="submit" class="btn btn-default"><fmt:message key="label.select"/></button>
            </form>
            <hr>
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
                        <td>
                            <a href = "?tripsFieldToSort=status&tripsChangeSortOrder=true">
                                <fmt:message key="label.trip.status"/>
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
                            <a href = "?tripsFieldToSort=distance&tripsChangeSortOrder=true">
                                <fmt:message key="label.trip.distance"/>
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
                                     <c:if test = "${user.role.title='ROLE_ADMINISTRATOR'}">
                                        <a href="/trips/edit.do?id=${trip.id}" type="button" class="btn btn-warning"><fmt:message key="label.edit"/></a>
                                        <a href="/trips/delete.do?id=${trip.id}" type="button" class="btn btn-danger"><fmt:message key="label.remove"/></a>
                                     </c:if>
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
