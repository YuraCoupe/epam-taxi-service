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
        <c:if test = "${isLoggedIn == true}">
            <c:import url="navibar.jsp"/>
        </c:if>
        <div class="container">
            <c:choose>
            <c:when test = "${isLoggedIn == true}">
                <div class="row">
                    <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                        <div class="btn-group me-2" role="group" aria-label="Second group">
                            <a href="/clients/view.do" type="button" class="btn btn-success"><fmt:message key="label.back.to.users"/></a>
                        </div>
                    </div>
                </div><br>
            </c:when>

            <c:otherwise>
                <div id="signupbox" style="margin-top:50px;" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <div class="panel-title">Sign Up</div>
                            <div style="float:right; font-size: 85%; position: relative; top:-10px"><a id="signinlink" href="${pageContext.request.contextPath}/login">Sign In</a></div>
                        </div>
                        <div class="panel-body" >
            </c:otherwise>
            </c:choose>
            <form ${user.id == null ? 'action="/clients/save.do"' :'action="/clients/update.do"'}  method="post">
                <div class="form-group">
                    <div class="row">
                         <label for="id"><fmt:message key="label.user.id"/></label><br>
                         <fmt:message key="label.user.enter.id" var="enterId"/>
                         <input type="number" readonly="readonly" class="form-control" id="userId" placeholder="${enterId}" name="userId" value="${user.id}"> <br>

                        <label for="phoneNumber"><fmt:message key="label.user.phoneNumber"/></label><br>
                        <fmt:message key="label.user.enter.phoneNumber" var="enterPhoneNumber"/>
                        <input type="text" class="form-control" id="phoneNumber" placeholder="${enterPhoneNumber}"
                        name="phoneNumber" value="${user.phoneNumber}"> <br>

                        <label for="password"><fmt:message key="label.user.password"/></label><br>
                        <fmt:message key="label.user.enter.password" var="enterPassword"/>
                        <input type="password" class="form-control" id="password" placeholder="${enterPassword}"
                        name="password" value="${user.password}"> <br>

                        <label for="firstName"><fmt:message key="label.user.firstName"/></label><br>
                        <fmt:message key="label.user.enter.firstName" var="enterFirstName"/>
                        <input type="text" class="form-control" id="firstName" placeholder="${enterFirstName}"
                        name="firstName" value="${user.firstName}"> <br>

                         <label for="lastName"><fmt:message key="label.user.lastName"/></label><br>
                         <fmt:message key="label.user.enter.lastName" var="enterLastName"/>
                         <input type="text" class="form-control" id="lastName" placeholder="${enterLastName}"
                         name="lastName" value="${user.lastName}"> <br>

                        <c:if test = "${isLoggedIn == true}">
                        <label for="role"><fmt:message key="label.user.role"/></label><br>
                        <select class="form-control" id="roleId" name="roleId">
                            <option disabled selected value><fmt:message key="label.user.select.role"/></option>
                            <c:forEach items="${roles}" var="role">
                                <option value="${role.id}" ${role.id == user.role.id ? 'selected="selected"' : ''}>
                                    <c:out value="${role.title}"/>
                                </option>
                            </c:forEach>
                        </select><br>
                        </c:if>

                    <div class="col-sm-12 controls">
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
            <c:if test = "${isLoggedIn != true}">
                </div></div>
            </c:if>
        </div>
    </body>
</html>
