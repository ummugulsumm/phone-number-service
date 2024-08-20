package com.phonenumber.controller;

import com.phonenumber.dto.CandidateDto;
import com.phonenumber.dto.CandidateRequestDto;
import com.phonenumber.dto.PhoneNumberDto;
import com.phonenumber.model.ResponseModel;
import com.phonenumber.service.CandidateService;
import com.phonenumber.service.PhoneNumberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/phone-numbers")
public class PhoneNumberController {

    private final PhoneNumberService phoneNumberService;

    private final CandidateService candidateService;

    private Integer defaultLimit;

    public PhoneNumberController(PhoneNumberService phoneNumberService, @Value("${phoneNumberDefaultLimit}") Integer defaultLimit, CandidateService  candidateService) {
        this.phoneNumberService = phoneNumberService;
        this.defaultLimit = defaultLimit;
        this.candidateService = candidateService;
    }

    @GetMapping()
    public ResponseModel getAvailablePhoneNumbers() {
        return phoneNumberService.getAvailablePhoneNumbers();
    }

    @GetMapping("/get")
    public ResponseModel getAvailablePhoneNumbersByCount(@RequestParam(name = "count", required = false) Integer count) {
        Integer limit = (count != null) ? count : defaultLimit;
        return phoneNumberService.getAvailablePhoneNumbersByCount(limit);
    }

    @GetMapping("/digit")
    public ResponseModel getAvailablePhoneNumbersByDigit(@RequestParam String digit) {
        return phoneNumberService.getAvailablePhoneNumbersByDigit(digit);
    }

    @PostMapping("/ai-help")
    public String getAiHelp(@RequestBody Map<String, List<Object>> requestBody) {
        return phoneNumberService.getAiHelp(requestBody.get("phoneNumbers"));
    }

    @PatchMapping("/confirm/{phoneNumberId}")
    public ResponseEntity<Void> addContactPhoneNumber(@PathVariable String phoneNumberId, @RequestBody Map<String, String> requestBody) {
        return  phoneNumberService.addContactPhoneNumber(phoneNumberId, requestBody.get("contactPhoneNumber"));
    }

    @PostMapping("/candidate/{phoneNumberId}")
    public ResponseEntity<CandidateDto> createCandidate(@PathVariable String phoneNumberId, @RequestBody CandidateRequestDto request) {
        return new ResponseEntity<>(candidateService.createCandidate(phoneNumberId, request), CREATED);
    }

    @PatchMapping("/delivery/{candidateId}")
    public ResponseEntity<CandidateDto> addDelivery(@PathVariable String candidateId, @RequestBody Map<String, String> request) {
        String deliveryType = request.get("deliveryType");
        String address = request.get("address");
        return new ResponseEntity<>(candidateService.addDelivery(candidateId, deliveryType, address), CREATED);
    }

    @PostMapping
    public ResponseEntity<PhoneNumberDto> createPhoneNumber(@RequestBody PhoneNumberDto newPhoneNumber) {
        return new ResponseEntity<>(phoneNumberService.createPhoneNumber(newPhoneNumber), CREATED);
    }

    @PutMapping("/put")
    public ResponseEntity<Void> updateStatusAvailable() {
        phoneNumberService.updateStatusAvailable();
        return new ResponseEntity<>(OK);
    }

}


