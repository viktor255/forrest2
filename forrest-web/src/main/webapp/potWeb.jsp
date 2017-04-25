<%@ page import="cz.muni.fi.pv168.web.PotServlet" %>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<table border="1">
    <thead>
    <tr>
        <th>id</th>
        <th>row</th>
        <th>column</th>
        <th>capacity</th>
        <th>note</th>
    </tr>
    </thead>
    <c:forEach items="${pots}" var="pot">
        <tr>
            <td><c:out value="${pot.id}"/></td>
            <td><c:out value="${pot.row}"/></td>
            <td><c:out value="${pot.column}"/></td>
            <td><c:out value="${pot.capacity}"/></td>
            <td><c:out value="${pot.note}"/></td>
            <td><form method="post" action="${pageContext.request.contextPath}/pots/delete?id=${pot.id}"
                      style="margin-bottom: 0;"><input type="submit" value="delete"></form></td>
        </tr>
    </c:forEach>
</table>

<h2>Put pot</h2>
<c:if test="${not empty chyba}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${chyba}"/>
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/pots/add" method="post">
    <table>
        <tr>
            <th>row:</th>
            <td><input type="text" name="row" value="<c:out value='${param.row}'/>"/></td>
        </tr>
        <tr>
            <th>column:</th>
            <td><input type="text" name="column" value="<c:out value='${param.column}'/>"/></td>
        </tr>
        <tr>
            <th>capacity:</th>
            <td><input type="text" name="capacity" value="<c:out value='${param.isProtected}'/>"/></td>
        </tr>
        <tr>
            <th>note:</th>
            <td><input type="text" name="note" value="<c:out value='${param.note}'/>"/></td>
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
<form action="${pageContext.request.contextPath}/pots/update" method="post">
    <table>
        <tr>
            <th>id:</th>
            <td><input type="text" name="id" value="<c:out value='${param.id}'/>"/></td>
        </tr>
        <tr>
            <th>row:</th>
            <td><input type="text" name="row" value="<c:out value='${param.row}'/>"/></td>
        </tr>
        <tr>
            <th>column:</th>
            <td><input type="text" name="column" value="<c:out value='${param.column}'/>"/></td>
        </tr>
        <tr>
            <th>capacity:</th>
            <td><input type="text" name="capacity" value="<c:out value='${param.isProtected}'/>"/></td>
        </tr>
        <tr>
            <th>note:</th>
            <td><input type="text" name="note" value="<c:out value='${param.note}'/>"/></td>
        </tr>
    </table>
    <input type="Submit" value="Update" />
</form>

</body>
</html>
