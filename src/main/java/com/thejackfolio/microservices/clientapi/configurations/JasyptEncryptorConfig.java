/*
 * Copyright (c) 2023.
 * Created this for the project called "TheJackFolio"
 * All right reserved by Jack
 */

package com.thejackfolio.microservices.clientapi.configurations;

import com.thejackfolio.microservices.clientapi.utilities.PropertiesReader;
import com.thejackfolio.microservices.clientapi.utilities.StringConstants;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptEncryptorConfig {

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor getPasswordEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();

        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(PropertiesReader.getProperty(StringConstants.ENCRYPTION_PASSWORD));
        config.setAlgorithm(PropertiesReader.getProperty(StringConstants.ENCRYPTION_ALGORITHM));
        config.setKeyObtentionIterations(PropertiesReader.getProperty(StringConstants.ENCRYPTION_ITERATIONS));
        config.setPoolSize(PropertiesReader.getProperty(StringConstants.ENCRYPTION_POOL_SIZE));
        config.setProviderName(PropertiesReader.getProperty(StringConstants.ENCRYPTION_PROVIDER_NAME));
        config.setSaltGeneratorClassName(PropertiesReader.getProperty(StringConstants.ENCRYPTION_SALT_GENERATOR));
        config.setStringOutputType(PropertiesReader.getProperty(StringConstants.ENCRYPTION_OUTPUT_TYPE));
        encryptor.setConfig(config);

        return encryptor;
    }
}
