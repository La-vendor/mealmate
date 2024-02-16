package com.lavendor.mealmate.dailymenu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyMenuRepository extends JpaRepository<DailyMenu, Long> {

    Optional<DailyMenu> findByDate(LocalDate date);
    List<DailyMenu> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
