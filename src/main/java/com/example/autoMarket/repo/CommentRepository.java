package com.example.autoMarket.repo;

import com.example.autoMarket.models.CarInfo;
import com.example.autoMarket.models.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository  extends JpaRepository<Comment, Long> {
    List<Comment> findByCar(CarInfo car);
}
