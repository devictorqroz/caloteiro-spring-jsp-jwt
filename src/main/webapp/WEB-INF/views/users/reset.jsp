<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Redefinir Senha</title>
        <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/users/profile.css">
    </head>
    <body>
        <header>
            <c:import url="/WEB-INF/views/includes/header.jsp" />
            <h1>Redefinir Senha</h1>
        </header>
        <main>
            <form:form modelAttribute="resetPasswordDTO" action="/password/reset" method="post">
                <form:hidden path="token" />

                <label for="password">Nova Senha:</label>
                <form:password path="password" required="true" />
                <form:errors path="password" cssClass="error-text" />

                <label for="confirmPassword">Confirmar Nova Senha:</label>
                <form:password path="confirmPassword" required="true" />
                <form:errors path="confirmPassword" cssClass="error-text" />

                <button type="submit">Redefinir Senha</button>

                <c:if test="${not empty error}">
                    <div class="error">
                        <p>${error}</p>
                    </div>
                </c:if>

            </form:form>
            <a href='<c:url value="/auth/login"/>' class="menu-link">Voltar para o Login</a>
        </main>
        <footer>
            <c:import url="/WEB-INF/views/includes/footer.jsp" />
        </footer>
    </body>
</html>
