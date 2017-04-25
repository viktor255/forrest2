<%@ page import="cz.muni.fi.pv168.web.TreeServlet" %>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<table border="1">
    <thead>
    <tr>
        <th>id</th>
        <th>name</th>
        <th>treeType</th>
        <th>isProtected</th>
    </tr>
    </thead>
    <c:forEach items="${trees}" var="tree">
        <tr>
            <td><c:out value="${tree.id}"/></td>
            <td><c:out value="${tree.name}"/></td>
            <td><c:out value="${tree.treeType}"/></td>
            <td><c:out value="${tree.isProtected() ? 'Yes':'No'}"/></td>
            <td><form method="post" action="${pageContext.request.contextPath}/trees/delete?id=${tree.id}"
                      style="margin-bottom: 0;"><input type="submit" value="delete"></form></td>
        </tr>
    </c:forEach>
</table>

<h2>Put tree</h2>
<c:if test="${not empty chyba}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${chyba}"/>
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/trees/add" method="post">
    <table>
        <tr>
            <th>name:</th>
            <td><input type="text" name="name" value="<c:out value='${param.name}'/>"/></td>
        </tr>
        <tr>
            <th>treeType:</th>
            <td><input type="text" name="treeType" value="<c:out value='${param.treeType}'/>"/></td>
        </tr>
        <tr>
            <th>isProtected:</th>
            <td><input type="text" name="isProtected" value="<c:out value='${param.isProtected}'/>"/></td>
        </tr>
    </table>
    <input type="Submit" value="Zadat" />
</form>


<h2>Update tree</h2>
<c:if test="${not empty chyba}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${chyba}"/>
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/trees/update" method="post">
    <table>
        <tr>
            <th>id:</th>
            <td><input type="text" name="id" value="<c:out value='${param.id}'/>"/></td>
        </tr>
        <tr>
            <th>name:</th>
            <td><input type="text" name="name" value="<c:out value='${param.name}'/>"/></td>
        </tr>
        <tr>
            <th>treeType:</th>
            <td><input type="text" name="treeType" value="<c:out value='${param.treeType}'/>"/></td>
        </tr>
        <tr>
            <th>isProtected:</th>
            <td><input type="text" name="isProtected" value="<c:out value='${param.isProtected}'/>"/></td>
        </tr>
    </table>
    <input type="Submit" value="Update" />
</form>

</body>
</html>
