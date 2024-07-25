package com.thejackfolio.microservices.clientapi.services;

import com.thejackfolio.microservices.clientapi.clients.TheJackFolioDBClient;
import com.thejackfolio.microservices.clientapi.exceptions.DataBaseOperationException;
import com.thejackfolio.microservices.clientapi.exceptions.EmailException;
import com.thejackfolio.microservices.clientapi.exceptions.MapperException;
import com.thejackfolio.microservices.clientapi.exceptions.ValidationException;
import com.thejackfolio.microservices.clientapi.models.ClientComments;
import com.thejackfolio.microservices.clientapi.models.Joiner;
import com.thejackfolio.microservices.clientapi.servicehelpers.IncomingValidation;
import com.thejackfolio.microservices.clientapi.utilities.StringConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class JoinerService {

    @Autowired
    private IncomingValidation validation;
    @Autowired
    private TheJackFolioDBClient client;

    public Joiner saveJoiner(Joiner joiner) throws ValidationException, DataBaseOperationException, MapperException {
        validation.checkJoinerFromUI(joiner);
        ResponseEntity<Joiner> response = client.saveJoiner(joiner);
        Joiner responseBody = response.getBody();
        if(responseBody.getMessage().equals(StringConstants.DATABASE_ERROR)){
            throw new DataBaseOperationException(responseBody.getMessage());
        }
        else if(responseBody.getMessage().equals(StringConstants.MAPPING_ERROR)){
            throw new MapperException(responseBody.getMessage());
        }
        else if(responseBody.getMessage().equals(StringConstants.DUPLICATE_EMAIL)){
            throw new DataBaseOperationException(responseBody.getMessage());
        }
        return responseBody;
    }

    public String updateJoiner(String email) throws DataBaseOperationException {
        ResponseEntity<String> response = client.updateJoiner(email);
        String responseBody = response.getBody();
        if(responseBody.equals(StringConstants.DATABASE_ERROR)){
            throw new DataBaseOperationException(responseBody);
        }
        else if(responseBody.equals(StringConstants.DUPLICATE_EMAIL)){
            throw new DataBaseOperationException(responseBody);
        }
        return responseBody;
    }
}
