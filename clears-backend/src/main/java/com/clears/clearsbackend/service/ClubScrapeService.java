package com.clears.clearsbackend.service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class ClubScrapeService {

    @Async
    //@Scheduled(fixedRate = 6000000)
    public void loadClubs() {
        String[] ids = {"22", "46", "15542", "29", "1061", "131", "330", "496", "3958", "210", "1114", "610", "336", "2464", "656", "4961", "69261", "614", "418", "18544", "2462", "2440", "9251", "583", "221", "5", "368", "720", "985", "506"};

        RestClient restClient = RestClient.create();

        for (int i = 0; i < ids.length; i++) {
            String result = restClient.get()
                .uri("http://localhost:8000/clubs/" + ids[i] + "/profile")
                .retrieve()
                .body(String.class);
            
            System.out.println(result + "\n");
        }
    }
    
}
