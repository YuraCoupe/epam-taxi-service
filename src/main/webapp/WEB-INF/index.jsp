<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <c:choose>
                <c:when test = "${isLoggedIn == true}">
                    Welcome to Taxi Service.<br>
                    Use navigation bar to proceed.

                </c:when>
                <c:otherwise>
                    Welcome to Taxi Service.<br>
                    Please, <a href="/loginPage.do">login</a> to proceed.
                </c:otherwise>
            </c:choose>
        </div>
    </body>
</html>
