package com.example.honeybeehaven.controllers;

import com.example.honeybeehaven.classes.ContactCard;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomError implements ErrorController {

    @RequestMapping("/error")
    public String error(Model model) {

        ContactCard contactCard = new ContactCard();
        model.addAttribute(contactCard);
        // Your custom error handling logic here
        return "error"; // Thymeleaf template name for your custom error page
    }

    public String getErrorPath() {
        return "/error";
    }
}
