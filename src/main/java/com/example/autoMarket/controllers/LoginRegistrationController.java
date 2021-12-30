package com.example.autoMarket.controllers;

import com.example.autoMarket.repo.UserRepository;
import com.example.autoMarket.services.UserRepr;
import com.example.autoMarket.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class LoginRegistrationController{

    private final UserService userService;
    private final UserRepository userRepository;
    private final String specialCharactersString = "1234567890!@#$%&*()'+,-./:;<=>?[]^_`{|}";
    private final String[] phoneNumberCodes = {"050", "070", "055", "075", "0990", "0995", "0997", "0998", "077", "0996"};

    @Autowired
    public LoginRegistrationController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth";
    }


    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new UserRepr());
        return "registration";
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid @ModelAttribute("user") UserRepr userRepr, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (userRepository.findByUsername(userRepr.getUsername()).isPresent()){
            bindingResult.rejectValue("username", "", "Benutzer existiert bereits");
            return "registration";
        }

        String userPassword = userRepr.getPassword();
        if (userPassword.length() < 8){
            bindingResult.rejectValue("password", "", "Das Passwort muss mindestens 8 Zeichen lang sein");
            return "registration";
        }

        if (userPassword.length() >= 8){
            int numberCount = 0;
            int symbolCount = 0;
            for (int i = 0; i < userPassword.length(); i++){
                String symbol = Character.toString(userPassword.charAt(i));
                if (specialCharactersString.contains(symbol)){
                    try {
                        Integer num = Integer.parseInt(symbol);
                        numberCount++;
                    }
                    catch (IllegalArgumentException e){
                        symbolCount++;
                    }

                }
            }
            System.out.println(numberCount + "  :  " + symbolCount);
            if (numberCount < 2 && symbolCount < 2){
                bindingResult.rejectValue("password", "", "Das Passwort muss aus mindestens 2 Ziffern und 2 Zeichen bestehen");
                return "registration";
            }
        }

        String userPhone = userRepr.getPhone();
        try {
            Integer.parseInt(userPhone);
            if (userPhone.length() != 10){
                bindingResult.rejectValue("phone", "", "Falsche Nummerneingabe");
                return "registration";
            }

            boolean isValidNumber = false;
            for (String numbCode : phoneNumberCodes){
                if (userPhone.startsWith(numbCode)){
                    isValidNumber = true;
                    break;
                }
            }
            if (!isValidNumber){
                bindingResult.rejectValue("phone", "", "Falsche Nummerneingabe");
                return "registration";
            }


        }
        catch (Exception e){
            bindingResult.rejectValue("phone", "", "Falsche Nummerneingabe");
            return "registration";
        }

        userService.create(userRepr);
        return "redirect:/login";
    }
}