package com.thejackfolio.microservices.clientapi.controllers;

import com.thejackfolio.microservices.clientapi.clients.TheJackFolioDBClient;
import com.thejackfolio.microservices.clientapi.exceptions.EmailException;
import com.thejackfolio.microservices.clientapi.models.ClientComments;
import com.thejackfolio.microservices.clientapi.models.EmailDetails;
import com.thejackfolio.microservices.clientapi.services.EmailService;
import com.thejackfolio.microservices.clientapi.utilities.StringConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emails")
@Tag(name = "Email", description = "Email management APIs")
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private TheJackFolioDBClient dbClient;

    @Operation(
            summary = "Send email to client",
            description = "Send email to client and gives the response with a message which defines whether the request is successful or not."
    )
    @PostMapping("/sendEmail/{commentId}")
    public ResponseEntity<String> sendEmailToClient(@RequestBody EmailDetails emailDetails, @PathVariable Integer commentId){
        try{
            ResponseEntity<ClientComments> comment = dbClient.getCommentById(commentId);
            ClientComments responseBody = comment.getBody();
            if(responseBody.getMessage().equals(StringConstants.REQUEST_PROCESSED)){
                emailService.sendEmailToClient(emailDetails);
                responseBody.setReplied(true);
                dbClient.updateComments(responseBody, commentId);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody.getMessage());
            }
        } catch (EmailException exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StringConstants.ERROR_OCCURRED_SENDING_EMAIL);
        }
        return ResponseEntity.status(HttpStatus.OK).body(StringConstants.MAIL_SENT_SUCCESSFULLY);
    }
}
