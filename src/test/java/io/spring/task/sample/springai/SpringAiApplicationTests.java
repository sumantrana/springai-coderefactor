package io.spring.task.sample.springai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModule;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

@SpringBootTest
class SpringAiApplicationTests {

    @Test
    void contextLoads() {
        var appModules = ApplicationModules.of(SpringAiApplication.class);
        System.out.println(appModules);

        appModules.verify();

        Documenter documenter = new Documenter(appModules);
        documenter.writeDocumentation();
    }

}
