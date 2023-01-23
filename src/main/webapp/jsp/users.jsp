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
            <form action="/users">
                <div class="form-group">
                    <label for="id"><fmt:message key="label.user"/></label><br>
                    <select class="form-control" id="id" name="id">
                        <option disabled selected value><fmt:message key="label.select.user"/></option>
                        <c:forEach items="${persons}" var="user">
                            <option value="${user.id}">
                                <c:out value="${user.phoneNumber}"/>
                                <c:out value="${user.firstName}"/>
                                <c:out value="${user.lastName}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                    <input type="submit" value="Search">
            </form><br>

            <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                <div class="btn-group me-2" role="group" aria-label="Second group">
                   <a href="/users/new" type="button" class="btn btn-primary"><fmt:message key="label.add"/></a>
                </div>
            </div>

            <table class="table table-hover">
                <thead>
                    <tr>
                        <td><fmt:message key="label.user.phoneNumber"/></td>
                        <td><fmt:message key="label.user.firstName"/></td>
                        <td><fmt:message key="label.user.lastName"/></td>
                        <td><fmt:message key="label.user.role"/></td>
                    </tr>
                </thead>
                <tbody>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td>
                            <c:out value="${user.phoneNumber}"/>
                        </td>
                        <td>
                            <c:out value="${user.firstName}"/>
                        </td>
                        <td>
                            <c:out value="${user.lastName}"/>
                        </td>
                        <td>
                            <c:out value="${user.role.title}"/>
                        </td>
                        <td>
                            <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
                                <div class="btn-group me-2" role="group" aria-label="Second group">
                                     <a href="/users/${user.id}" type="button" class="btn btn-warning"><fmt:message key="label.edit"/></a>
                                     <a href="/users?deleteId=${user.id}" type="button" class="btn btn-danger"><fmt:message key="label.remove"/></a>
                                </div>
                            </div>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>

            <c:import url="paginationNavibar.jsp?page=${page}&pageCount=${pageCount}"/>


        </div>

    </body>
</html>
