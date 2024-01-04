package com.lavendor.mealmate.controller;

import com.lavendor.mealmate.model.DailyMenu;
import com.lavendor.mealmate.service.DailyMenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/daily-menu")
public class DailyMenuController {

    private final DailyMenuService dailyMenuService;

    public DailyMenuController(DailyMenuService dailyMenuService) {
        this.dailyMenuService = dailyMenuService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<DailyMenu>> getAllDailyMenus(){
        List<DailyMenu> dailyMenus = dailyMenuService.getAllDailyMenu();

        if(dailyMenus != null && !dailyMenus.isEmpty()){
            return ResponseEntity.ok(dailyMenus);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<DailyMenu> getDailyMenuById(@PathVariable("id") String stringId){

        Long dailyMenuId = Long.valueOf(stringId);
        DailyMenu dailyMenu = dailyMenuService.getDailyMenuById(dailyMenuId);
        return ResponseEntity.ok(dailyMenu);
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<String> deleteDailyMenu(@PathVariable("id") String stringId){
        Long dailyMenuId = Long.valueOf(stringId);
        dailyMenuService.deleteDailyMenu(dailyMenuId);
        return ResponseEntity.ok("Daily Menu with ID: " + dailyMenuId + " deleted successfully");
    }

}
