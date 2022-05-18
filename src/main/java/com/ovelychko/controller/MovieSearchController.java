package com.ovelychko.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/search")
public class MovieSearchController {

    private static final String OMDBAPI_KEY_NAME = "apikey";
    private static final String OMDBAPI_KEY_VALUE = "1ac1214b";

    @GetMapping
    public ResponseEntity getOmdbSearch(@RequestParam Map<String, String> requestParamMap,
                                        Principal principal) {
        log.info("getOmdbSearch called with: " + requestParamMap);
        log.info("getOmdbSearch user auth: " + principal);

        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme("https").host("www.omdbapi.com").queryParam(OMDBAPI_KEY_NAME, OMDBAPI_KEY_VALUE);

        requestParamMap.forEach(uriComponentsBuilder::queryParam);

        return restTemplate.getForEntity(uriComponentsBuilder.toUriString(), String.class);
    }
}
