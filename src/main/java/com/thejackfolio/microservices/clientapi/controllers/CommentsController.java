package com.thejackfolio.microservices.clientapi.controllers;

import com.thejackfolio.microservices.clientapi.clients.EmailClient;
import com.thejackfolio.microservices.clientapi.exceptions.DataBaseOperationException;
import com.thejackfolio.microservices.clientapi.exceptions.EmailException;
import com.thejackfolio.microservices.clientapi.exceptions.MapperException;
import com.thejackfolio.microservices.clientapi.exceptions.ValidationException;
import com.thejackfolio.microservices.clientapi.models.ClientComments;
import com.thejackfolio.microservices.clientapi.models.EmailDetails;
import com.thejackfolio.microservices.clientapi.models.EmailResponse;
import com.thejackfolio.microservices.clientapi.services.CommentsService;
import com.thejackfolio.microservices.clientapi.services.EmailService;
import com.thejackfolio.microservices.clientapi.utilities.StringConstants;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Comment", description = "Comment management APIs")
@RestController
@RequestMapping("/comments")
public class CommentsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentsController.class);
    private boolean isRetryEnabled = false;
    @Autowired
    private CommentsService service;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailClient client;

    @Operation(
            summary = "Save comments",
            description = "Save comments and gives the same comments response with a message which defines whether the request is successful or not."
    )
    @PostMapping("/save-comments")
    @Retry(name = "save-comments-db-retry", fallbackMethod = "saveCommentsDBRetry")
    public ResponseEntity<ClientComments> saveComment(@RequestBody ClientComments comments){
        ClientComments response = null;
        try{
            if(isRetryEnabled){
                LOGGER.info(StringConstants.RETRY_MESSAGE);
            }
            if(!isRetryEnabled){
                isRetryEnabled = true;
            }
            response = service.saveComment(comments);
            if(response.getMessage().equals(StringConstants.REQUEST_PROCESSED)){
                EmailDetails details = new EmailDetails();
                details.setRecipient(response.getEmail());
                ResponseEntity<EmailResponse> emailResponseResponseEntity = client.sendAcknowledgementEmailToClient(details);
                EmailResponse emailResponse = emailResponseResponseEntity.getBody();
                if(!emailResponse.getMessage().equals(StringConstants.MAIL_SENT_SUCCESSFULLY)) {
                    throw new EmailException(emailResponse.getMessage());
                }
            }
        } catch (ValidationException | MapperException | DataBaseOperationException exception){
            if(comments == null){
                comments = new ClientComments();
            }
            comments.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(comments);
        } catch (EmailException exception) {
            LOGGER.error("Error occurred while sending acknowledgement email to {}", comments.getEmail());
            response.setMessage(StringConstants.ERROR_SENDING_EMAIL);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        isRetryEnabled = false;
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ClientComments> saveCommentsDBRetry(ClientComments comments, Exception exception){
        isRetryEnabled = false;
        LOGGER.info(StringConstants.FALLBACK_MESSAGE, exception);
        ClientComments commentResponse = new ClientComments();
        commentResponse.setMessage(StringConstants.FALLBACK_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(commentResponse);
    }

    @Operation(
            summary = "Get comments",
            description = "It gives the list of comments as response which are not replied yet with a message which defines whether the request is successful or not."
    )
    @GetMapping("/get-comments")
    @Retry(name = "get-comments-db-retry", fallbackMethod = "getCommentsDBRetry")
    public ResponseEntity<List<ClientComments>> getComments(){
        List<ClientComments> comments = null;
        try{
            if(isRetryEnabled){
                LOGGER.info(StringConstants.RETRY_MESSAGE);
            }
            if(!isRetryEnabled){
                isRetryEnabled = true;
            }
            comments = service.getComments();
        } catch (ValidationException | MapperException | DataBaseOperationException exception){
            comments = new ArrayList<>();
            ClientComments comment = new ClientComments();
            comment.setMessage(exception.getMessage());
            comments.add(comment);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(comments);
        }
        isRetryEnabled = false;
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    public ResponseEntity<List<ClientComments>> getCommentsDBRetry(Exception exception){
        isRetryEnabled = false;
        LOGGER.info(StringConstants.FALLBACK_MESSAGE, exception);
        ClientComments commentResponse = new ClientComments();
        commentResponse.setMessage(StringConstants.FALLBACK_MESSAGE);
        List<ClientComments> comments = new ArrayList<>();
        comments.add(commentResponse);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }
}
