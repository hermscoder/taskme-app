package com.herms.taskme.service;

import com.herms.taskme.enums.ActionEnum;
import com.herms.taskme.exception.UsernameAlreadyExistException;
import com.herms.taskme.model.Media;
import com.herms.taskme.model.User;
import com.herms.taskme.repository.MediaRepository;
import com.herms.taskme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MediaRepository mediaRepository;

    public static final String DEFAULT_PROFILE_IMG_URL  = "https://res.cloudinary.com/dsonk49e9/image/upload/v1578403951/default_profile_img.png";

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

    public User addUser(User user) throws UsernameAlreadyExistException {
        if(validateAndThrows(user, ActionEnum.CREATING)){
            user = userRepository.save(user);
            if(user.getProfilePhoto() == null){
                Media profilePhoto = new Media();
                profilePhoto.setDescription("Profile picture");
                profilePhoto.setType(Media.MEDIA_TYPE_IMG);
                profilePhoto.setUrl(DEFAULT_PROFILE_IMG_URL);
                user.setProfilePhoto(profilePhoto);
            }
            user.setCreatedOn(new Date());
            return userRepository.save(user);
        }
        return null;
    }

    public boolean validateAndThrows(User user, ActionEnum action) throws UsernameAlreadyExistException {
        User repoUser = findByUsername(user.getUsername());

        if (repoUser != null && action.equals(ActionEnum.CREATING)) {
            throw  new UsernameAlreadyExistException("Username already been used.", "username");
        }

        return true;
    }

    public void updateUser(Long id, User user) throws UsernameAlreadyExistException {
        if(validateAndThrows(user, ActionEnum.UPDATING)){
            userRepository.save(user);
        }
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

    public User rateUser(Long userIdToRate, Integer rate) {
        User userToChange = userRepository.findById(userIdToRate).get();

        Integer numOfRates = userToChange.getNumberOfRates() == null ? 0 : userToChange.getNumberOfRates();
        Integer rateSum = userToChange.getRateSum() == null ? 0 : userToChange.getRateSum();

        userToChange.setRateSum(rateSum + rate);
        userToChange.setNumberOfRates(numOfRates + 1);

        return userRepository.save(userToChange);
    }
}

