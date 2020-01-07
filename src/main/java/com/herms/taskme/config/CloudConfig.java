package com.herms.taskme.config;

import com.herms.taskme.model.CloudinaryManager;
import com.herms.taskme.model.CloudinarySettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackages = { "com.herms.taskme.*" })
@PropertySource("classpath:cloud.properties")
public class CloudConfig {

    @Autowired
    private Environment env;

    @Bean
    public CloudinaryManager cloudinarySettings() {
        CloudinarySettings cs = new CloudinarySettings();
        cs.setCloudName(env.getProperty("cloud.cloudName"));
        cs.setApiKey(env.getProperty("cloud.apiKey"));
        cs.setApiSecret(env.getProperty("cloud.apiSecret"));
        return new CloudinaryManager(cs);
    }
}
