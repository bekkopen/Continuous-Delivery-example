<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="feature" uri="http://open.bekk.no/jsp/jstl/feature" %>

<h1><fmt:message key="course.form.title"/></h1>

<c:if test="${not empty statusMessageKey}">
    <p><fmt:message key="${statusMessageKey}"/></p>
</c:if>

<c:url var="url" value="/course/form.html"/>
<form:form action="${url}" commandName="course">
    <form:hidden path="id"/>

    <fieldset>
        <div class="form-row">
            <label for="name"><fmt:message key="course.form.name"/>:</label>
            <span class="input"><form:input path="name"/></span>
        </div>
        <div class="form-row">
            <label for="location"><fmt:message key="course.form.location"/>:</label>
            <span class="input"><form:input path="location"/></span>
        </div>
        <div class="form-row">
            <label for="date"><fmt:message key="course.form.date"/>:</label>
            <span class="input"><form:input path="date"/></span>
        </div>
        <div class="form-buttons">
            <div class="button"><input name="submit" type="submit" value="<fmt:message key="button.save"/>"/></div>
        </div>

    </fieldset>
</form:form>
<feature:enabled name="Course.Attendants">
    <fieldset>
        <h1><fmt:message key="course.form.attendants"/></h1>
        <table class="search">
            <tr>
                <th><fmt:message key="attendant.form.name"/></th>
                <th><fmt:message key="attendant.form.email"/></th>
            </tr>
            <c:forEach var="attendant" items="${course.attendants}" varStatus="status">
                <tr>
                    <c:set var="attendantFormId" value="course${status.index}"/>

                    <c:url var="editUrl" value="/attendant/form.html">
                        <c:param name="id" value="${attendant.id}"/>
                    </c:url>
                    <c:url var="deleteUrl" value="/course/delete.html"/>
                    <form id="${attendantFormId}" action="${deleteUrl}" method="POST">
                        <input id="id" name="id" type="hidden" value="${attendant.id}"/>
                    </form>

                    <td>${attendant.name}</td>
                    <td>${attendant.email}</td>
                    <td>
                        <a href='<c:out value="${editUrl}"/>'><fmt:message key="button.edit"/></a>
                        <a href="javascript:document.forms['${courseFormId}'].submit();"><fmt:message key="button.delete"/></a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </fieldset>
</feature:enabled>