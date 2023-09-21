/*
 * Copyright (c) 2023.
 * Created this for the project called "TheJackFolio"
 * All right reserved by Jack
 */

package com.thejackfolio.microservices.clientapi.utilities;

public class StringConstants {

    // Mapper and Database error messages
    public static final String MAPPING_ERROR = "Error occurred while mapping";
    public static final String DATABASE_ERROR = "Error occurred while database operation";
    public static final String VALIDATION_ERROR = "Error occurred while validation";
    public static final String VALIDATION_PASSED_UI = "Validation passed for incoming request from UI";
    public static final String VALIDATION_PASSED_DB = "Validation passed for incoming request from DB";
    public static final String FALLBACK_MESSAGE = "Something went wrong. Please try again later";
    public static final String RETRY_MESSAGE = "Something went wrong. Doing retry...";

    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String ROLE = "ROLE";


    public static final String SENDER = "SENDER";
    public static final String ACKNOWLEDGE_BODY = "Hey! \n\nHope you are doing well. Thanks for reaching out to me. \nPlease wait maximum of one day to get my response. Have a nice day. \n\nThanks \nJack  \n\nThis is an auto triggered email. Please don't reply to this." ;
    public static final String RESPONSE_INITIAL = "Hey! \n\nHope you are doing well. Thanks for reaching out to me. \n";
    public static final String RESPONSE_END = " \n\nThanks \nJack";
    public static final String ACKNOWLEDGE_SUBJECT = "ACKNOWLEDGEMENT";
    public static final String MAIL_SENT_SUCCESSFULLY = "Mail Sent Successfully";
    public static final String ERROR_OCCURRED_SENDING_EMAIL = "Error occurred while sending email";

    public static final String REQUEST_PROCESSED = "Request Processed";

    public static final String ENCRYPTION_PASSWORD = "ENCRYPTION_PASSWORD";
    public static final String ENCRYPTION_ALGORITHM = "ENCRYPTION_ALGORITHM";
    public static final String ENCRYPTION_ITERATIONS = "ENCRYPTION_ITERATIONS";
    public static final String ENCRYPTION_POOL_SIZE = "ENCRYPTION_POOL_SIZE";
    public static final String ENCRYPTION_PROVIDER_NAME = "ENCRYPTION_PROVIDER_NAME";
    public static final String ENCRYPTION_SALT_GENERATOR = "ENCRYPTION_SALT_GENERATOR";
    public static final String ENCRYPTION_OUTPUT_TYPE = "ENCRYPTION_OUTPUT_TYPE";

    private StringConstants(){}

}
