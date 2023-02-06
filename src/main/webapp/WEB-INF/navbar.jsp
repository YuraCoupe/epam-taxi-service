<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />

<fmt:setBundle basename="labels"/>

<nav class="navbar navbar-inverse">
    <div class="container">
        <div class="navbar-header">
            <!-- <a class="navbar-brand" href="/">
                <img src="${pageContext.request.contextPath}/images/taxi-logo.jpg" width="30" height="30" alt="taxi logo"/>
            </a>-->
            <a class="navbar-brand" href="/">Taxi Service</a>
        </div>
        <ul class="nav navbar-nav">
            <li><a href="/"><fmt:message key="label.main"/></a></li>
            <c:if test = "${not empty sessionScope.isLoggedIn}">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="/trips/list.do">
                    <fmt:message key="label.trips"/><span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="/trips/list.do"><fmt:message key="label.list"/></a></li>
                    <c:if test = "${sessionScope.user.role.title == 'ROLE_CLIENT'}">
                    <li><a href="/trips/new.do"><fmt:message key="label.create"/></a></li>
                    </c:if>
                </ul>
            </li>
            </c:if>
            <c:if test = "${sessionScope.user.role.title == 'ROLE_ADMINISTRATOR'}">
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="/cars/list.do">
                    <fmt:message key="label.cars"/><span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="/cars/list.do"><fmt:message key="label.list"/></a></li>
                    <li><a href="/cars/new.do"><fmt:message key="label.add"/></a></li>
                </ul>
                </li>
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="/drivers/list.do">
                    <fmt:message key="label.drivers"/><span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="/drivers/list.do"><fmt:message key="label.list"/></a></li>
                    <li><a href="/drivers/new.do"><fmt:message key="label.add"/></a></li>
                </ul>
            </li>
            <li class="dropdown">
                <a class="dropdown-toggle" data-toggle="dropdown" href="/clients/list.do">
                    <fmt:message key="label.users"/><span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="/clients/list.do"><fmt:message key="label.list"/></a></li>
                    <li><a href="/clients/new.do"><fmt:message key="label.add"/></a></li>
                </ul>
            </li>
            </c:if>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <c:choose>
                <c:when test = "${not empty sessionScope.isLoggedIn}">
                    <li>
                        <a style="float: right" href="/logout.do"><fmt:message key="label.logout"/></a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li>
                        <a style="float: right" href="/loginPage.do"><fmt:message key="label.login"/></a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
        <form class="navbar-form navbar-right">
            <div class="form-group">
                <div class="row">
                    <select id="language" name="language" onchange="submit()" class="form-control">
                        <option value="en" ${language == 'en' ? 'selected' : ''}><fmt:message key="label.language.en"/></option>
                        <option value="uk" ${language == 'uk' ? 'selected' : ''}><fmt:message key="label.language.uk"/></option>
                    </select>
                </div>
            </div>
        </form>
    </div>
</nav>