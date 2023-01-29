<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />

<fmt:setBundle basename="labels"/>

<nav aria-label="Page navigation">
    <ul class="pagination">
        <li ${page == 1 ? 'class="page-item disabled"' : 'class="page-item"'}>
            <c:choose>
                <c:when test = "${page == 1}">
                    <span class="page-link"><fmt:message key="label.page.first"/></span>
                </c:when>
                <c:otherwise>
                    <a class="page-link" href="?page=1">
                        <fmt:message key="label.page.first"/>
                    </a>
                </c:otherwise>
            </c:choose>
        </li>
        <li ${page == 1 ? 'class="page-item disabled"' : 'class="page-item"'}>
            <c:choose>
                <c:when test = "${page == 1}">
                    <span class="page-link"><fmt:message key="label.page.previous"/></span>
                </c:when>
                <c:otherwise>
                    <a class="page-link" href="?page=${page-1}">
                        <fmt:message key="label.page.previous"/>
                    </a>
                </c:otherwise>
            </c:choose>
        </li>
        <c:forEach var = "i" begin = "1" end = "${param.pageCount}">
            <li ${page == i ? 'class="page-item active" aria-current="page"' : 'class="page-item"'}>
                <a class="page-link" href="?page=${i}">${i}</a>
            </li>
        </c:forEach>
        <li ${page == pageCount ? 'class="page-item disabled"' : 'class="page-item"'}>
            <c:choose>
                <c:when test = "${page == pageCount}">
                    <span class="page-link"><fmt:message key="label.page.next"/></span>
                </c:when>
                <c:otherwise>
                    <a class="page-link" href="?page=${page+1}">
                        <fmt:message key="label.page.next"/>
                    </a>
                </c:otherwise>
            </c:choose>
        </li>
        <li ${page == pageCount ? 'class="page-item disabled"' : 'class="page-item"'}>
            <c:choose>
                <c:when test = "${page == pageCount}">
                    <span class="page-link"><fmt:message key="label.page.last"/></span>
                </c:when>
                <c:otherwise>
                    <a class="page-link" href="?page=${pageCount}">
                        <fmt:message key="label.page.last"/>
                    </a>
                </c:otherwise>
            </c:choose>
        </li>
    </ul>
</nav>