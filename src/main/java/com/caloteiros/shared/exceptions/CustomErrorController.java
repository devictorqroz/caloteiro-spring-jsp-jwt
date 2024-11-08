package com.caloteiros.shared.exceptions;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        ModelAndView mv = new ModelAndView();

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.FORBIDDEN.value()) {
                mv.setViewName("error/403");
                mv.addObject("error", "Acesso negado");
                mv.addObject("message", "Você não tem permissão para acessar este recurso.");
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                mv.setViewName("error/http-error");
                mv.addObject("error", "404");
                mv.addObject("message", "Página não encontrada.");
            } else {
                mv.setViewName("error/500");
                mv.addObject("error", "500");
                mv.addObject("message", "Erro interno do servidor.");
            }
        }
        return mv;
    }
}