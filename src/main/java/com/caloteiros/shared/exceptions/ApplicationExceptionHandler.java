package com.caloteiros.shared.exceptions;

import com.caloteiros.caloteiro.domain.exceptions.CaloteiroException;
import com.caloteiros.user.domain.exceptions.PasswordException;
import com.caloteiros.user.domain.exceptions.PasswordResetTokenException;
import com.caloteiros.user.domain.exceptions.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ApplicationExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler({
            CaloteiroException.class,
            UserException.class,
            PasswordException.class,
            PasswordResetTokenException.class,
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBusinessException(Exception ex, Model model) {
        logger.warn("Uma exceção de negócio foi tratada: {}", ex.getMessage());
        model.addAttribute("error", "Requisição inválida");
        model.addAttribute("message", ex.getMessage());
        return "error/default-error";
    }

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleDatabaseException(DataAccessException ex, Model model) {
        logger.error("Exceção de acesso a dados ocorreu:", ex);
        model.addAttribute("error", "Erro de Banco de Dados");
        model.addAttribute("message", "Ocorreu um erro ao acessar o banco de dados. Por favor, tente novamente mais tarde.");
        return "error/sql-error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleInternalServerError(Exception ex, Model model) {
        logger.error("Erro interno inesperado foi capturado:", ex);
        model.addAttribute("error", "500 - Erro interno do servidor");
        model.addAttribute("message", "Ocorreu um erro inesperado. Nossa equipe foi notificada");
        return "error/http-error";
    }
}
