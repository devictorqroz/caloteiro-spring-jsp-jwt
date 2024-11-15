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
            <form action="${pageContext.request.contextPath}/password/reset" method="post">
                <input type="hidden" name="token" value="${token}">
                <label for="password">New Password:</label>
                <input type="password" id="password" name="password" required>
                <label for="confirmPassword">Confirm Password:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" required>
                <button type="submit">Redefinir password</button>
                <c:if test="${not empty message}">
                    <p>${message}</p>
                </c:if>
                <c:if test="${not empty error}">
                    <p class="error">${error}</p>
                </c:if>
            </form>
            <a href="/auth/login" class="menu-link">Login</a>
        </main>
        <footer>
            <c:import url="${pageContext.request.contextPath}/WEB-INF/views/includes/footer.jsp" />
        </footer>
    </body>
</html>
