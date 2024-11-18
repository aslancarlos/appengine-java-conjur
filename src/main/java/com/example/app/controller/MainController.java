package com.example.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "My App Engine Java Application");
        model.addAttribute("message", "Hello World!");
        return "index"; // Retorne apenas o nome do template, sem o caminho ou extensão
    }
}
