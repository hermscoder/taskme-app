package com.herms.taskme.controller;

import com.herms.taskme.dto.UserDTO;
import com.herms.taskme.exception.UsernameAlreadyExistException;
import com.herms.taskme.model.User;
import com.herms.taskme.security.JwtTokenProvider;

import com.herms.taskme.service.CustomUserDetailsService;
import com.herms.taskme.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @RequestMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public LoginResponse login(@RequestBody User user) throws ServletException {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            throw new ServletException("Username and passwords are mandatory fields.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        UserDTO userDTO = new UserDTO();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(customUserDetailsService.getLoggedUser(), userDTO);

        LoginResponse loginResponse = new LoginResponse(jwt);
        loginResponse.setUser(userDTO);
        return loginResponse;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UsernameAlreadyExistException{
        User repoUser = userService.findByUsername(user.getUsername());

        if (repoUser != null) {
            throw  new UsernameAlreadyExistException("Username already been used.", "username");
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        User addedUser  = userService.addUser(user);
        return new ResponseEntity<>(addedUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/values", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public List<User> getValues(){
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L,"Emerson","Ribeiro", "36 70 987 7272", "Nefelejcs 38", "gutti", "mama"));
        userList.add(new User(2L,"Loren","Grajau", "36 11 387 1123", "Damininah 12", "haha", "tica"));
        userList.add(new User(3L,"Joana","Dark", "30 41 987 9411", "Do 33", "jdark", "kill"));
        return userList;
    }

    private class LoginResponse {
        private String token;

        private UserDTO user;

        public LoginResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public UserDTO getUser() {
            return user;
        }

        public void setUser(UserDTO user) {
            this.user = user;
        }
    }
}
