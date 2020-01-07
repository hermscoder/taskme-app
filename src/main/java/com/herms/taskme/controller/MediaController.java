package com.herms.taskme.controller;

import com.herms.taskme.model.CloudinaryManager;
import com.herms.taskme.model.Media;
import com.herms.taskme.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/logged")
public class MediaController {
    @Autowired
    private MediaService mediaService;
    @Autowired
    private CloudinaryManager cloudinaryManager;

    @RequestMapping(method = RequestMethod.GET, value = "/medias")
    public List<Media> getAllMedia(){
        return mediaService.getAllMedia();
    }


    @RequestMapping("/medias/{id}")
    public Media getMedia(@PathVariable Long id){
        return mediaService.getMedia(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/medias")
    public void addMedia(@RequestBody Media taskSomeone){
        mediaService.addMedia(taskSomeone);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/medias/{id}")
    public void updateMedia(@RequestBody Media taskSomeone, @PathVariable Long id){
        mediaService.updateMedia(id, taskSomeone);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/medias/{id}")
    public void deleteMedia(@PathVariable Long id) throws Exception {
        mediaService.deleteMedia(id);
    }
}
