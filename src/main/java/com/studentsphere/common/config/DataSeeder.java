package com.studentsphere.common.config;

import com.studentsphere.master.entity.City;
import com.studentsphere.master.entity.College;
import com.studentsphere.master.entity.State;
import com.studentsphere.master.repository.CityRepository;
import com.studentsphere.master.repository.CollegeRepository;
import com.studentsphere.master.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CollegeRepository collegeRepository;

    @Override
    public void run(String... args) throws Exception {
        if (stateRepository.count() == 0) {
            // Create State
            State california = new State();
            california.setName("California");
            stateRepository.save(california);

            State newYork = new State();
            newYork.setName("New York");
            stateRepository.save(newYork);

            // Create Cities
            City losAngeles = new City();
            losAngeles.setName("Los Angeles");
            losAngeles.setState(california);
            cityRepository.save(losAngeles);

            City sanFrancisco = new City();
            sanFrancisco.setName("San Francisco");
            sanFrancisco.setState(california);
            cityRepository.save(sanFrancisco);

            City nyc = new City();
            nyc.setName("New York City");
            nyc.setState(newYork);
            cityRepository.save(nyc);

            // Create Colleges
            College ucla = new College();
            ucla.setName("University of California, Los Angeles");
            ucla.setDomain("ucla.edu");
            ucla.setCity(losAngeles);
            collegeRepository.save(ucla);

            College usc = new College();
            usc.setName("University of Southern California");
            usc.setDomain("usc.edu");
            usc.setCity(losAngeles);
            collegeRepository.save(usc);

            College nyu = new College();
            nyu.setName("New York University");
            nyu.setDomain("nyu.edu");
            nyu.setCity(nyc);
            collegeRepository.save(nyu);
            
            System.out.println("Sample data seeded successfully!");
        }
    }
}
