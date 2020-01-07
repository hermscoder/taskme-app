package com.herms.taskme.controller;

import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.herms.taskme.dto.MediaForCreationDTO;
import com.herms.taskme.model.CloudinaryManager;
import com.herms.taskme.model.Media;
import com.herms.taskme.model.User;
import com.herms.taskme.dto.UserDTO;
import com.herms.taskme.model.UserPassChange;
import com.herms.taskme.service.CustomUserDetailsService;
import com.herms.taskme.service.MediaService;
import com.herms.taskme.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.ws.rs.FormParam;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logged")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private CloudinaryManager cloudinaryManager;
    @Autowired
    private MediaService mediaService;

//    @Secured("ROLE_USER")
    @RequestMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}")
    public UserDTO getUser(@PathVariable Long id){
        UserDTO userDTO = new UserDTO();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(userService.getUser(id), userDTO);

        return userDTO;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users")
    public ResponseEntity<User> addUser(@RequestBody User user){
        User addedUser = userService.addUser(user);

        return new ResponseEntity<>(addedUser, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) throws Exception{
        User principal = customUserDetailsService.getLoggedUser();
        if(id != null && !id.equals(principal.getId())){
            throw new Exception("You don't have access to this function");
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(userDTO, principal);
        userService.updateUser(id, principal);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/users/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users/passwordChange")
    public void passwordChange(@RequestBody UserPassChange userPassChange) throws ServletException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userPassChange.getUsername(),
                        userPassChange.getCurrentPassword()
                )
        );
        //if everything went fine, we can change the user password.
        User userToUpdate = userService.getUser(userPassChange.getId());
        userToUpdate.setPassword(BCrypt.hashpw(userPassChange.getNewPassword(), BCrypt.gensalt()));
        userService.updateUser(userToUpdate.getId(), userToUpdate);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/users/updateProfileImage/{id}")
    public ResponseEntity<Media> updateUserProfileImage(@RequestParam("file") MultipartFile multipartFile, @PathVariable Long id) throws Exception {

        User principal = customUserDetailsService.getLoggedUser();
        if(id != null && !id.equals(principal.getId())){
            throw new Exception("You don't have access to this function");
        }

        if(multipartFile.isEmpty()){
            throw new Exception("Invalid file size");
        }

        //if everything went fine, we can update the user image.
        User userToUpdate = userService.getUser(id);
        mediaService.updateMediaContentOnCloud(userToUpdate.getProfilePhoto(), multipartFile.getBytes());
        return new ResponseEntity<>(userToUpdate.getProfilePhoto(), HttpStatus.OK);

    }
}
