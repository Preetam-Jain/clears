package com.clears.clearsbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.clears.clearsbackend.model.PlayerStats;
import com.clears.clearsbackend.model.PlayerStatistic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@Service
public class PlayerScrapeService {

    @Autowired
    ClubScrapeService clubScrapeService;

    @Async
    //@Scheduled(fixedRate = 6000000)
    public void loadTransfermarktPlayers() {
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

        HashMap<String, ArrayList<String>> clubIDs = new HashMap<>();
        HashSet<String> allClubs = new HashSet();

        for (int i = 0; i < players.size(); i++) {
            String result = restClient.get()
                .uri("http://localhost:8000/players/search/" + players.get(i))
                .retrieve()
                .body(String.class);

            String stringID = getStringFromResult(result, "id");
            String name = getStringFromResult(result, "name");

            System.out.println(name);

            result = restClient.get()
                .uri("http://localhost:8000/players/" +  stringID + "/profile")
                .retrieve()
                .body(String.class);

            //System.out.println("Profile: " + result);

            PlayerStats playerStat = restClient.get()
                .uri("http://localhost:8000/players/" +  stringID + "/stats")
                .retrieve()
                .body(PlayerStats.class);

            List<PlayerStatistic> stats = playerStat.getStats();
            ArrayList<String> clubIDList = new ArrayList<>();

            for (PlayerStatistic stat : stats) {
                clubIDList.add(stat.getClubId());
                allClubs.add(stat.getClubId());
                //System.out.println(stat);
            }

            clubIDs.put(name, clubIDList);
        }
        //System.out.println(allClubs);        
    }

    @Async
    @Scheduled(fixedRate = 600000)
    public void loadSportsmonkPlayers() {
        List<String> responses = new ArrayList<>();

        try (InputStream inputStream = PlayerScrapeService.class.getClassLoader().getResourceAsStream("responses.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                responses.add(line);
        }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        for (String repsonse : responses) {
            System.out.println(repsonse);
        }
                
        RestClient restClient = RestClient.create();
        String filePath = "IdCheck.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (String response : responses) {

                String ID = getStringFromSportsmonkResult(response, "id");
                String result = restClient.get()
                        .uri("https://api.sportmonks.com/v3/football/players/" + ID + "?api_token=8nmBzeo1ygkopFiwvXw5WCOgv9jbTOOMsVJsc9umZJ8x76oSZxzPJoph3bMS")
                        .retrieve()
                        .body(String.class);
                //System.out.println(result);
                writer.write(getStringFromResult(result, "name") + ": " + result);
                writer.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }

    private static String getStringFromResult(String result, String searchString) {
        int index = result.indexOf("\"" + searchString + "\"");
        int startIndex = result.indexOf(": \"", index);
        int endIndex = result.indexOf("\",", index);
        return result.substring(startIndex + 3, endIndex);
    }

    private static String getStringFromSportsmonkResult(String result, String searchString) {
        int index = result.indexOf("\"" + searchString + "\"");
        int startIndex = result.indexOf(":", index) + 1;
        int endIndex = result.indexOf(",", index);
        return result.substring(startIndex, endIndex);
    }
}
