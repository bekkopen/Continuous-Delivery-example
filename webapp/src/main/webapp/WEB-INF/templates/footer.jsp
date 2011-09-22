<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div align="right">
    <div>
        <fmt:message key="button.locale"/>:
            <c:url var="englishLocaleUrl" value="/index.html">
                <c:param name="locale" value="" />
            </c:url>
            <c:url var="norwegianLocaleUrl" value="/index.html">
                <c:param name="locale" value="no" />
            </c:url>
        
            <a href='<c:out value="${englishLocaleUrl}"/>'><fmt:message key="locale.english"/></a>
            <a href='<c:out value="${norwegianLocaleUrl}"/>'><fmt:message key="locale.norwegian"/></a>
    </div>
    
    <div>&nbsp;</div>
    
    <div><fmt:message key="site.footer"/></div>
</div>