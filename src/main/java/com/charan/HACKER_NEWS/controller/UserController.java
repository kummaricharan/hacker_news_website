package com.charan.HACKER_NEWS.controller;

import com.charan.HACKER_NEWS.entity.Story;
import com.charan.HACKER_NEWS.entity.User;
import com.charan.HACKER_NEWS.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

@Controller
@RequestMapping("/user")
public class UserController {
    private UserService userService;
//    private MediaService mediaService;
//
//    @Autowired
//    public UserController(UserService userService, MediaService mediaService) {
//        this.userService = userService;
//        this.mediaService = mediaService;
//    }

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/register")
    public String showRegistorForm(Model model){
        User user = new User();
        model.addAttribute("user",user);
        return "posts/registration_form";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute("user") User user, @RequestParam("role") String role, @RequestParam(value = "file",required = false)MultipartFile file, @RequestParam("password") String password, Model model){
        User users = userService.findByName(user.getUsername());

        if(users!=null){
            model.addAttribute("error","user name already present try with different one");
            return "posts/registration_form";
        }
        else{
            user.setEnabled(1);
            user.setRole("ROLE_"+role);
            user.setPassword("{noop}" + password);
            Timestamp currentTimestamp = new Timestamp(new Date().getTime());
            user.setSubmissionTime(currentTimestamp);
            userService.save(user);
//            if(file!=null && !file.isEmpty()) {
//                byte[] bytes = new byte[0];
//                try {
//                    bytes = file.getBytes();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                Blob blob = null;
//                try {
//                    blob = new SerialBlob(bytes);
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }
//
//                if (user.getMedia() != null) {
//                    user.getMedia().setMedia(blob);
//                    user.getMedia().setContentType(file.getContentType());
//                    mediaService.create(user.getMedia());
//                } else {
//                    Media media = new Media();
//                    media.setMedia(blob);
//                    media.setContentType(file.getContentType());
//                    mediaService.create(media);
//                    user.setMedia(media);
//                }
//            }
            return "redirect:/user/LoginPage";
        }
    }
    @GetMapping("/LoginPage")
    public String loginPage(){
        return "posts/login_form";
    }
    @GetMapping("/changepw")
    public String changePw(){
        return "posts/change_password_page";
    }
    @PostMapping("/saveChangePw")
    public String saveChangePw(Model model,@RequestParam("username") String username , @RequestParam("newPassword") String password){
       User user = userService.findByName(username);
       if(user==null){
           model.addAttribute("username",username);
           model.addAttribute("errorMessage", "Enter correct username");
           return "posts/change_password_page";
       }
        user.setPassword("{noop}" + password);
        userService.save(user);
        model.addAttribute("successMessage", "Updated sucessfully");
        return "posts/login_form";
    }
//    @GetMapping("/display")
//    public ResponseEntity<byte[]> displayImage(@RequestParam("id") Integer id) throws IOException, SQLException {
//        Media media = mediaService.viewById(id);
//        byte[] mediaBytes = null;
//        mediaBytes = media.getMedia().getBytes(1, (int) media.getMedia().length());
//        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(mediaBytes);
//    }

}
