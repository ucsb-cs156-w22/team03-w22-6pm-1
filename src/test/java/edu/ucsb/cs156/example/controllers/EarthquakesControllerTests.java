package edu.ucsb.cs156.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import edu.ucsb.cs156.example.collections.FeatureCollection;
import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.services.EarthquakeQueryService;

import static org.mockito.ArgumentMatchers.eq;

import org.springframework.security.test.context.support.WithMockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(value = EarthquakesController.class)
public class EarthquakesControllerTests {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    EarthquakeQueryService mockEarthquakeQueryService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    FeatureCollection featureCollection;

    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void test_getEarthquakes_admin() throws Exception {
        String fakeJsonResult="{ \"fake\" : \"result\" }";
        String distance = "100";
        String minMag = "1.5";
        when(mockEarthquakeQueryService.getJSON(eq(distance),eq(minMag))).thenReturn(fakeJsonResult);

        String url = String.format("/api/earthquakes/retrieve?distanceKm=%s&minMagnitude=%s",distance,minMag);

        MvcResult response = mockMvc
            .perform( get(url).contentType("application/json"))
            .andExpect(status().isOk()).andReturn();

        String responseString = response.getResponse().getContentAsString();

        assertEquals(fakeJsonResult, responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void test_getEarthquakes_user() throws Exception {
        String fakeJsonResult="{ \"fake\" : \"result\" }";
        String distance = "100";
        String minMag = "1.5";
        when(mockEarthquakeQueryService.getJSON(eq(distance),eq(minMag))).thenReturn(fakeJsonResult);
        
        String url = String.format("/api/earthquakes/retrieve?distanceKm=%s&minMagnitude=%s",distance,minMag);
        mockMvc.perform(get(url))
                                .andExpect(status().is(403));
    }

    @Test
    public void test_getEarthquakes_noauth() throws Exception {
        String fakeJsonResult="{ \"fake\" : \"result\" }";
        String distance = "100";
        String minMag = "1.5";
        when(mockEarthquakeQueryService.getJSON(eq(distance),eq(minMag))).thenReturn(fakeJsonResult);
        
        String url = String.format("/api/earthquakes/retrieve?distanceKm=%s&minMagnitude=%s",distance,minMag);
        mockMvc.perform(get(url))
                                .andExpect(status().is(403));
    }
}