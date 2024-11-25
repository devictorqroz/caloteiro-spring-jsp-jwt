<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/includes.jsp" %>
<html>
	<head>
	    <meta charset="UTF-8">
		<title>Deletar Usuário</title>
		<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/users/delete.css">
		<script src="${pageContext.request.contextPath}/javascript/jquery-3.7.1.min.js" defer></script>
		<script src="${pageContext.request.contextPath}/javascript/jquery.maskMoney.min.js" defer></script>
		<script src="${pageContext.request.contextPath}/javascript/jquery.inputmask.min.js" defer></script>
        <script src="${pageContext.request.contextPath}/javascript/jquery.validate.min.js" defer></script>
        <script src="${pageContext.request.contextPath}/javascript/masks.js" defer></script>
		<script src="${pageContext.request.contextPath}/javascript/confirmation.js" defer></script>
	</head>
	<body>
		<header>
			<c:import url="${pageContext.request.contextPath}/WEB-INF/views/includes/header.jsp" />
			<h1>Caloteiros</h1>
		</header>
		<main>
		    <p>
		        Deletar Conta: ${sessionScope.loggedUserName}
		    </p>
		    <p>
		        Atenção, essa ação é irreversívell!
		    </p>
			<form action="/users/delete" method="POST" id="deleteUserForm">
                <input type="hidden" name="userId" value="${userId}" />

				<triadTag:password
					passwordField="password"
					label="Confirme a Senha:"
					id="labelPassword"
					value="" />

                <c:if test="${not empty errors}">
                    <div class="error">
                        <c:forEach var="error" items="${errors}">
                            <p>${error}</p>
                        </c:forEach>
                    </div>
                </c:if>

				<input type="submit" id="deleteUserButton" value="Excluir" />
			</form>
			<a href="/home" class="menu-link">Cancelar</a>
		</main>
		<footer>
			<c:import url="${pageContext.request.contextPath}/WEB-INF/views/includes/footer.jsp" />
		</footer>
	</body>
</html>