<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/includes.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
	<head>
	    <meta charset="UTF-8">
		<title>Registrar</title>
		<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/auth/register.css">
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
			<form:form modelAttribute="registerRequest" action="/auth/register" method="POST" id="formRegister">

				<app:textField
					nameField="name"
					label="Nome:"
					id="labelName" />
                <form:errors path="name" cssClass="error-text" />

                <app:textField
					nameField="email"
					label="Email:"
					id="labelEmal" />
                <form:errors path="email" cssClass="error-text" />

				<app:password
					passwordField="password"
					label="Password:"
					id="labelPassword" />
                <form:errors path="password" cssClass="error-text" />

                <app:password
                    passwordField="confirmPassword"
                    label="Confirmar Senha:"
                    id="labelConfirmPassword" />
                <form:errors path="confirmPassword" cssClass="error-text" />

				<input type="submit" value="Registrar" />
			</form:form>
			<a href='<c:url value="/auth/login"/>' class="menu-link">Já tem uma conta? Faça o login</a>
		</main>
		<footer>
			<c:import url="/WEB-INF/views/includes/footer.jsp" />
		</footer>
	</body>
</html>