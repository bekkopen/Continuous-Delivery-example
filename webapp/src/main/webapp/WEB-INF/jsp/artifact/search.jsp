<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1><fmt:message key="artifact.search.title"/></h1>

<table class="search">
    <tr>
        <th><fmt:message key="artifact.form.groupId"/></th>
        <th><fmt:message key="artifact.form.artifactId"/></th>
        <th><fmt:message key="artifact.form.version"/></th>
        <th><fmt:message key="artifact.form.packaging"/></th>
    </tr>
<c:forEach var="artifact" items="${artifacts}" varStatus="status">
    <tr>
        <c:set var="artifactFormId" value="artifact${status.index}"/>

        <c:url var="editUrl" value="/artifact/form.html">
            <c:param name="id" value="${artifact.id}" />
        </c:url>
        <c:url var="deleteUrl" value="/artifact/delete.html"/>
        <form id="${artifactFormId}" action="${deleteUrl}" method="POST">
            <input id="id" name="id" type="hidden" value="${artifact.id}"/>
        </form>

    	<td>${artifact.groupId}</td>
        <td>${artifact.artifactId}</td>
        <td>${artifact.version}</td>
        <td>${artifact.packaging}</td> 
    	<td>
            <a href='<c:out value="${editUrl}"/>'><fmt:message key="button.edit"/></a>
            <a href="javascript:document.forms['${artifactFormId}'].submit();"><fmt:message key="button.delete"/></a> 
        </td>
    </tr>
</c:forEach>
</table>