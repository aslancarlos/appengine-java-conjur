package com.example.app.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object statusCode = request.getAttribute("jakarta.servlet.error.status_code");

        if (statusCode != null) {
            int status = Integer.parseInt(statusCode.toString());

            switch (status) {
                case 404:
                    model.addAttribute("errorMessage", "Oops! The page you are looking for does not exist.");
                    break;
                case 500:
                    model.addAttribute("errorMessage", "Sorry! Something went wrong on our end.");
                    break;
                default:
                    model.addAttribute("errorMessage", "Unexpected error occurred. Please try again later.");
                    break;
            }
        } else {
            model.addAttribute("errorMessage", "Unexpected error occurred.");
        }

        return "error"; // Deve ser o nome do template sem caminho ou extensão
    }
}
