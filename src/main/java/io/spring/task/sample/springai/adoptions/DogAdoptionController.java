package io.spring.task.sample.springai.adoptions;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

interface DogRepository extends CrudRepository<Dog, Integer> {}

record Dog (@Id Integer id, String name, String description, String owner){}

//@RestController
public class DogAdoptionController {

    private final DogRepository dogRepository;
    private final ApplicationEventPublisher eventPublisher;


    public DogAdoptionController(DogRepository dogRepository, ApplicationEventPublisher eventPublisher){
        this.dogRepository = dogRepository;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/dogs/{id}/adoption")
    //@Transactional
    public void adoptDog(@PathVariable int id, @RequestBody Map<String, String> owner ){
        dogRepository.findById(id)
                .ifPresent( dog -> {
                    var newDog = dogRepository.save(new Dog(dog.id(), dog.name(), dog.description(), owner.get("name")));
                    this.eventPublisher.publishEvent(new DogAdoptionEvent(dog.id()));
                    System.out.println("adopted [" + newDog + "]");
                });

    }
}
