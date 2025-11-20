package org.util.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.util.records.EmailRequest;
import org.util.services.SesEmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final SesEmailService sesEmailService;

    public EmailController(SesEmailService sesEmailService) {
        this.sesEmailService = sesEmailService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailRequest request) {
        try {
            sesEmailService.sendEmail(request);
            return ResponseEntity.ok().body("{\"status\":\"ok\"}");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"error\":\"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            // log error
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"Failed to send email\"}");
        }
    }
}
