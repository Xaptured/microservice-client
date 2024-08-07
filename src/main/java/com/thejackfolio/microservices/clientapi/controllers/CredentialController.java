package com.thejackfolio.microservices.clientapi.controllers;

import com.thejackfolio.microservices.clientapi.exceptions.DataBaseOperationException;
import com.thejackfolio.microservices.clientapi.exceptions.EmailException;
import com.thejackfolio.microservices.clientapi.exceptions.MapperException;
import com.thejackfolio.microservices.clientapi.exceptions.ValidationException;
import com.thejackfolio.microservices.clientapi.models.ClientCredential;
import com.thejackfolio.microservices.clientapi.services.CredentialService;
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

@RestController
@RequestMapping("/credential")
@Tag(name = "Credential", description = "Credential management APIs")
public class CredentialController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CredentialController.class);

    private boolean isRetryEnabled = false;
    @Autowired
    private CredentialService service;

    @Operation(
            summary = "Save credential",
            description = "Save credential and gives the same credential response with a message which defines whether the request is successful or not."
    )
    @PostMapping("/save-credential")
    @Retry(name = "save-credential-db-retry", fallbackMethod = "saveCredentialDBRetry")
    public ResponseEntity<ClientCredential> saveCredential(@RequestBody ClientCredential credential) {
        ClientCredential response = null;
        try{
            if(isRetryEnabled){
                LOGGER.info(StringConstants.RETRY_MESSAGE);
            }
            if(!isRetryEnabled){
                isRetryEnabled = true;
            }
            response = service.saveCredential(credential);
        } catch (EmailException exception){
            if(credential == null){
                credential = new ClientCredential();
            }
            credential.setMessage(StringConstants.EMAIL_EXIST);
            return ResponseEntity.status(HttpStatus.OK).body(credential);
        } catch (ValidationException | MapperException | DataBaseOperationException exception){
            if(credential == null){
                credential = new ClientCredential();
            }
            credential.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(credential);
        }
        isRetryEnabled = false;
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<ClientCredential> saveCredentialDBRetry(ClientCredential credential, Exception exception){
        isRetryEnabled = false;
        LOGGER.info(StringConstants.FALLBACK_MESSAGE, exception);
        ClientCredential credentialResponse = new ClientCredential();
        credentialResponse.setMessage(StringConstants.FALLBACK_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(credentialResponse);
    }

    @Operation(
            summary = "Get credential",
            description = "It gives the credential as response with a message which defines whether the request is successful or not."
    )
    @GetMapping("/get-credential/{email}")
    @Retry(name = "get-credential-db-retry", fallbackMethod = "getCredentialDBRetry")
    public ResponseEntity<ClientCredential> getCredential(@PathVariable String email){
        ClientCredential credential = null;
        try{
            if(isRetryEnabled){
                LOGGER.info(StringConstants.RETRY_MESSAGE);
            }
            if(!isRetryEnabled){
                isRetryEnabled = true;
            }
            credential = service.getCredential(email);
        } catch (EmailException exception){
            credential = new ClientCredential();
            credential.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(credential);
        } catch (ValidationException | MapperException | DataBaseOperationException exception){
            credential = new ClientCredential();
            credential.setMessage(exception.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(credential);
        }
        isRetryEnabled = false;
        return ResponseEntity.status(HttpStatus.OK).body(credential);
    }

    public ResponseEntity<ClientCredential> getCredentialDBRetry(Exception exception){
        isRetryEnabled = false;
        LOGGER.info(StringConstants.FALLBACK_MESSAGE, exception);
        ClientCredential credential = new ClientCredential();
        credential.setMessage(StringConstants.FALLBACK_MESSAGE);
        return ResponseEntity.status(HttpStatus.OK).body(credential);
    }
}
