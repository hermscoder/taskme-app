package com.herms.taskme.model;

import com.cloudinary.utils.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class CloudinarySettings {
    private String cloudName;
    private String apiKey;
    private String apiSecret;

    public String getCloudName() {
        return cloudName;
    }

    public void setCloudName(String cloudName) {
        this.cloudName = cloudName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public Map<String, String> getConfigMap(){
        return ObjectUtils.asMap("cloud_name", getCloudName(),
                "api_key", getApiKey(),
                "api_secret", getApiSecret());
    }
}
