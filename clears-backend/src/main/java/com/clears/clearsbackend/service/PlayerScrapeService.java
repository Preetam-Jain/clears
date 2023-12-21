package com.clears.clearsbackend.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

@Service
public class PlayerScrapeService {

    @Async
    @Scheduled(fixedRate = 300000)
    public void loadPlayers() {
        List<String> players = new ArrayList<>();

        try (InputStream inputStream = PlayerScrapeService.class.getClassLoader().getResourceAsStream("players.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                players.add(line);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        RestClient restClient = RestClient.create();

        List<String> results = new ArrayList<>();

        for (String player : players ) {
            String result = restClient.get()
                .uri("http://localhost:8000/players/search/" + player)
                .retrieve()
                .body(String.class);
            System.out.println(result);
            results.add(result);
        }
    }

}
