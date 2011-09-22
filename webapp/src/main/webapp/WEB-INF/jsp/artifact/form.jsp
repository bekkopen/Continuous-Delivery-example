<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<h1><fmt:message key="artifact.form.title"/></h1>

<c:if test="${not empty statusMessageKey}">
    <p><fmt:message key="${statusMessageKey}"/></p>
</c:if>

<c:url var="url" value="/artifact/form.html" /> 
<form:form action="${url}" commandName="artifact">
    <form:hidden path="id" />

    <fieldset>
        <div class="form-row">
            <label for="groupId"><fmt:message key="artifact.form.groupId"/>:</label>
            <span class="input"><form:input path="groupId" /></span>
        </div>       
        <div class="form-row">
            <label for="artifactId"><fmt:message key="artifact.form.artifactId"/>:</label>
            <span class="input"><form:input path="artifactId" /></span>
        </div>
        <div class="form-row">
            <label for="version"><fmt:message key="artifact.form.version"/>:</label>
            <span class="input"><form:input path="version" /></span>
        </div>
        <div class="form-row">
            <label for="packaging"><fmt:message key="artifact.form.packaging"/>:</label>
            <span class="input"><form:input path="packaging" /></span>
        </div>
        <div class="form-buttons">
            <div class="button"><input name="submit" type="submit" value="<fmt:message key="button.save"/>" /></div>
        </div>
    </fieldset>
</form:form>