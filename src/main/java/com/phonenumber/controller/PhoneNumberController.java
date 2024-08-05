package com.phonenumber.controller;

import com.phonenumber.model.CandidateModel;
import com.phonenumber.model.PhoneNumberModel;
import com.phonenumber.model.ResponseModel;
import com.phonenumber.service.PhoneNumberService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/phone-numbers")
public class PhoneNumberController {

    private final PhoneNumberService phoneNumberService;

    private Integer defaultLimit;

    public PhoneNumberController(PhoneNumberService phoneNumberService, @Value("${phoneNumberDefaultLimit}") Integer defaultLimit) {
        this.phoneNumberService = phoneNumberService;
        this.defaultLimit = defaultLimit;
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

    @PostMapping
    public ResponseEntity<PhoneNumberModel> createPhoneNumber(@RequestBody PhoneNumberModel newPhoneNumber) {
        return new ResponseEntity<>(phoneNumberService.createPhoneNumber(newPhoneNumber), CREATED);
    }

    @PostMapping("/hold")
    public ResponseEntity<Void> holdPhoneNumber(@RequestBody Map<String, String> request) {
        phoneNumberService.updateStatusHold(request.get("phoneNumberId"));
        return new ResponseEntity<>(OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhoneNumber(@PathVariable String id) {
        phoneNumberService.deletePhoneNumber(id);
        return new ResponseEntity<>(OK);
    }

    @PutMapping("/put")
    public ResponseEntity<Void> updateStatusAvailable() {
        phoneNumberService.updateStatusAvailable();
        return new ResponseEntity<>(OK);
    }


    @PostMapping("/confirm")
    public ResponseEntity<Void> confirmPhoneNumber(@RequestParam Map<String, String> request) {

        String phoneNumberId = request.get("phoneNumberId");
        String firstName = request.get("firstName");
        String lastName = request.get("lastName");
        String address = request.get("address");
        String contactPhoneNumber = request.get("contactPhoneNumber");


        CandidateModel candidate = new CandidateModel();
        candidate.setPhoneNumberId(phoneNumberId);
        candidate.setFirstName(firstName);
        candidate.setLastName(lastName);
        candidate.setAddress(address);
        candidate.setContactPhoneNumber(contactPhoneNumber);



        // Update phone number status to SOLD
        phoneNumberService.updateStatusSold(phoneNumberId);

        return ResponseEntity.ok().build();
    }
}


