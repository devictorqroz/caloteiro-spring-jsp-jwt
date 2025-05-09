<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/includes.jsp" %>
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
			<c:import url="${pageContext.request.contextPath}/WEB-INF/views/includes/header.jsp" />
			<h1>Caloteiros</h1>
		</header>
		<main>
			<form action="${pageContext.request.contextPath}/auth/login" method="POST" id="formLogin">

				<triadTag:textField
					nameField="email"
					label="Email:"
					id="labelEmail"
					value="" />
				<triadTag:password
					passwordField="password"
					label="Password:"
					id="labelPassword"
					value="" />

                <c:if test="${not empty errors}">
                    <div class="error">
                        <c:forEach var="error" items="${errors}">
                            <p>${error}</p>
                        </c:forEach>
                    </div>
                </c:if>

				<input type="submit" value="Login" />
			</form>
			<div class="links-below-form">
                <a href="/password/forgot" id="link-new-user">Esqueceu sua senha?</a>
                <a href="/auth/register" id="link-new-user">Não tem uma conta? Cadastre-se</a>
            </div>
		</main>
		<footer>
			<c:import url="${pageContext.request.contextPath}/WEB-INF/views/includes/footer.jsp" />
		</footer>
	</body>
</html>