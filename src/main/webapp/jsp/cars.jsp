<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <c:import url="navibar.jsp"/>
        <div class="container">
            <form action="/cars">
                <div class="form-group">
                    <label for="id"><fmt:message key="label.car"/></label><br>
                    <select class="form-control" id="id" name="id">
                        <option disabled selected value><fmt:message key="label.select.car"/></option>
                        <c:forEach items="${cars}" var="car">
                            <option value="${car.id}">
                                <c:out value="${car.model.brand}"/>
                                <c:out value="${car.model.model}"/>
                                <c:out value="${car.licensePlate}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                    <input type="submit" value="Search">
            </form><br>

            <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                <div class="btn-group me-2" role="group" aria-label="Second group">
                   <a href="/cars/new" type="button" class="btn btn-primary"><fmt:message key="label.add"/></a>
                </div>
            </div>

            <table class="table table-hover">
                <thead>
                    <tr>
                        <td><fmt:message key="label.brand"/></td>
                        <td><fmt:message key="label.model"/></td>
                        <td><fmt:message key="label.car.capacity"/></td>
                        <td><fmt:message key="label.car.category"/></td>
                        <td><fmt:message key="label.car.status"/></td>
                        <td><fmt:message key="label.car.driver"/></td>
                        <td><fmt:message key="label.car.licenseplate"/></td>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${cars}" var="car">
                    <tr>
                        <td>
                            <c:out value="${car.model.brand}"/>
                        </td>
                        <td>
                            <c:out value="${car.model.model}"/>
                        </td>
                        <td>
                            <c:out value="${car.capacity}"/>
                        </td>
                        <td>
                            <c:out value="${car.category.title}"/>
                        </td>
                        <td>
                            <c:out value="${car.status.title}"/>
                        </td>
                        <td>
                            <c:out value="${car.driver.firstName}"/>
                            <c:out value="${car.driver.lastName}"/>
                        </td>
                        <td>
                            <c:out value="${car.licensePlate}"/>
                        </td>
                        <td>
                            <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                                <div class="btn-group me-2" role="group" aria-label="Second group">
                                     <a href="/cars/${car.id}" type="button" class="btn btn-warning"><fmt:message key="label.edit"/></a>
                                     <a href="/cars?deleteId=${car.id}" type="button" class="btn btn-danger"><fmt:message key="label.remove"/></a>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>

            <c:import url="paginationnavibar.jsp?page=${page}&pageCount=${pageCount}"/>


        </div>

    </body>
</html>
