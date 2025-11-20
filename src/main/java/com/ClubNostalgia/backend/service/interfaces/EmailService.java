package com.ClubNostalgia.backend.service.interfaces;

import java.io.IOException;

public interface EmailService {
    
    void sendContactNotification(String nombre, String emailUsuario, String proyecto) throws IOException;
}