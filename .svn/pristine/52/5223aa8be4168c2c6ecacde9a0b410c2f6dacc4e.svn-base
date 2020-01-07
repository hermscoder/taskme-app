package com.herms.taskme.model;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.Map;

public class CloudinaryManager {

    private CloudinarySettings cloudinarySettings;

    public CloudinaryManager (CloudinarySettings cloudinarySettings){
        this.cloudinarySettings = cloudinarySettings;
    }

    public void setCloudinarySettings(CloudinarySettings cloudinarySettings) {
        this.cloudinarySettings = cloudinarySettings;
    }

    public Cloudinary getCloudinaryInstance(){
        return new Cloudinary(cloudinarySettings.getConfigMap());
    }

    public Map<String, String> upload(Object file, Map<String, String> options) throws IOException {
        return getCloudinaryInstance().uploader().upload(file, options);
    }

    public Map<String, String> destroy(String publicId, Map<String, String> options) throws IOException {
        return getCloudinaryInstance().uploader().destroy(publicId, options);
    }
}
