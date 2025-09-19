package com.caloteiros.shared.exceptions;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(CustomErrorController.class);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String viewName = "error/default-error";
        String errorTitle = "Erro Inesperado";
        String errorMessage = "Ocorreu um erro. Por favor, tente novamente";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                viewName = "error/403";
                errorTitle = "403 - Acesso negado";
                errorMessage = "Você não tem permissão para acessar este recurso.";
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                viewName = "error/http-error";
                errorTitle = "404 - Página Não Encontrada";
                errorMessage = "O recurso solicitado não foi encontrado." + request.getRequestURI();
            } else {
                logger.error("Erro não tratado. Status: {}. URI: {}", statusCode, request.getRequestURI());
                viewName = "error/http-error";
                errorTitle = statusCode + " - Erro Interno";
                errorMessage = "Ocorreu um erro inesperado do servidor.";
            }
        } else {
            logger.error("Erro não tratado sem código de status. URI: {}", request.getRequestURI());
        }
        model.addAttribute("error", errorTitle);
        model.addAttribute("message", errorMessage);

        return viewName;
    }
}