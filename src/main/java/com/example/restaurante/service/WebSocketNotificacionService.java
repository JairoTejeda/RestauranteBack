package com.example.restaurante.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class WebSocketNotificacionService {
	
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
	

	
	
    public void enviarCambioEstadoMesa(Long idmesa, String estadomesa) {

        Map<String, Object> mensaje = new HashMap<>();
        mensaje.put("idmesa", idmesa);
        mensaje.put("estadomesa", estadomesa);

        messagingTemplate.convertAndSend("/topic/estado-mesa", mensaje);
    }

}
