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
                        <a href="/cars" type="button" class="btn btn-success"><fmt:message key="label.back.to.cars"/></a>
                    </div>
                </div>
            </div><br>
            <form action="/cars" method="post">
                <div class="form-group">
                    <div class="row">
                        <label for="id"><fmt:message key="label.car.id"/></label><br>
                        <fmt:message key="label.car.enter.id" var="enterId"/>
                        <input type="number" readonly="readonly" class="form-control" id="carId" placeholder="${enterId}" name="carId" value="${car.id}"> <br>

                        <label for="brand"><fmt:message key="label.car.model"/></label><br>
                        <select class="form-control" id="id" name="id">
                            <option disabled selected value><fmt:message key="label.car.select.model"/></option>
                            <c:forEach items="${carModels}" var="carModel">
                                <option value="${carModel.id}" ${carModel.id == car.model.id ? 'selected="selected"' : ''}>
                                    <c:out value="${carModel.brand}"/>
                                    <c:out value="${carModel.model}"/>
                                </option>
                            </c:forEach>
                        </select><br>

                        <label for="licensePlate"><fmt:message key="label.car.licenseplate"/>${licensePlate}</label><br>
                        <fmt:message key="label.car.enter.licenseplate" var="enterLicensePlate"/>
                        <input type="text" class="form-control" id="licensePlate" placeholder="${enterLicensePlate}"
                        name="licensePlate" value="${car.licensePlate}"> <br>

                        <label for="capacity"><fmt:message key="label.car.capacity" var="carCapacity"/>${carCapacity}</label><br>
                        <select class="form-control" id="capacity" name="capacity">
                            <option disabled selected value><fmt:message key="label.car.select.capacity"/></option>
                            <c:forEach var = "i" begin = "3" end = "7">
                                <option value="${i}" ${i == car.capacity ? 'selected="selected"' : ''}>
                                    <c:out value="${i}"/>
                                </option>
                            </c:forEach>
                        </select><br>

                        <label for="category"><fmt:message key="label.car.category"/></label><br>
                        <select class="form-control" id="category" name="categoryId">
                            <option disabled selected value><fmt:message key="label.car.select.category"/></option>
                            <c:forEach items="${carCategories}" var="carCategory">
                                <option value="${carCategory.id}" ${carCategory.id == car.category.id ? 'selected="selected"' : ''}>
                                    <c:out value="${carCategory.title}"/>
                                </option>
                            </c:forEach>
                        </select><br>

                        <label for="status"><fmt:message key="label.car.status"/></label><br>
                        <select class="form-control" id="status" name="statusId">
                            <option disabled selected value><fmt:message key="label.car.select.status"/></option>
                            <c:forEach items="${carStatuses}" var="carStatus">
                                <option value="${carStatus.id}" ${carStatus.title == car.status.title ? 'selected="selected"' : ''}>
                                    <c:out value="${carStatus.title}"/>
                                </option>
                            </c:forEach>
                        </select><br>

                        <label for="driver"><fmt:message key="label.car.driver"/></label><br>
                        <select class="form-control" id="driverId" name="driverId">
                            <option selected value><fmt:message key="label.car.select.driver"/></option>
                            <c:choose>
                                <c:when test = "${car.driver.id != null}">
                                    <option value="${car.driver.id}" selected="selected">
                                        <c:out value="${car.driver.firstName}"/>
                                        <c:out value="${car.driver.lastName}"/>
                                    </option>
                                </c:when>
                            </c:choose>
                            <c:forEach items="${freeDrivers}" var="driver">
                                <option value="${driver.id}" ${driver.id == car.driver.id ? 'selected="selected"' : ''}>
                                    <c:out value="${driver.firstName}"/>
                                    <c:out value="${driver.lastName}"/>
                                </option>
                            </c:forEach>
                        </select><br>
                    </div>

                    <div class="row">
                        <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                            <div class="btn-group me-2" role="group" aria-label="Second group">
                                <button type="submit" value="Submit" class="btn btn-primary"><fmt:message key="label.save"/></button>
                            </div>
                        </div>
                    </div>
                </div>
                <c:if test="${not empty errorMessage}">
                    <c:forEach items="${errorMessage.errors}" var="error">
                        <p style="color:red">${error}</p>
                    </c:forEach>
                </c:if>
            </form>
        </div>
    </body>
</html>
