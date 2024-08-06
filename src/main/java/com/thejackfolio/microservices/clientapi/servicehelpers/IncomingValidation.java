package com.thejackfolio.microservices.clientapi.servicehelpers;

import com.thejackfolio.microservices.clientapi.exceptions.ValidationException;
import com.thejackfolio.microservices.clientapi.models.ClientComments;
import com.thejackfolio.microservices.clientapi.models.ClientCredential;
import com.thejackfolio.microservices.clientapi.models.Joiner;
import com.thejackfolio.microservices.clientapi.utilities.StringConstants;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomingValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingValidation.class);

    public void checkCommentsFromUI(ClientComments comments) throws ValidationException {
        if(comments == null){
            LOGGER.error("Validation failed in IncomingValidation.class : checkCommentsFromUI for object: null");
            throw new ValidationException(StringConstants.VALIDATION_ERROR);
        }
        checkUIComments(comments);
    }

    private void checkUIComments(ClientComments comments) throws ValidationException {
        if(Strings.isNotEmpty(comments.getEmail()) && Strings.isNotBlank(comments.getEmail())
                && Strings.isNotEmpty(comments.getComments()) && Strings.isNotBlank(comments.getComments())){
            LOGGER.info(StringConstants.VALIDATION_PASSED_UI);
        } else {
            LOGGER.error("Validation failed in IncomingValidation.class : checkUIComments for object: {}", comments);
            throw new ValidationException(StringConstants.VALIDATION_ERROR);
        }
    }

    public void checkCommentsFromDB(ClientComments comments) throws ValidationException {
        if(comments == null){
            LOGGER.error("Validation failed in IncomingValidation.class : checkCommentsFromDB for object: null");
            throw new ValidationException(StringConstants.VALIDATION_ERROR);
        }
        checkDBComments(comments, true);
    }

    private void checkDBComments(ClientComments comments, boolean loggerRequired) throws ValidationException {
        if(Strings.isNotEmpty(comments.getEmail()) && Strings.isNotBlank(comments.getEmail())
                && Strings.isNotEmpty(comments.getComments()) && Strings.isNotBlank(comments.getComments())
                    && Strings.isNotEmpty(comments.getMessage()) && Strings.isNotBlank(comments.getMessage())){
            if(loggerRequired){
                LOGGER.info(StringConstants.VALIDATION_PASSED_DB);
            }
        } else {
            LOGGER.error("Validation failed in IncomingValidation.class : checkDBComments for object: {}", comments);
            throw new ValidationException(StringConstants.VALIDATION_ERROR);
        }
    }

    public void checkCommentsListFromDB(List<ClientComments> comments) throws ValidationException {
        if(comments != null && !comments.isEmpty()){
            for(ClientComments comment : comments){
                checkDBComments(comment, false);
            }
            LOGGER.info(StringConstants.VALIDATION_PASSED_DB);
        } else if(comments == null) {
            LOGGER.error("Validation failed in IncomingValidation.class : checkCommentsListFromDB for object: null");
            throw new ValidationException(StringConstants.VALIDATION_ERROR);
        }
    }

    public void checkCredentialFromUI(ClientCredential credential) throws ValidationException {
        if(credential == null){
            LOGGER.error("Validation failed in IncomingValidation.class : checkCredentialFromUI for object: null");
            throw new ValidationException(StringConstants.VALIDATION_ERROR);
        }
        checkUICredential(credential);
    }

    private void checkUICredential(ClientCredential credential) throws ValidationException {
        if(Strings.isNotEmpty(credential.getEmail()) && Strings.isNotBlank(credential.getEmail())
                && Strings.isNotEmpty(credential.getPassword()) && Strings.isNotBlank(credential.getPassword())){
            LOGGER.info(StringConstants.VALIDATION_PASSED_UI);
        } else {
            LOGGER.error("Validation failed in IncomingValidation.class : checkUICredential for object: {}", credential);
            throw new ValidationException(StringConstants.VALIDATION_ERROR);
        }
    }

    public void checkCredentialFromDB(ClientCredential credential) throws ValidationException {
        if(credential == null){
            LOGGER.error("Validation failed in IncomingValidation.class : checkCredentialFromDB for object: null");
            throw new ValidationException(StringConstants.VALIDATION_ERROR);
        }
        // checkDBCredential(credential);
    }

    private void checkDBCredential(ClientCredential credential) throws ValidationException {
        if(Strings.isNotEmpty(credential.getEmail()) && Strings.isNotBlank(credential.getEmail())
                && Strings.isNotEmpty(credential.getPassword()) && Strings.isNotBlank(credential.getPassword())
                && Strings.isNotEmpty(credential.getMessage()) && Strings.isNotBlank(credential.getMessage())){
                LOGGER.info(StringConstants.VALIDATION_PASSED_DB);
        } else {
            LOGGER.error("Validation failed in IncomingValidation.class : checkDBCredential for object: {}", credential);
            throw new ValidationException(StringConstants.VALIDATION_ERROR);
        }
    }

    public void checkJoinerFromUI(Joiner joiner) throws ValidationException {
        if(joiner == null){
            LOGGER.error("Validation failed in IncomingValidation.class : checkJoinerFromUI for object: null");
            throw new ValidationException(StringConstants.VALIDATION_ERROR);
        }
        checkUIJoiner(joiner);
    }

    private void checkUIJoiner(Joiner joiner) throws ValidationException {
        if(Strings.isNotEmpty(joiner.getEmail()) && Strings.isNotBlank(joiner.getEmail())){
            LOGGER.info(StringConstants.VALIDATION_PASSED_UI);
        } else {
            LOGGER.error("Validation failed in IncomingValidation.class : checkUIJoiner for object: {}", joiner);
            throw new ValidationException(StringConstants.VALIDATION_ERROR);
        }
    }
}
