<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<table border="1">
    <thead>
    <tr>
        <th>název</th>
        <th>autor</th>
    </tr>
    </thead>
    <c:forEach items="${trees}" var="tree">
        <tr>
            <td><c:out value="${tree.name}"/></td>
            <td><c:out value="${tree.treeType}"/></td>
            <td><form method="post" action="${pageContext.request.contextPath}/trees/delete?id=${tree.id}"
                      style="margin-bottom: 0;"><input type="submit" value="Smazat"></form></td>
        </tr>
    </c:forEach>
</table>

<h2>Zadejte knihu</h2>
<c:if test="${not empty chyba}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${chyba}"/>
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/trees/add" method="post">
    <table>
        <tr>
            <th>název knihy:</th>
            <td><input type="text" name="name" value="<c:out value='${param.name}'/>"/></td>
        </tr>
        <tr>
            <th>autor:</th>
            <td><input type="text" name="author" value="<c:out value='${param.treeType}'/>"/></td>
        </tr>
    </table>
    <input type="Submit" value="Zadat" />
</form>

</body>
</html>