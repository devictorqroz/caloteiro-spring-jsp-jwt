<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Dashboard - Caloteiros</title>
        <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/users/profile.css">
    </head>
    <body>
        <header>
            <c:import url="/WEB-INF/views/includes/header.jsp" />
            <h1>Recuperar Senha</h1>
        </header>
        <main>
            <form:form modelAttribute="forgotPasswordDTO" action="/password/forgot" method="post">
                <p>Por favor, insira seu email para enviarmos um link de recuperação.</p>

                <label for="email">Email:</label>
                <form:input path="email" type="email" required="true" />
                <form:errors path="email" cssClass="error-text" />

                <button type="submit">Enviar link de recuperação</button>

                <c:if test="${not empty message}">
                    <div class="success">
                        <p>${message}</p>
                    </div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="error">
                        <p>${error}</p>
                    </div>
                </c:if>
            </form:form>
            <a href="<c:url value='/auth/login'/>" class="menu-link">Voltar para o Login</a>
        </main>
        <footer>
            <c:import url="/WEB-INF/views/includes/footer.jsp" />
        </footer>
    </body>
</html>
