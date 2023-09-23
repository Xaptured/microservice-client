package com.thejackfolio.microservices.clientapi.services;

import com.thejackfolio.microservices.clientapi.exceptions.DataBaseOperationException;
import com.thejackfolio.microservices.clientapi.exceptions.EmailException;
import com.thejackfolio.microservices.clientapi.exceptions.MapperException;
import com.thejackfolio.microservices.clientapi.exceptions.ValidationException;
import com.thejackfolio.microservices.clientapi.models.ClientComments;
import com.thejackfolio.microservices.clientapi.models.EmailDetails;
import com.thejackfolio.microservices.clientapi.utilities.PropertiesReader;
import com.thejackfolio.microservices.clientapi.utilities.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulingService.class);

    @Autowired
    private CommentsService service;
    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 10 16 * * *")
    public void executeNonRespondedMails(){
        List<ClientComments> comments = null;
        try{
            comments = service.getComments();
            String messageBody = buildEmailMessageBody(comments);
            String completeMessage = StringConstants.ROBO_RESPONSE_INITIAL + messageBody + StringConstants.ROBO_RESPONSE_END;
            EmailDetails details = new EmailDetails();
            details.setMsgBody(completeMessage);
            details.setRecipient(PropertiesReader.getProperty(StringConstants.SENDER));
            details.setSubject(StringConstants.ROBO_RESPONSE_SUBJECT);
            emailService.sendMail(details, false);
        } catch (ValidationException | MapperException | DataBaseOperationException | EmailException exception){
            LOGGER.info(exception.getMessage());
        }
    }

    private String buildEmailMessageBody(List<ClientComments> comments){
        String mailBody = null;
        for(ClientComments comment : comments) {
            String id = "id : " + comment.getMessage() + "\n";
            String email = "email : " + comment.getEmail();
            String block = id + email;
            mailBody = block + "\n\n";
        }
        return mailBody;
    }
}
