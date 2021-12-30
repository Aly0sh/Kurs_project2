package com.example.autoMarket.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String text;


    @ManyToOne
    private User user;

    @ManyToOne
    private CarInfo car;

    @Column(name="date", columnDefinition="DATETIME")
    private LocalDateTime date;

    public Comment(){}

    public Comment(String text, User user, CarInfo car, LocalDateTime date){
        this.text = text;
        this.user = user;
        this.car = car;
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CarInfo getCar() {
        return car;
    }

    public void setCar(CarInfo car) {
        this.car = car;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
