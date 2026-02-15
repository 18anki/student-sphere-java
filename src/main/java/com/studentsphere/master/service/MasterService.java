package com.studentsphere.master.service;

import com.studentsphere.master.entity.City;
import com.studentsphere.master.entity.College;
import com.studentsphere.master.entity.State;
import com.studentsphere.master.repository.CityRepository;
import com.studentsphere.master.repository.CollegeRepository;
import com.studentsphere.master.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterService {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    public List<State> getAllStates() {
        return stateRepository.findAll();
    }

    public List<City> getCitiesByState(Long stateId) {
        return cityRepository.findByStateId(stateId);
    }

    public List<College> getCollegesByCity(Long cityId) {
        return collegeRepository.findByCityId(cityId);
    }
}
