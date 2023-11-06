package com.thejackfolio.microservices.clientapi.services;

import com.thejackfolio.microservices.clientapi.clients.TheJackFolioDBClient;
import com.thejackfolio.microservices.clientapi.exceptions.DataBaseOperationException;
import com.thejackfolio.microservices.clientapi.exceptions.MapperException;
import com.thejackfolio.microservices.clientapi.exceptions.ValidationException;
import com.thejackfolio.microservices.clientapi.models.ClientComments;
import com.thejackfolio.microservices.clientapi.servicehelpers.IncomingValidation;
import com.thejackfolio.microservices.clientapi.utilities.StringConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentsService {

    @Autowired
    private IncomingValidation validation;
    @Autowired
    private TheJackFolioDBClient client;

    public ClientComments saveComment(ClientComments comments) throws ValidationException, DataBaseOperationException, MapperException {
        validation.checkCommentsFromUI(comments);
        ResponseEntity<ClientComments> response = client.saveComments(comments);
        ClientComments responseBody = response.getBody();
        validation.checkCommentsFromDB(responseBody);
        if(responseBody.getMessage().equals(StringConstants.DATABASE_ERROR)){
            throw new DataBaseOperationException(responseBody.getMessage());
        }
        else if(responseBody.getMessage().equals(StringConstants.MAPPING_ERROR)){
            throw new MapperException(responseBody.getMessage());
        }
        return responseBody;
    }

    public List<ClientComments> getComments() throws ValidationException, DataBaseOperationException, MapperException {
        ResponseEntity<List<ClientComments>> response = client.getComments();
        List<ClientComments> comments = response.getBody();
        validation.checkCommentsListFromDB(comments);
        if(comments.size() == 1 && comments.get(0).getMessage().equals(StringConstants.DATABASE_ERROR)){
            throw new DataBaseOperationException(comments.get(0).getMessage());
        } else if(comments.size() == 1 && comments.get(0).equals(StringConstants.MAPPING_ERROR)){
            throw new MapperException(comments.get(0).getMessage());
        }
        return comments;
    }
}
