package com.example.autoMarket.models;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;


@Entity
@Table(name = "carInfo")
public class CarInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String brand;
    private String model;
    private double engine_volume;
    private int horse_power;
    private String fuel_type;
    private String transmission;
    private String carcase;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "date")
    private Date date;

    @OneToMany(
            mappedBy = "car",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Comment> comments;

    private String drive;
    private String color;
    private int price;
    private int year;
    private int mileage;
    private String steering_wheel;

    @Column(name="image", columnDefinition="LONGBLOB")
    private byte[] image;
    private String dopInfo;

    private String vin;

    public CarInfo() {

    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getDopInfo() {
        return dopInfo;
    }

    public void setDopInfo(String dopInfo) {
        this.dopInfo = dopInfo;
    }

    @ManyToOne
    private User user;

    public CarInfo(String brand, String model, double engine_volume, int horse_power, String fuel_type, String transmission, String carcase, Date date, String drive, String color, int price, int year, int mileage, String steering_wheel, byte[] image, String dopInfo, User user, String vin) {
        this.brand = brand;
        this.model = model;
        this.engine_volume = engine_volume;
        this.horse_power = horse_power;
        this.fuel_type = fuel_type;
        this.transmission = transmission;
        this.carcase = carcase;
        this.date = date;
        this.drive = drive;
        this.color = color;
        this.price = price;
        this.year = year;
        this.mileage = mileage;
        this.steering_wheel = steering_wheel;
        this.image = image;
        this.dopInfo = dopInfo;
        this.user = user;
        this.vin = vin;
    }

    public CarInfo(String brand, String model, double engine_volume, int horse_power, String fuel_type, String transmission, String carcase, String drive, String color, int price, int year, int mileage, String steering_wheel, byte[] image, String dopInfo, User user, String vin) {
        this.brand = brand;
        this.model = model;
        this.engine_volume = engine_volume;
        this.horse_power = horse_power;
        this.fuel_type = fuel_type;
        this.transmission = transmission;
        this.carcase = carcase;
        this.drive = drive;
        this.color = color;
        this.price = price;
        this.year = year;
        this.mileage = mileage;
        this.steering_wheel = steering_wheel;
        if (image != null){
            this.image = image;
        }
        this.dopInfo = dopInfo;
        this.user = user;
        this.vin = vin;
    }

    public String getSteering_wheel() {
        return steering_wheel;
    }

    public void setSteering_wheel(String steering_wheel) {
        this.steering_wheel = steering_wheel;
    }

    public String generateBase64Image() {
        return Base64.encodeBase64String(this.getImage());
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getEngine_volume() {
        return engine_volume;
    }

    public void setEngine_volume(double engine_volume) {
        this.engine_volume = engine_volume;
    }

    public int getHorse_power() {
        return horse_power;
    }

    public void setHorse_power(int horse_power) {
        this.horse_power = horse_power;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getCarcase() {
        return carcase;
    }

    public void setCarcase(String carcase) {
        this.carcase = carcase;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
