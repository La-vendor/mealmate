package com.lavendor.mealmate.repository;

import com.lavendor.mealmate.model.DailyMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyMenuRepository extends JpaRepository<DailyMenu, Long> {

    Optional<DailyMenu> findByDate(LocalDate date);
    List<DailyMenu> findByUserId(Long userId);

}
