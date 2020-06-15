package com.herms.taskme.service;

import com.herms.taskme.model.Media;
import com.herms.taskme.model.User;
import com.herms.taskme.repository.MediaRepository;
import com.herms.taskme.repository.ParamRepository;
import com.herms.taskme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ParamRepository paramRepository;
    @Autowired
    private MediaRepository mediaRepository;

    public UserService() {
    }

    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public User getUser(Long id){
        return userRepository.findById(id).get();
    }

    public User addUser(User user){

        user = userRepository.save(user);
        if(user.getProfilePhoto() == null){
            Media profilePhoto = new Media();
            profilePhoto.setDescription("Profile picture");
            profilePhoto.setType(Media.MEDIA_TYPE_IMG);
            //we are not using this because it's necessary to include the param with the default_img_url
            // all the time, because we are always creat-dropping the db.
            //when we are done with the db, we can change it to the param finding
//            profilePhoto.setUrl(paramRepository.findByCode("DEFAULT_IMG_URL").getValue());
            profilePhoto.setUrl(ParamRepository.DEFAULT_PROFILE_IMG_URL);
            user.setProfilePhoto(profilePhoto);
        }
        user.setCreatedOn(new Date());
        return userRepository.save(user);
    }

    public void updateUser(Long id, User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    
    public List<User> findAllTaskApplicantsByTaskSomeone_IdOrderByCreatedOnDesc(Long taskSomeoneId){
    	return userRepository.findAllTaskApplicantsByTaskSomeone_IdOrderByCreatedOnDesc(taskSomeoneId);
    }
}

