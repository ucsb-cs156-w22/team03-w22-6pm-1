package edu.ucsb.cs156.example.controllers;

import org.springframework.web.bind.annotation.RestController;

import edu.ucsb.cs156.example.services.EarthquakeQueryService;
import edu.ucsb.cs156.example.collections.FeatureCollection;
import edu.ucsb.cs156.example.documents.Feature;
import edu.ucsb.cs156.example.documents.GeoJSON;
import edu.ucsb.cs156.example.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

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
    FeatureCollection featureCollection;

    @Autowired
    EarthquakeQueryService earthquakeQueryService;

    @ApiOperation(value = "Get earthquakes a certain distance from UCSB's Storke Tower and store the update in the MongoDB collection earthquakes", notes = "JSON return format documented here: https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/retrieve")
    public ResponseEntity<String> getEarthquakes(
            @ApiParam("distance in km, e.g. 100") @RequestParam String distanceKm,
            @ApiParam("minimum magnitude, e.g. 2.5") @RequestParam String minMagnitude
        ) throws JsonProcessingException {
        log.info("getEarthquakes: distance={} minMag={}", distanceKm, minMagnitude);
        String result = earthquakeQueryService.getJSON(distanceKm, minMagnitude);
        // Convert each earthquake to a feature object
        GeoJSON json = mapper.readValue(result, GeoJSON.class);
        List<Feature> features = json.getFeatures();
        // Save each earthquake TO the collection
        featureCollection.saveAll(features);
        return ResponseEntity.ok().body(result);
    }

    @ApiOperation(value = "List all of the earthquakes in the database", notes = "JSON return format documented here: https://earthquake.usgs.gov/earthquakes/feed/v1.0/geojson.php")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    public ResponseEntity<List<Feature>> listEarthquakes() {
        log.info("listEarthquakes");
        return ResponseEntity.ok().body(featureCollection.findAll());
    }

    @ApiOperation(value = "Delete all of the earthquakes in the database")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/purge")
    public ResponseEntity<String> purgeEarthquakes() {
        log.info("purgeEarthquakes");
        featureCollection.deleteAll();
        return ResponseEntity.ok().body("Earthquakes collection cleaned");
    }
}
