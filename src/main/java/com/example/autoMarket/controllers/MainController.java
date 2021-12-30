package com.example.autoMarket.controllers;


import com.example.autoMarket.models.CarInfo;
import com.example.autoMarket.repo.CarInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    CarInfoRepository carInfoRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<CarInfo> cars = new ArrayList<>();
        int i = 0;
        for (CarInfo j:carInfoRepository.findAll()){
            if (i==6){
                break;
            }
            cars.add(j);
            i++;
        }
        model.addAttribute("cars", cars);
        return "index";
    }

    @GetMapping("/about-us")
    public String aboutUs(Model model) {
        return "about-us";
    }


}






















