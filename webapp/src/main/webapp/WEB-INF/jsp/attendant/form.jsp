<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<h1><fmt:message key="attendant.form.title"/></h1>

<c:if test="${not empty statusMessageKey}">
    <p><fmt:message key="${statusMessageKey}"/></p>
</c:if>

<c:url var="url" value="/attendant/form.html"/>
<form:form action="${url}" commandName="attendant">
    <form:hidden path="id"/>
    <form:hidden path="course.id"/>

    <fieldset>
        <div class="form-row">
            <label for="name"><fmt:message key="attendant.form.name"/>:</label>
            <span class="input"><form:input path="name"/></span>
        </div>
        <div class="form-row">
            <label for="e-mail"><fmt:message key="attendant.form.email"/>:</label>
            <span class="input"><form:input path="eMail"/></span>
        </div>
        <div class="form-buttons">
            <div class="button"><input name="submit" type="submit" value="<fmt:message key="button.save"/>"/></div>
        </div>
    </fieldset>

</form:form>