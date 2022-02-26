package edu.ucsb.cs156.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import edu.ucsb.cs156.example.collections.FeatureCollection;
import edu.ucsb.cs156.example.documents.Feature;
import edu.ucsb.cs156.example.documents.FeatureGeometry;
import edu.ucsb.cs156.example.documents.FeatureProperties;
import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.services.EarthquakeQueryService;

import static org.mockito.ArgumentMatchers.eq;

import org.springframework.security.test.context.support.WithMockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import java.util.ArrayList;
import java.util.List;


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

    public Feature createTestFeature(){
        List<Float> coords = new ArrayList<Float>();
        coords.add(1.1f);
        coords.add(1.2f);
        coords.add(1.3f);
        FeatureProperties testProps = FeatureProperties.builder()
            .mag(1.1f)
            .place("Test")
            .time(123)
            .updated(123)
            .tz(123)
            .url("Test")
            .detail("Test")
            .felt(123)
            .cdi(1.1f)
            .mmi(1.1f)
            .alert("Test")
            .status("Test")
            .tsunami(123)
            .sig(123)
            .net("Test")
            .code("Test")
            .ids("Test")
            .sources("Test")
            .types("Test")
            .nst(123)
            .dmin(1.1f)
            .rms(1.1f)
            .gap(1.1f)
            .magType("Test")
            .type("Test")
            .build();
        FeatureGeometry testGeom = FeatureGeometry.builder().coordinates(coords).build();
        Feature testFeature = Feature.builder().geometry(testGeom).properties(testProps).build();
        return testFeature;
    }

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

    // Test the /all endpoint

    @WithMockUser(roles = { "ADMIN", "USER" })
    @Test
    public void test_allEarthquakes_admin() throws Exception {
        List<Feature> fakeResult = new ArrayList<>();
        when(featureCollection.findAll()).thenReturn(fakeResult);

        String url = "/api/earthquakes/all";

        MvcResult response = mockMvc
            .perform( get(url).contentType("application/json"))
            .andExpect(status().isOk()).andReturn();

        String responseString = response.getResponse().getContentAsString();

        assertEquals("[]", responseString);
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void test_allEarthquakes_user() throws Exception {
        List<Feature> fakeResult = new ArrayList<>();
        when(featureCollection.findAll()).thenReturn(fakeResult);

        String url = "/api/earthquakes/all";

        MvcResult response = mockMvc
            .perform( get(url).contentType("application/json"))
            .andExpect(status().isOk()).andReturn();

        String responseString = response.getResponse().getContentAsString();

        assertEquals("[]", responseString);
    }

    @Test
    public void test_allEarthquakes_noauth() throws Exception {
        
        String url = "/api/earthquakes/all";
        mockMvc.perform(get(url))
                                .andExpect(status().is(403));
    }


    // Test the purge endpoints
    @WithMockUser(roles = { "ADMIN" })
    @Test
    public void test_purgeEarthquakes_admin() throws Exception {
        Feature test = createTestFeature();

        featureCollection.save(test);

        String url = "/api/earthquakes/purge";

        MvcResult response = mockMvc
            .perform( post(url).with(csrf()).contentType("application/json"))
            .andExpect(status().isOk()).andReturn();

        String responseString = response.getResponse().getContentAsString();

        assertEquals("Earthquakes collection cleaned", responseString);
        assertEquals(0, featureCollection.count());
    }

    @WithMockUser(roles = { "USER" })
    @Test
    public void test_purgeEarthquakes_user() throws Exception {
        String url = "/api/earthquakes/purge";
        mockMvc.perform(post(url))
                                .andExpect(status().is(403));
    }

    @Test
    public void test_purgeEarthquakes_noauth() throws Exception {
        String url = "/api/earthquakes/purge";
        mockMvc.perform(post(url))
                                .andExpect(status().is(403));
    }
}