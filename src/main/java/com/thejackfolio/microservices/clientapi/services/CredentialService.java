package com.thejackfolio.microservices.clientapi.services;

import com.thejackfolio.microservices.clientapi.clients.TheJackFolioDBClient;
import com.thejackfolio.microservices.clientapi.exceptions.DataBaseOperationException;
import com.thejackfolio.microservices.clientapi.exceptions.EmailException;
import com.thejackfolio.microservices.clientapi.exceptions.MapperException;
import com.thejackfolio.microservices.clientapi.exceptions.ValidationException;
import com.thejackfolio.microservices.clientapi.models.ClientCredential;
import com.thejackfolio.microservices.clientapi.servicehelpers.IncomingValidation;
import com.thejackfolio.microservices.clientapi.utilities.StringConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {

    @Autowired
    private IncomingValidation validation;
    @Autowired
    private TheJackFolioDBClient client;

    public ClientCredential saveCredential(ClientCredential credential) throws ValidationException, DataBaseOperationException, MapperException, EmailException {
        validation.checkCredentialFromUI(credential);
        ResponseEntity<ClientCredential> response = client.saveClientCredential(credential);
        ClientCredential responseBody = response.getBody();
        validation.checkCredentialFromDB(responseBody);
        if(responseBody.getMessage().equals(StringConstants.DATABASE_ERROR)){
            throw new DataBaseOperationException(responseBody.getMessage());
        }
        else if(responseBody.getMessage().equals(StringConstants.MAPPING_ERROR)){
            throw new MapperException(responseBody.getMessage());
        }
        else if(responseBody.getMessage().equals(StringConstants.DUPLICATE_EMAIL)){
            throw new EmailException(responseBody.getMessage());
        }
        return responseBody;
    }

    public ClientCredential getCredential(String email) throws ValidationException, DataBaseOperationException, MapperException, EmailException {
        ResponseEntity<ClientCredential> response = client.findClientCredential(email);
        ClientCredential credential = response.getBody();
        validation.checkCredentialFromDB(credential);
        if(credential.getMessage().equals(StringConstants.DATABASE_ERROR)){
            throw new DataBaseOperationException(credential.getMessage());
        }
        else if(credential.getMessage().equals(StringConstants.MAPPING_ERROR)){
            throw new MapperException(credential.getMessage());
        }
        else if(credential.getMessage().equals(StringConstants.EMAIL_NOT_PRESENT)){
            throw new EmailException(StringConstants.USER_NOT_FOUND);
        }
        return credential;
    }
}
