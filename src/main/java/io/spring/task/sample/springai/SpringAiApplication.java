package io.spring.task.sample.springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@SpringBootApplication
public class SpringAiApplication {

    @Value("${input.file.path}")
    private String filePath;

    public static void main(String[] args) {
        SpringApplication.run(SpringAiApplication.class, args);
    }

    @Bean
    ChatClient client(ChatClient.Builder builder){

        var systemPrompt = """
                You are an AI powered assistant to help people migrate code from JSF to Thymeleaf.
                 If the user asks anything else, return a disappointed response suggesting that you
                 do not know the answer.
                """;

        return builder
                .defaultSystem(systemPrompt)
                .build();
    }

    @Bean
    ApplicationRunner codeRefactorRunner(ChatClient chatClient){

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

        return args -> {
            var content = chatClient
                    .prompt("Convert the following JSF code to Thymeleaf code: " + fileDataBuilder)
                    .call()
                    .content();

            System.out.println("Response: ");
            System.out.println(content);

        };
    }

}


