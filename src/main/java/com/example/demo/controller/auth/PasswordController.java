package com.example.demo.controller.auth;

import com.example.demo.dto.error.ErrorDTO;
import com.example.demo.dto.user.PasswordDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/forgot")
public class PasswordController {
    private final UserService userService;

    //private Environment env;
    @Autowired
    private JavaMailSender mailSender;

    public PasswordController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestParam("email") String email) {
        try {
            User user = userService.findUserByEmail(email);
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            mailSender.send(constructResetTokenEmail(token, user));
            String message = "Se ha enviado un email a " + email + " para restablecer la contraseña";
            return new ResponseEntity<>(message,HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    // Save password
    @PostMapping("/user/savePassword")
    public ResponseEntity<?> savePassword(@RequestBody PasswordDTO passwordDto) {
        try {
            final String result = userService.validatePasswordResetToken(passwordDto.getToken());

            if (result.equals("invalidToken")) {
                return new ResponseEntity<>(ErrorDTO.fromMessage("Invalid token"), HttpStatus.BAD_REQUEST);
            }
            if (result.equals("expired")) {
                return new ResponseEntity<>(ErrorDTO.fromMessage("Expired token"), HttpStatus.BAD_REQUEST);
            }
            Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
            if (user.isPresent()) {
                userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
                return new ResponseEntity<>(("Contraseña guardada existosamente"), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage("No se pudo guardar la contraseña"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ErrorDTO.fromMessage("No se pudo guardar la contraseña"), HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/changePassword")
    public ResponseEntity<?> showChangePasswordPage(Model model, @RequestParam("token") String token) {
        String result = userService.validatePasswordResetToken(token);
        if(result.equals("invalidToken")) {
            return new ResponseEntity<>(ErrorDTO.fromMessage("Invalid token"), HttpStatus.BAD_REQUEST);
        }
        if (result.equals("expired")) {
            return new ResponseEntity<>(ErrorDTO.fromMessage("Expired token"), HttpStatus.BAD_REQUEST);
        }else {
            model.addAttribute("token", token);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    // Change user password
    @PostMapping("/updatePassword")
    public ResponseEntity<?> changeUserPassword(@RequestBody PasswordDTO passwordDto) throws Exception {
        try {
            final User user = userService.findUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
            userService.changeUserPassword(user, passwordDto.getNewPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage("No se pudo cambiar la contraseña"), HttpStatus.BAD_REQUEST);
        }

    }


    private SimpleMailMessage constructResetTokenEmail(final String token, final User user) {
        String messageBody = "You have requested to reset your password. Please click the link below to complete the process.";
        String url = "http://localhost:8080/forgot/resetPassword?token=" + token;
        return constructEmail(messageBody + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject("Reset Password");
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom("moon@maildrop.cc");
        return email;
    }
}
