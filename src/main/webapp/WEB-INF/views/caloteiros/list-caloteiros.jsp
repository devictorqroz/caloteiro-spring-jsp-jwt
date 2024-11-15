<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/includes.jsp" %>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Lista de Caloteiros</title>
		<link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/caloteiros/list-caloteiros.css">
		<script src="${pageContext.request.contextPath}/javascript/jquery-3.7.1.min.js" defer></script>
    	<script src="${pageContext.request.contextPath}/javascript/confirmation.js" defer></script>
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
			<p>
				Lista de Caloteiros: 
			</p>
            <div class="sorting-container">
                <form method="get" action="">
                    <label for="sortField">Ordenar por:</label>
                    <select name="sortField" id="sortField" onchange="this.form.submit()">
                        <option value="name" ${param.sortField == 'name' ? 'selected' : ''}>Nome</option>
                        <option value="debt" ${param.sortField == 'debt' ? 'selected' : ''}>Dívida</option>
                        <option value="debtDate" ${param.sortField == 'debtDate' ? 'selected' : ''}>Data</option>
                    </select>

                    <select name="sortOrder" id="sortOrder" onchange="this.form.submit()">
                        <option value="asc" ${param.sortOrder == 'asc' ? 'selected' : ''}>Crescente</option>
                        <option value="desc" ${param.sortOrder == 'desc' ? 'selected' : ''}>Decrescente</option>
                    </select>
                </form>
            </div>
			<table>
				<tr><th>ID</th><th>Nome</th><th>Email</th><th>Devendo</th><th>Data Dívida</th><th>Editar</th><th>Excluir</th></tr>
				<c:forEach var="caloteiro" items="${caloteirosPage.caloteiros()}" varStatus="id">
					<tr>
						<td>${caloteiro.id()}</td>
						<c:choose>
							<c:when test="${not empty caloteiro.name()}">
								<td>${caloteiro.name()}</td>
							</c:when>
							<c:otherwise>
								<td>Nome não preenchido.</td>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${not empty caloteiro.email()}">
								<td><a href="#">${caloteiro.email()}</a></td>
							</c:when>
							<c:otherwise>
								<td>E-mail não preenchido.</td>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${not empty caloteiro.debt()}">
								<td>${caloteiro.debt()}</td>
							</c:when>
							<c:otherwise>
								<td>Devendo não preenchido.</td>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${not empty caloteiro.debtDate()}">
								<td>
									 ${fn:formatLocalDate(caloteiro.debtDate(), 'dd/MM/yyyy')}
								</td>
							</c:when>
							<c:otherwise>
								<td>Data dívida não preenchida.</td>
							</c:otherwise>
						</c:choose>
						<td>
							<form action="/caloteiros/${caloteiro.id()}/edit" method="GET">
								<input type="submit" id="updateButton" value="Editar" />
							</form>
						</td>
						<td>
							<form action="/caloteiros/${caloteiro.id()}" method="POST">
								<input type="hidden" name="_method" value="DELETE" />
								<input type="submit" id="deleteButton" value="Excluir" />
							</form>
						</td>
					</tr>		
				</c:forEach>
			</table>
			<div class="pagination">
                <c:if test="${!caloteirosPage.isFirst()}">
                    <a href="?pageNumber=0&pageSize=${caloteirosPage.pageSize()}">Primeira</a>
                </c:if>

                <c:if test="${caloteirosPage.hasPrevious()}">
                    <a href="?pageNumber=${caloteirosPage.pageNumber() - 1}&pageSize=${caloteirosPage.pageSize()}">Anterior</a>
                </c:if>

                <span>Página ${caloteirosPage.pageNumber() + 1} de ${caloteirosPage.totalPages()}</span>

                <c:if test="${caloteirosPage.hasNext()}">
                    <a href="?pageNumber=${caloteirosPage.pageNumber() + 1}&pageSize=${caloteirosPage.pageSize()}">Próxima</a>
                </c:if>

                <c:if test="${!caloteirosPage.isLast()}">
                    <a href="?pageNumber=${caloteirosPage.totalPages() - 1}&pageSize=${caloteirosPage.pageSize()}">Última</a>
                </c:if>
            </div>
			<a href="/home" class="menu-link">Retornar ao Menu</a>
		</main>
		<footer>
			<c:import url="${pageContext.request.contextPath}/WEB-INF/views/includes/footer.jsp" />
		</footer>
	</body>
</html>