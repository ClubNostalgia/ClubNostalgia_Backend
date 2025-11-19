package com.ClubNostalgia.backend.controller;

import com.ClubNostalgia.backend.dto.request.ContactRequest;
import com.ClubNostalgia.backend.dto.response.MessageResponse;
import com.ClubNostalgia.backend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<MessageResponse> sendContactMessage(@Valid @RequestBody ContactRequest request) {
        try {
            emailService.sendContactNotification(
                request.nombre(),
                request.email(),
                request.proyecto()
            );
            
            return ResponseEntity.ok(new MessageResponse("Mensaje enviado correctamente"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new MessageResponse("Error al enviar el mensaje: " + e.getMessage()));
        }
    }
}