<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/includes.jsp" %>
<html>
	<head>
	    <meta charset="UTF-8">
		<title>Atualizar Perfil</title>
		<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/users/profile.css">
		<script src="${pageContext.request.contextPath}/javascript/jquery-3.7.1.min.js" defer></script>
		<script src="${pageContext.request.contextPath}/javascript/jquery.maskMoney.min.js" defer></script>
		<script src="${pageContext.request.contextPath}/javascript/jquery.inputmask.min.js" defer></script>
		<script src="${pageContext.request.contextPath}/javascript/jquery.validate.min.js" defer></script>
		<script src="${pageContext.request.contextPath}/javascript/masks.js" defer></script>
	</head>
	<body>
		<header>
			<c:import url="/WEB-INF/views/includes/header.jsp" />
			<h1>Atualizar Perfil</h1>
		</header>
		<main>
			<form action='<c:url value="/users/profile"/>' method="POST" id="formProfile">
                    <input type="hidden" name="_method" value="PUT"/>

                <fieldset>
                    <legend>Dados Pessoais</legend>

                    <triadTag:textField
                        nameField="name"
                        label="Nome:"
                        id="labelName"
                        value="${updateUserDTO.name()}" />

                    <triadTag:textField
                        nameField="email"
                        label="Email:"
                        id="labelEmal"
                        value="${updateUserDTO.email()}" />
                </fieldset>

                <fieldset>
                    <legend>Alterar Senha</legend>

                    <p class="note">Preencha os três campos abaixo apenas se desejar alterar sua senha.</p>

                    <triadTag:password
                        passwordField="currentPassword"
                        label="Senha Atual:"
                        id="labelCurrentPassword"
                        value="" />

                    <triadTag:password
                        passwordField="newPassword"
                        label="Novo Password:"
                        id="labelnewPassword"
                        value="" />

                    <triadTag:password
                        passwordField="confirmPassword"
                        label="Confirmar Password:"
                        id="labelConfirmPassword"
                        value="" />
                </fieldset>

                    <c:if test="${not empty errors}">
                        <div class="error">
                            <c:forEach var="error" items="${errors}">
                                <p>${error}</p>
                            </c:forEach>
                        </div>
                    </c:if>

                    <c:if test="${not empty successMessage}">
                        <div class="success">
                            <p>${successMessage}</p>
                        </div>
                    </c:if>

				<input type="submit" value="Atualizar" />
			</form>
			<a href='<c:url value="/home"/>' class="menu-link">Retornar ao Menu</a>
		</main>
		<footer>
			<c:import url="/WEB-INF/views/includes/footer.jsp" />
		</footer>
	</body>
</html>