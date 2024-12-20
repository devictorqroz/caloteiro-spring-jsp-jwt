<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/includes.jsp" %>
<html>
	<head>
		<title>Editar caloteiro</title>
		<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/caloteiros/update-caloteiro.css">
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
			<p>
				Usuário Logado:
			</p>
			<form action="/caloteiros/${updateCaloteiro.id()}" method="POST" id="formUpdateCaloteiro">
				<input type="hidden" name="_method" value="PUT"/>

				<triadTag:textField 
					nameField="name" 
					label="Nome:" 
					id="labelName"
					value="${updateCaloteiro.name()}" />
				<triadTag:textField 
					nameField="email" 
					label="Email:" 
					id="labelEmail" 
					value="${updateCaloteiro.email()}" />
				<triadTag:textField 
					nameField="debt" 
					label="Devendo:" 
					id="labelDebt" 
					value="${updateCaloteiro.debt()}" />
				<triadTag:textField 
					nameField="debtDate" 
					label="Data da Dívida:"
					id="labelDate" 
					value="${fn:formatLocalDate(updateCaloteiro.debtDate(), 'dd/MM/yyyy')}" />

				<input type="submit" value="Atualizar"/>
			</form>
			<a href="/home" class="menu-link">Retornar ao Menu</a>
		</main>
		<footer>
			<c:import url="${pageContext.request.contextPath}/WEB-INF/views/includes/footer.jsp" />
		</footer>
	</body>
</html>