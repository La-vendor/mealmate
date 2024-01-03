package com.lavendor.mealmate.repository;

import com.lavendor.mealmate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
