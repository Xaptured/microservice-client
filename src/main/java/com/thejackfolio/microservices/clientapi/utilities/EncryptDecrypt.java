/*
 * Copyright (c) 2023.
 * Created this for the project called "TheJackFolio"
 * All right reserved by Jack
 */

package com.thejackfolio.microservices.clientapi.utilities;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class EncryptDecrypt {

    @Autowired
    private ApplicationContext context;
    private StringEncryptor jasyptStringEncryptor;

    public String encrypt(String plainText) {
        jasyptStringEncryptor = (StringEncryptor) SpringBeanApplicationContext.getBean("jasyptStringEncryptor");
        return jasyptStringEncryptor.encrypt(plainText);
    }

    public String decrypt(String cypherText) {
        jasyptStringEncryptor = (StringEncryptor) SpringBeanApplicationContext.getBean("jasyptStringEncryptor");
        return jasyptStringEncryptor.decrypt(cypherText);
    }
}
