<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/includes.jsp" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Dashboard - Caloteiros</title>
        <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/users/profile.css">
    </head>
    <body>
        <header>
            <c:import url="${pageContext.request.contextPath}/WEB-INF/views/includes/header.jsp" />
            <h1>Dashboard - Caloteiros</h1>
        </header>
        <main>
            <form action="${pageContext.request.contextPath}/password/forgot" method="post">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
                <button type="submit">Enviar link de recuperação</button>
                <c:if test="${not empty message}">
                    <p>${message}</p>
                </c:if>
                <c:if test="${not empty error}">
                    <p class="error">${error}</p>
                </c:if>
            </form>
            <a href="/auth/login" class="menu-link">login</a>
        </main>
        <footer>
            <c:import url="${pageContext.request.contextPath}/WEB-INF/views/includes/footer.jsp" />
        </footer>
    </body>
</html>
