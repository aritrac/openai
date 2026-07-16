package com.binarylife.openai.controller;

import com.binarylife.openai.model.CountryCities;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StructuredOutputController {
    private final ChatClient chatClient;

    public StructuredOutputController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping("chat-bean")
    public ResponseEntity<CountryCities> chatBean(@RequestParam("message") String message) {
        CountryCities countryCities = chatClient
                .prompt()
                .user(message)
                .call()
                .entity(CountryCities.class); //but this way it is much smaller
                //.entity(new BeanOutputConverter<>(CountryCities.class)); We can use this way as well to convert the output to a formatted bean response
        return ResponseEntity.ok(countryCities);
    }

    @GetMapping("chat-list")
    public ResponseEntity<List<String>> chatList(@RequestParam("message") String message) {
        List<String> listOfCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new ListOutputConverter());
        return ResponseEntity.ok(listOfCities);
    }

    @GetMapping("chat-map")
    public ResponseEntity<Map<String, Object>> chatMap(@RequestParam("message") String message) {
        Map<String, Object> mapOfCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new MapOutputConverter());
        return ResponseEntity.ok(mapOfCities);
    }

    @GetMapping("chat-bean-list")
    public ResponseEntity<List<CountryCities>> chatBeanList(@RequestParam("message") String message) {
        List<CountryCities> listOfCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new ParameterizedTypeReference<List<CountryCities>>() { //to get a list of custom objects and also to preserve bean class info
                    //in generics during the runtime
                });
        return ResponseEntity.ok(listOfCities);
    }
}
