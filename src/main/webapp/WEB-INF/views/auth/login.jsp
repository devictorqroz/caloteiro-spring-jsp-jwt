<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
	<head>
	    <meta charset="UTF-8">
		<title>Login</title>
		<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
		<script src="${pageContext.request.contextPath}/javascript/jquery-3.7.1.min.js" defer></script>
		<script src="${pageContext.request.contextPath}/javascript/jquery.maskMoney.min.js" defer></script>
		<script src="${pageContext.request.contextPath}/javascript/jquery.inputmask.min.js" defer></script>
		<script src="${pageContext.request.contextPath}/javascript/jquery.validate.min.js" defer></script>
		<script src="${pageContext.request.contextPath}/javascript/masks.js" defer></script>
	</head>
	<body>
		<header>
			<c:import url="/WEB-INF/views/includes/header.jsp" />
			<h1>Caloteiros</h1>
		</header>
		<main>
			<form:form modelAttribute="loginRequest" action="/auth/login" method="POST" id="formLogin">
                <c:if test="${param.error}">
                    <div class="error">
                        <p>Email ou senha inválidos.</p>
                    </div>
                </c:if>

                <c:if test="${param.success}">
                    <div class="success">
                        <p>Usuário registrado com sucesso. Por favor, faça o login.</p>
                    </div>
                </c:if>

				<app:textField
					nameField="email"
					label="Email:"
					id="labelEmail"
					value="" />
                <form:errors path="email" cssClass="error-text" />

				<app:password
					passwordField="password"
					label="Password:"
					id="labelPassword"
					value="" />
                <form:errors path="password" cssClass="error-text" />

                <c:if test="${not empty errors}">
                    <div class="error">
                        <c:forEach var="error" items="${errors}">
                            <p>${error}</p>
                        </c:forEach>
                    </div>
                </c:if>

				<input type="submit" value="Login" />
			</form:form>
			<div class="links-below-form">
                <a href='<c:url value="/password/forgot"/>' id="link-new-user">Esqueceu sua senha?</a>
                <a href='<c:url value="/auth/register"/>' id="link-new-user">Não tem uma conta? Cadastre-se</a>
            </div>
		</main>
		<footer>
			<c:import url="/WEB-INF/views/includes/footer.jsp" />
		</footer>
	</body>
</html>