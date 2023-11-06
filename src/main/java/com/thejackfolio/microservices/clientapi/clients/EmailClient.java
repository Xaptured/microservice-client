package com.thejackfolio.microservices.clientapi.clients;

import com.thejackfolio.microservices.clientapi.models.EmailDetails;
import com.thejackfolio.microservices.clientapi.models.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ORCHESTRATE-SERVICE")
public interface EmailClient {

    @PostMapping("/email/send-acknowledgement-email")
    public ResponseEntity<EmailResponse> sendAcknowledgementEmailToClient(@RequestBody EmailDetails details);

    @PostMapping("/email/send-robo-email")
    public ResponseEntity<EmailResponse> sendRoboEmailToMe(@RequestBody EmailDetails details);

    @PostMapping("/email/send-response-email")
    public ResponseEntity<EmailResponse> sendResponseEmailToClient(@RequestBody EmailDetails details);
}
