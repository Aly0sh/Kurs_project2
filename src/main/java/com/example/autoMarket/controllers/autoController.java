package com.example.autoMarket.controllers;

import com.example.autoMarket.models.CarInfo;
import com.example.autoMarket.models.Comment;
import com.example.autoMarket.models.User;
import com.example.autoMarket.repo.CarInfoRepository;
import com.example.autoMarket.repo.CommentRepository;
import com.example.autoMarket.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class autoController {

    @Autowired
    private CarInfoRepository carInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;


    @GetMapping("/auto-catalog")
    public String indexPage(Model model, Principal principal) {
        Iterable<CarInfo> autoPosts = carInfoRepository.findAll();
        model.addAttribute("autoPosts", autoPosts);
        return "catalog-auto";
    }

    @GetMapping("/auto-catalog/{brand}")
    public String brandPage(@PathVariable(value = "brand") String brand, Model model, Principal principal) {
        List<CarInfo> autoPosts = carInfoRepository.findByBrand(brand);
        model.addAttribute("autoPosts", autoPosts);
        model.addAttribute(brand);
        if (autoPosts.isEmpty()){
            model.addAttribute("noData", true);
        }
        else{
            model.addAttribute("autoPosts", autoPosts);
        }
        return "brand-catalog";
    }

    @GetMapping("/my-autos")
    public String myAutos(Model model, Principal principal) {
        Iterable<CarInfo> autoPosts = carInfoRepository.findByUserUsername(principal.getName());
        if (carInfoRepository.findByUserUsername(principal.getName()).isEmpty()){
            model.addAttribute("noData", true);
        }
        else{
            model.addAttribute("autoPosts", autoPosts);
        }
        return "my-autos";
    }

    @GetMapping("/my-auto/{id}")
    public String myAuto(@PathVariable(value = "id") long id, Model model){
        Optional<CarInfo> carInfo = carInfoRepository.findById(id);
        List<Comment> comments = commentRepository.findByCar(carInfo.get());
        model.addAttribute("car", carInfo.get());
        model.addAttribute("user", carInfo.get().getUser());
        model.addAttribute("comments", comments);
        model.addAttribute("thisId", id);
        return "my-auto";
    }

    @GetMapping("/auto/{id}")
    public String autoDetails(@PathVariable(value = "id") long id, Model model){
        Optional<CarInfo> carInfo = carInfoRepository.findById(id);
        List<Comment> comments = commentRepository.findByCar(carInfo.get());
        model.addAttribute("car", carInfo.get());
        model.addAttribute("user", carInfo.get().getUser());
        model.addAttribute("comments", comments);
        model.addAttribute("thisId", id);
        return "auto-details";
    }

    @PostMapping("/auto/{id}")
    public String autoDetailsComments(@PathVariable(value = "id") long id, Principal principal, @RequestParam String comm, Model model){
        if (!comm.equals("")){
            User user = userRepository.findByUsername(principal.getName()).get();
            Optional<CarInfo> car = carInfoRepository.findById(id);
            Comment comment = new Comment(comm, user, car.get(), LocalDateTime.now());
            commentRepository.save(comment);
        }
        return autoDetails(id, model);
    }

    @PostMapping("/my-auto/{id}")
    public String myAutoComment(@PathVariable(value = "id") long id, Principal principal, @RequestParam String comm, Model model){
        if (!comm.equals("")){
            User user = userRepository.findByUsername(principal.getName()).get();
            Optional<CarInfo> car = carInfoRepository.findById(id);
            Comment comment = new Comment(comm, user, car.get(), LocalDateTime.now());
            commentRepository.save(comment);
        }
        return myAuto(id, model);
    }

    @DeleteMapping("/my-auto/{id}/delete")
    public String deleteAuto(@PathVariable(value = "id") long id, Model model){
        carInfoRepository.delete(carInfoRepository.findById(id).get());
        return "redirect:/my-autos";
    }

    @GetMapping("/add-car")
    public String addCarPage(Model model) {
        model.addAttribute("car", new CarInfo());
        return "addCar";
    }


    @PostMapping("/add-car")
    public String addCar(
            @RequestParam String brand,
            @RequestParam String models,
            @RequestParam double engine_volume,
            @RequestParam int horse_power,
            @RequestParam String carcase,
            @RequestParam String color,
            @RequestParam int price,
            @RequestParam int year,
            @RequestParam int mileage,
            @RequestParam String fuel_type,
            @RequestParam String transmission,
            @RequestParam String drive,
            @RequestParam String steering_wheel,
            @RequestParam String dop_info,
            @RequestParam MultipartFile photo,
            @RequestParam String vin,
            Principal principal,
            @Valid @ModelAttribute("car") CarInfo carInfo,
            BindingResult result
    ) {

        Date date = new Date(System.currentTimeMillis());
        User user = userRepository.findByUsername(principal.getName()).get();
        try {
            if (vin.length() != 17){
                result.rejectValue("vin", "", "VIN-Nummer muss 17 Zeichen lang sein");
                return "addCar";
            }
            else{
                if (!isVinValid(vin)){
                    result.rejectValue("vin", "", "UNGÜLTIG");
                    return "addCar";
                }
            }
            CarInfo car = new CarInfo(brand, models, engine_volume, horse_power, fuel_type, transmission, carcase, date, drive, color, price, year, mileage, steering_wheel, photo.getBytes(), dop_info, user, vin);
            carInfoRepository.save(car);
            return "redirect:/my-autos";
        } catch (Exception e){
            e.getMessage();
            return "index";
        }
    }

    @GetMapping("/car-update/{id}")
    public String redactCarPage(@PathVariable(value = "id") long id ,Model model) {
        model.addAttribute("car", carInfoRepository.findById(id).get());
        return "redactAuto";
    }

    @PostMapping("/car-update/{id}")
    public String redactCar(
            @PathVariable(value = "id") long id,
            @RequestParam String brand,
            @RequestParam String models,
            @RequestParam double engine_volume,
            @RequestParam int horse_power,
            @RequestParam String carcase,
            @RequestParam String color,
            @RequestParam int price,
            @RequestParam int year,
            @RequestParam int mileage,
            @RequestParam String fuel_type,
            @RequestParam String transmission,
            @RequestParam String drive,
            @RequestParam String steering_wheel,
            @RequestParam String dop_info,
            @RequestParam MultipartFile photo,
            @RequestParam String vin,
            @Valid @ModelAttribute("car") CarInfo carInfo,
            BindingResult result
    ){
        if (result.hasErrors()){
            return "redactCar";
        }
        try{
            CarInfo car = carInfoRepository.findById(id).get();
            car.setBrand(brand);
            car.setModel(models);
            car.setEngine_volume(engine_volume);
            car.setHorse_power(horse_power);
            car.setFuel_type(fuel_type);
            car.setTransmission(transmission);
            car.setDrive(drive);
            car.setColor(color);
            car.setPrice(price);
            car.setYear(year);
            car.setMileage(mileage);
            car.setSteering_wheel(steering_wheel);
            car.setCarcase(carcase);
            if (photo.getBytes().length != 0) {
                car.setImage(photo.getBytes());
            }

            car.setDopInfo(dop_info);
            if (vin.length() != 17){
                result.rejectValue("vin", "", "VIN-Nummer muss 17 Zeichen lang sein");
                return  "redactAuto";
            }
            else{
                if (!isVinValid(vin)){
                    result.rejectValue("vin", "", "UNGÜLTIG");
                    return "redactAuto";
                }
            }

            car.setVin(vin);

            carInfoRepository.save(car);
            return "redirect:/my-auto/"+ id;
        }
        catch (Exception e){
            e.getMessage();
        }

        return "redactAuto";

    }

    private boolean isVinValid(String vin) {
        int[] values = { 1, 2, 3, 4, 5, 6, 7, 8, 0, 1, 2, 3, 4, 5, 0, 7, 0, 9,
                2, 3, 4, 5, 6, 7, 8, 9 };
        int[] weights = { 8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2 };


        String s = vin;
        s = s.replaceAll("-", "");
        s = s.replaceAll(" ", "");
        s = s.toUpperCase();

        int sum = 0;
        for (int i = 0; i < 17; i++) {
            char c = s.charAt(i);
            int value;
            int weight = weights[i];

            // letter
            if (c >= 'A' && c <= 'Z') {
                value = values[c - 'A'];
                if (value == 0)
                    value = 0;
            }

            // number
            else if (c >= '0' && c <= '9')
                value = c - '0';

            // illegal character
            else value = 0;

            sum = sum + weight * value;

        }


        sum = sum % 11;
        char check = s.charAt(8);
        if (sum == 10 && check == 'X') {
            return true;
        } else if (sum == transliterate(check)) {
            return true;
        } else {
            return false;
        }

    }

    private int transliterate(char check){
        if(check == 'A' || check == 'J'){
            return 1;
        } else if(check == 'B' || check == 'K' || check == 'S'){
            return 2;
        } else if(check == 'C' || check == 'L' || check == 'T'){
            return 3;
        } else if(check == 'D' || check == 'M' || check == 'U'){
            return 4;
        } else if(check == 'E' || check == 'N' || check == 'V'){
            return 5;
        } else if(check == 'F' || check == 'W'){
            return 6;
        } else if(check == 'G' || check == 'P' || check == 'X'){
            return 7;
        } else if(check == 'H' || check == 'Y'){
            return 8;
        } else if(check == 'R' || check == 'Z'){
            return 9;
        } else if(Integer.valueOf(Character.getNumericValue(check)) != null){
            return Character.getNumericValue(check);
        }
        return -1;
    }

}