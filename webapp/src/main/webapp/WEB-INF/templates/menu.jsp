<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div id="side-bar">
    <a href="<c:url value="/"/>">Home</a>
    
    <p><fmt:message key="artifact.form.title"/></p>
        <a href="<c:url value="/artifact/form.html"/>"><fmt:message key="button.create"/></a> 
        <a href="<c:url value="/artifact/search.html"/>"><fmt:message key="button.search"/></a>
    <p><fmt:message key="course.form.title"/></p>
        <a href="<c:url value="/course/form.html"/>"><fmt:message key="button.create"/></a>
        <a href="<c:url value="/course/search.html"/>"><fmt:message key="button.search"/></a>
</div>
