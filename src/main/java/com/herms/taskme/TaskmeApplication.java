package com.herms.taskme;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

@SpringBootApplication
public class TaskmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskmeApplication.class, args);
    }
}
