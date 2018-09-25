package com.savik.service.bookmaker.matchbook;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchbookNavigationEntry {

    String name;
    
    Long id;
    
    String type; // enum
    
    @JsonProperty("url-name")
    String url;
    
    @JsonProperty("meta-tags")
    List<MatchbookNavigationEntry> leagues;
}
