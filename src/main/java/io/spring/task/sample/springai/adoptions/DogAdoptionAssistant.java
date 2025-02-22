package io.spring.task.sample.springai.adoptions;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RegisterReflectionForBinding(DogAdoptionSuggestion.class)
@Configuration
public class DogAdoptionAssistant {

    @Bean
    ChatClient client(ChatClient.Builder builder, DogRepository dogRepository, VectorStore vectorStore){

        var systemPrompt = """
                You are an AI powered assistant to help people adopt a dog from the adoption
                 agency named Spring's Pet Emporium with locations in Barcelona, Spain, and
                 Madrid, Spain. If you don't know about the dogs housed at our particular
                 stores, then return a disappointed response suggesting we don't have anything.
                """;
        return builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .defaultSystem(systemPrompt)
                .build();
    }

    @Bean
    ApplicationRunner demoApplicationRunner(ChatClient client,
                                            DogRepository dogRepository,
                                            VectorStore vectorStore){

//        dogRepository.findAll().forEach( dog -> {
//                    var dogument = new Document("name: " + dog.name() + ", description: " + dog.description(), Map.of("dogId", dog.id()));
//                    System.out.println("Processing dog %s: %s: %s".formatted(dog.id(), dog.name(), dog.description()));
//                    vectorStore.add(List.of(dogument));
//                }
//        );

        return args -> {
            var content = client
                    .prompt("Do you have any neurotic dogs?")
                    .call()
                    .entity(DogAdoptionSuggestion.class);
            System.out.println("Content [" + content + "]");
        };
    }
}

record DogAdoptionSuggestion(int dogId, String name, String description){}
