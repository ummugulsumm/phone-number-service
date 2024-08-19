package com.phonenumber.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "chatbot", url = "http://localhost:8081")
public interface AiService {

    @PostMapping("/chat")
    String chatGemini(@RequestBody String prompt);


}
