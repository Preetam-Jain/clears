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
public class Club {

    @JsonProperty
    private String URL;

    @JsonProperty
    private String name;

    @JsonProperty
    private String imageURL;

    @JsonProperty
    private String stadium;

    @JsonProperty
    private String foundingDate;

    
}
