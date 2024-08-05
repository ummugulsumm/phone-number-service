package com.phonenumber.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class RedirectController {

    @GetMapping("/confirm")
    public String showConfirmPage(@RequestParam(name = "phoneNumberId", required = false) String phoneNumberId) {
        return "confirm";
    }
}
