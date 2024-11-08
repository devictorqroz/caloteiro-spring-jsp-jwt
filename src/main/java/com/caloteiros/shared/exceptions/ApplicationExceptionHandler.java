package com.caloteiros.shared.exceptions;

import com.caloteiros.caloteiro.domain.exceptions.CaloteiroException;
import com.caloteiros.user.domain.exceptions.UserException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleGeneralException() {
        return createErrorModelAndView("error/default-error", "Erro",
                "Ocorreu um erro no servidor.");
    }

    @ExceptionHandler(DataAccessException.class)
    public ModelAndView handleDatabaseException() {
        return createErrorModelAndView("error/sql-error", "SQL Error",
                "Erro com a operação no banco de dados, verifique a transação.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleBadRequest(MethodArgumentNotValidException ex) {
        StringBuilder message = new StringBuilder("Os dados fornecidos são inválidos: ");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                message.append(error.getField()).append(" ").append(error.getDefaultMessage()).append(". ")
        );
        return createErrorModelAndView("error/http-error", "400 - Requisição inválida", message.toString());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView handleNotFound(NoHandlerFoundException ex) {
        ModelAndView mv = createErrorModelAndView("error/http-error", "404 - Recurso não encontrado",
                "A página que você está procurando não existe.");
        mv.setStatus(HttpStatus.NOT_FOUND);
        return mv;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        ModelAndView mv = createErrorModelAndView("error/http-error", "405 - Método HTTP não suportado", "O método " + ex.getMethod() + " não é permitido para esta requisição.");
        mv.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
        return mv;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleInternalServerError() {
        ModelAndView mv = createErrorModelAndView("error/http-error", "500 - Erro interno do servidor", "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde.");
        mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return mv;
    }

    @ExceptionHandler(CaloteiroException.class)
    public ModelAndView handleCaloteiroException(CaloteiroException ex) {
        return createErrorModelAndView("error/caloteiro-error",
                "Erro na operação com Caloteiro", ex.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ModelAndView handleUserException(UserException ex) {
        return createErrorModelAndView("error/user-error",
                "Erro na operação com Usuário", ex.getMessage());
    }

    private ModelAndView createErrorModelAndView(String viewName, String error, String message) {
        ModelAndView mv = new ModelAndView(viewName);
        mv.addObject("error", error);
        mv.addObject("message", message);
        return mv;
    }
}
