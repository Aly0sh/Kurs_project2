package com.example.autoMarket.controllers;

import com.example.autoMarket.models.CarInfo;
import com.example.autoMarket.models.User;
import com.example.autoMarket.repo.CarInfoRepository;
import com.example.autoMarket.repo.CommentRepository;
import com.example.autoMarket.repo.UserRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarInfoRepository carInfoRepository;

    @Autowired
    private CommentRepository commentRepository;


    @GetMapping("/userLists")
    public String getUserListsPage(Model model){
        List<User> tempUsers = userRepository.findAll().stream().filter(user -> !user.getUsername().equalsIgnoreCase("admin")).collect(Collectors.toList());
        Iterable<User> userList = tempUsers;
        model.addAttribute("user_list", userList);
        return "userLists";
    }

    @DeleteMapping("/userLists/{id}")
    public String deleteUser(@PathVariable(value = "id") long id){
        userRepository.deleteById(id);
        return "redirect:/userLists";
    }

    @GetMapping("/user-autos/{id}")
    public String userAutos(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("user", userRepository.findById(id).get().getUsername());
        Iterable<CarInfo> autoPosts = carInfoRepository.findByUserUsername(userRepository.findById(id).get().getUsername());
        if (carInfoRepository.findByUserUsername(userRepository.findById(id).get().getUsername()).isEmpty()){
            model.addAttribute("noData", true);
        }
        else{
            model.addAttribute("autoPosts", autoPosts);
        }
        return "user-autos";
    }

    @DeleteMapping("/user-auto/{id}/delete")
    public String userAutoDelete(@PathVariable(value = "id") long id){
        carInfoRepository.deleteById(id);
        return "redirect:/userLists";
    }

}
