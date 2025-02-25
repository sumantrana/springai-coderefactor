package io.spring.task.sample.springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

@Configuration
public class RAGConfiguration {

    @Value("${input.file.path}")
    private String filePath;

    @Bean
    ChatClient client(ChatClient.Builder builder, VectorStore vectorStore){

        var systemPrompt = """
                You are an AI powered assistant to help people migrate code from JSF to Thymeleaf.
                 If the user asks anything else, return a disappointed response suggesting that you
                 do not know the answer.
                """;

        return builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
                .defaultSystem(systemPrompt)
                .build();
    }

    @Bean
    ApplicationRunner codeRefactorRunner(ChatClient chatClient, VectorStore vectorStore){

        var jsfCode = """
                                <h:panelGrid columns="2">
                                </h:panelGrid>
                """;


        var thymeCode = """
                    <div class="grid-container">
                    </div>
                """;

        var jsfToThymeHintDocument = new Document("jsfCode:" + jsfCode + ", thymeCode: " + thymeCode);
        vectorStore.add(List.of(jsfToThymeHintDocument));

        File file = new File(filePath);
        StringBuilder fileDataBuilder = new StringBuilder();

        try {
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                fileDataBuilder.append(scanner.nextLine());
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String fileData = fileDataBuilder.toString().replace("{", "\\{").replace("}", "\\}");

        return args -> {
            var content = chatClient
                    .prompt("Convert the following JSF code to Thymeleaf code: " + fileData)
                    .call()
                    .content();

//            var content = chatClient.prompt("What is the time?")
//                            .call()
//                            .content();

            System.out.println("Response: ");
            System.out.println(content);

        };
    }

}
