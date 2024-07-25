package com.thejackfolio.microservices.clientapi.controllers;

import com.thejackfolio.microservices.clientapi.clients.EmailClient;
import com.thejackfolio.microservices.clientapi.exceptions.DataBaseOperationException;
import com.thejackfolio.microservices.clientapi.exceptions.EmailException;
import com.thejackfolio.microservices.clientapi.exceptions.MapperException;
import com.thejackfolio.microservices.clientapi.exceptions.ValidationException;
import com.thejackfolio.microservices.clientapi.models.ClientComments;
import com.thejackfolio.microservices.clientapi.models.EmailDetails;
import com.thejackfolio.microservices.clientapi.models.EmailResponse;
import com.thejackfolio.microservices.clientapi.models.Joiner;
import com.thejackfolio.microservices.clientapi.services.JoinerService;
import com.thejackfolio.microservices.clientapi.utilities.StringConstants;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Joiner", description = "Joiner management APIs")
@RestController
@RequestMapping("/joiners")
public class JoinerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinerController.class);

    private boolean isRetryEnabled = false;

    @Autowired
    private JoinerService service;
    @Autowired
    private EmailClient client;

    @Operation(
            summary = "Save joiner",
            description = "Save joiner and gives the same joiner response with a message which defines whether the request is successful or not."
    )
    @PostMapping("/save-joiner")
    @Retry(name = "save-joiner-db-retry", fallbackMethod = "saveJoinerDBRetry")
    public ResponseEntity<Joiner> saveJoiner(@RequestBody Joiner joiner){
        Joiner response = null;
        try{
            if(isRetryEnabled){
                LOGGER.info(StringConstants.RETRY_MESSAGE);
            }
            if(!isRetryEnabled){
                isRetryEnabled = true;
            }
            response = service.saveJoiner(joiner);
            if(response.getMessage().equals(StringConstants.REQUEST_PROCESSED)){
                EmailDetails details = new EmailDetails();
                details.setRecipient(response.getEmail());
                ResponseEntity<EmailResponse> emailResponseResponseEntity = client.sendOnboardingEmailToClient(details);
                EmailResponse emailResponse = emailResponseResponseEntity.getBody();
                if(!emailResponse.getMessage().equals(StringConstants.MAIL_SENT_SUCCESSFULLY)) {
                    throw new EmailException(emailResponse.getMessage());
                }
            }
        } catch (ValidationException | MapperException | DataBaseOperationException exception){
            if(joiner == null){
                joiner = new Joiner();
            }
            joiner.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(joiner);
        } catch (EmailException exception) {
            LOGGER.error("Error occurred while sending acknowledgement email to {}", joiner.getEmail());
            response.setMessage(StringConstants.ERROR_SENDING_EMAIL);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        isRetryEnabled = false;
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<Joiner> saveJoinerDBRetry(Joiner joiner, Exception exception){
        isRetryEnabled = false;
        LOGGER.info(StringConstants.FALLBACK_MESSAGE, exception);
        joiner.setMessage(StringConstants.FALLBACK_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(joiner);
    }

    @Operation(
            summary = "Update joiner",
            description = "Update joiner and gives the same joiner response with a message which defines whether the request is successful or not."
    )
    @PostMapping("/update-joiner/{email}")
    @Retry(name = "update-joiner-db-retry", fallbackMethod = "updateJoinerDBRetry")
    public ResponseEntity<String> updateJoiner(@PathVariable String email){
        String response = null;
        try{
            if(StringUtils.isBlank(email) || StringUtils.isEmpty(email)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if(isRetryEnabled){
                LOGGER.info(StringConstants.RETRY_MESSAGE);
            }
            if(!isRetryEnabled){
                isRetryEnabled = true;
            }
            response = service.updateJoiner(email);
            if(response.equals(StringConstants.REQUEST_PROCESSED)){
                EmailDetails details = new EmailDetails();
                details.setRecipient(email);
                ResponseEntity<EmailResponse> emailResponseResponseEntity = client.sendOnboardingCompleteEmailToClient(details);
                EmailResponse emailResponse = emailResponseResponseEntity.getBody();
                if(!emailResponse.getMessage().equals(StringConstants.MAIL_SENT_SUCCESSFULLY)) {
                    throw new EmailException(emailResponse.getMessage());
                }
            }
        } catch (DataBaseOperationException exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        } catch (EmailException exception) {
            LOGGER.error("Error occurred while sending acknowledgement email to {}", email);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
        isRetryEnabled = false;
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<String> updateJoinerDBRetry(String email, Exception exception){
        isRetryEnabled = false;
        LOGGER.info(StringConstants.FALLBACK_MESSAGE, exception);
        return ResponseEntity.status(HttpStatus.OK).body(StringConstants.FALLBACK_MESSAGE);
    }
}
