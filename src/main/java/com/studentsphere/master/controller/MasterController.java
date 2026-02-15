package com.studentsphere.master.controller;

import com.studentsphere.master.entity.City;
import com.studentsphere.master.entity.College;
import com.studentsphere.master.entity.State;
import com.studentsphere.master.service.MasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master")
public class MasterController {

    @Autowired
    private MasterService masterService;

    @GetMapping("/states")
    public ResponseEntity<List<State>> getAllStates() {
        return ResponseEntity.ok(masterService.getAllStates());
    }

    @GetMapping("/cities/{stateId}")
    public ResponseEntity<List<City>> getCitiesByState(@PathVariable Long stateId) {
        return ResponseEntity.ok(masterService.getCitiesByState(stateId));
    }

    @GetMapping("/colleges/{cityId}")
    public ResponseEntity<List<College>> getCollegesByCity(@PathVariable Long cityId) {
        return ResponseEntity.ok(masterService.getCollegesByCity(cityId));
    }
}
