package com.fastturtle.userauthenticationservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SuccessController {

    @GetMapping("/success")
    public String successPage() {
        return "redirect:/success.html"; // Return the name of the success view (e.g., success.html)
    }
}
