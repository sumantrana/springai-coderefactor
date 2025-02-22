package io.spring.task.sample.springai.vet;

import io.spring.task.sample.springai.adoptions.DogAdoptionEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
class Dogtor {

    @ApplicationModuleListener
    //@EventListener
    void checkup(DogAdoptionEvent dogAdoptionEvent) throws Exception{
        Thread.sleep(5_000);
        System.out.println("Checking up [" + dogAdoptionEvent + "]");
    }
}
