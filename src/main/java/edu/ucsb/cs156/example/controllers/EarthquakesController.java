package edu.ucsb.cs156.example.controllers;

import org.springframework.web.bind.annotation.RestController;

import edu.ucsb.cs156.example.services.EarthquakeQueryService;
import edu.ucsb.cs156.example.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(description="Earthquake info from USGS")
@Slf4j
@RestController
@RequestMapping("/api/earthquakes")
public class EarthquakesController {
    
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    UserRepository userRepository;

    @Autowired
    EarthquakeQueryService earthquakeQueryService;

    @ApiOperation(value = "Get earthquakes a certain distance from UCSB's Storke Tower", notes = "JSON return format documented here: https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php")
    @GetMapping("/get")
    public ResponseEntity<String> getEarthquakes(
        @ApiParam("distance in km, e.g. 100") @RequestParam String distance,
        @ApiParam("minimum magnitude, e.g. 2.5") @RequestParam String minMag
    ) throws JsonProcessingException {
        log.info("getEarthquakes: distance={} minMag={}", distance, minMag);
        String result = earthquakeQueryService.getJSON(distance, minMag);
        return ResponseEntity.ok().body(result);
    }

}
