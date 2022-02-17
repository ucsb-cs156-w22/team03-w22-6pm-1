package edu.ucsb.cs156.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;

@RestClientTest(EarthquakeQueryService.class)
public class EarthquakeQueryServiceTests {

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private EarthquakeQueryService earthquakeQueryService;

    @Test
    public void test_getJSON() {

        String distance = "10";
        String minMag = "1.5";
        String ucsbLat = "34.4140"; // hard coded params for Storke Tower
        String ucsbLong = "-119.8489";
        String expectedURL = EarthquakeQueryService.ENDPOINT.replace("{distance}", distance)
                .replace("{minMag}", minMag).replace("{latitude}", ucsbLat).replace("{longitude}", ucsbLong);

        String fakeJsonResult = "{ \"fake\" : \"result\" }";

        this.mockRestServiceServer.expect(requestTo(expectedURL))
                .andExpect(header("Accept", MediaType.APPLICATION_JSON.toString()))
                .andExpect(header("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andRespond(withSuccess(fakeJsonResult, MediaType.APPLICATION_JSON));

        String actualResult = earthquakeQueryService.getJSON(distance, minMag);
        assertEquals(fakeJsonResult, actualResult);
    }
}
