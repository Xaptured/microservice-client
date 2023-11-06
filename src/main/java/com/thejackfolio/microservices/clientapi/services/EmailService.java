package com.thejackfolio.microservices.clientapi.services;

import com.thejackfolio.microservices.clientapi.clients.EmailClient;
import com.thejackfolio.microservices.clientapi.controllers.CommentsController;
import com.thejackfolio.microservices.clientapi.exceptions.EmailException;
import com.thejackfolio.microservices.clientapi.models.EmailDetails;
import com.thejackfolio.microservices.clientapi.utilities.PropertiesReader;
import com.thejackfolio.microservices.clientapi.utilities.StringConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private static final String SENDER_EMAIL = PropertiesReader.getProperty(StringConstants.SENDER);
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailClient client;

    public void sendEmailToClient(EmailDetails emailDetails) throws EmailException {
        String messageBody = StringConstants.RESPONSE_INITIAL + emailDetails.getMsgBody() + StringConstants.RESPONSE_END;
        emailDetails.setMsgBody(messageBody);
        client.sendResponseEmailToClient(emailDetails);
    }

//    public void sendMail(EmailDetails details, boolean isAcknowledgement) throws EmailException {
//        try {
//            SimpleMailMessage mailMessage = getMailMessage(details, isAcknowledgement);
//
//            javaMailSender.send(mailMessage);
//            LOGGER.info(StringConstants.MAIL_SENT_SUCCESSFULLY);
//        } catch (Exception exception) {
//            LOGGER.info(StringConstants.ERROR_OCCURRED_SENDING_EMAIL);
//            throw new EmailException(StringConstants.ERROR_OCCURRED_SENDING_EMAIL, exception);
//        }
//    }

//    private SimpleMailMessage getMailMessage(EmailDetails details, boolean isAcknowledgement) {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//
//        mailMessage.setFrom(SENDER_EMAIL);
//        mailMessage.setTo(details.getRecipient());
//        if(isAcknowledgement){
//            mailMessage.setText(StringConstants.ACKNOWLEDGE_BODY);
//            mailMessage.setSubject(StringConstants.ACKNOWLEDGE_SUBJECT);
//        } else {
//            mailMessage.setText(details.getMsgBody());
//            mailMessage.setSubject(details.getSubject());
//        }
//        return mailMessage;
//    }
}
