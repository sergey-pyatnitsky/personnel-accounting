package com.personnel_accounting.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

@RestController
public class messageRESTController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/api/messages/{lang}/{property}")
    public ResponseEntity<?> getMessages(@PathVariable String lang, @PathVariable String property) {
        return new ResponseEntity<>((messageSource.getMessage(property, null, Locale.forLanguageTag(lang)))
                .getBytes(StandardCharsets.UTF_8), HttpStatus.OK);
    }
}
