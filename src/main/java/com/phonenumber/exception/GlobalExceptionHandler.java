package com.phonenumber.exception;

import com.phonenumber.constant.PhoneNumberResultConstants;
import com.phonenumber.model.ResponseModel;
import com.phonenumber.model.ResultModel;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PhoneNumberNotFoundException.class)
    public ResponseEntity<ResponseModel> handlePhoneNumberNotFoundException() {
        ResultModel result = new ResultModel(PhoneNumberResultConstants.FAILED_RESULT.getResult(), String.valueOf(HttpStatus.NOT_FOUND.value()), PhoneNumberResultConstants.NOT_FOUND_MESSAGE.getResult());
        return new ResponseEntity<>(new ResponseModel(result), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel> handleServerException() {
        ResultModel result = new ResultModel(PhoneNumberResultConstants.FAILED_RESULT.getResult(),  String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), PhoneNumberResultConstants.INTERNAL_SERVER_MESSAGE.getResult());
        return new ResponseEntity<>(new ResponseModel(result), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<ResponseModel> handleForbiddenException() {
        ResultModel result = new ResultModel(PhoneNumberResultConstants.FAILED_RESULT.getResult(),  String.valueOf(HttpStatus.FORBIDDEN.value()), PhoneNumberResultConstants.FORBIDDEN_MESSAGE.getResult());
        return new ResponseEntity<>(new ResponseModel(result), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseModel> handleAuthenticationException() {
        ResultModel result = new ResultModel(PhoneNumberResultConstants.FAILED_RESULT.getResult(),  String.valueOf(HttpStatus.UNAUTHORIZED.value()), PhoneNumberResultConstants.AUTHENTICATION_FAILED_MESSAGE.getResult());
        return new ResponseEntity<>(new ResponseModel(result), HttpStatus.UNAUTHORIZED);
    }
}
