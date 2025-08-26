<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello JSP</title>
</head>
<body>
    <h1>Query</h1>

    <form action="myservlet" method="get">
        <input type="hidden" name="action" value="mainFunction">
        <button type="submit">Call Main Function</button>
    </form>
</body>
</html>
