package com.phonenumber.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class RedirectController {

    @GetMapping("phone-numbers/confirm/{phoneNumberId}")
    public String showConfirmPage() {
        return "confirm";
    }
    @GetMapping("phone-numbers/candidate/{phoneNumberId}")
    public String showCandidatePage() {
        return "candidate";
    }
    @GetMapping("phone-numbers/delivery/{phoneNumberId}")
    public String showDeliveryPage() {
        return "delivery";
    }

    @GetMapping("phone-numbers/success")
    public String showSuccessPage() {
        return "success";
    }


}
