<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/includes/includes.jsp" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Dashboard - Caloteiros</title>
        <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/home.css">
    </head>
    <body>
        <header>
            <c:import url="${pageContext.request.contextPath}/WEB-INF/views/includes/header.jsp" />
            <h1>Dashboard - Caloteiros</h1>
        </header>

        <main>
            <section class="welcome-section">
                <p>Bem-vindo(a), ${sessionScope.loggedUserName}, ao sistema Caloteiros!</p>
                <p>Aqui você pode gerenciar e monitorar as ações dos usuários e caloteiros.</p>
            </section>

            <nav class="action-menu">
                <ul>
                    <li><a href='<c:url value="/caloteiros/new" />'>Adicionar Caloteiro</a></li>
                    <li><a href='<c:url value="/caloteiros" />'>Listar Caloteiros</a></li>
                    <li><a href='<c:url value="/users/profile" />'>Atualizar Dados do Usuário</a></li>
                    <li><a href='<c:url value="/auth/logout" />'>Logout</a></li>
                    <li><a href='<c:url value="/users/delete" />'>Excluir conta</a></li>
                </ul>
            </nav>
        </main>

        <footer>
            <c:import url="${pageContext.request.contextPath}/WEB-INF/views/includes/footer.jsp" />
        </footer>
    </body>
</html>
