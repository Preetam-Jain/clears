package com.clears.clearsbackend.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerStatistic {

    @JsonProperty("competitionID")
    private String competitionID;

    @JsonProperty("clubID")
    private String clubId;

    @JsonProperty("seasonID")
    private String seasonId;

    @JsonProperty("competitionName")
    private String competitionName;

    @JsonProperty("appearances")
    private int appearances;

    @JsonProperty("goals")
    private int goals;

    @JsonProperty("assists")
    private int assists;

    @JsonProperty("minutesPlayed")
    private String minutesPlayed; // This is a String because of the apostrophe
    
}
