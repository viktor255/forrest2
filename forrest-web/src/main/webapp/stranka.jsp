<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<table border="1">
    <thead>
    <tr>
        <th>název</th>
    </tr>
    </thead>
    <c:forEach items="${trees}" var="tree">
        <tr>
            <td><c:out value="${tree.name}"/></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>