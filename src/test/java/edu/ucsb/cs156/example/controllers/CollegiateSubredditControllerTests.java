package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.CollegiateSubreddit;
import edu.ucsb.cs156.example.repositories.CollegiateSubredditRepository;
import edu.ucsb.cs156.example.controllers.CollegiateSubredditController;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = CollegiateSubredditController.class)
@Import(TestConfig.class)
public class CollegiateSubredditControllerTests extends ControllerTestCase {

    @MockBean
    CollegiateSubredditRepository collegiateSubredditRepository;

    @MockBean
    UserRepository userRepository;

    // Test /all endpoint

    @Test
    public void api_all() throws Exception {

        // arrange

        CollegiateSubreddit collegiateSubreddit1 = CollegiateSubreddit.builder().name("College 1").location("Iceland")
                .subreddit("icelandu").id(1L).build();
        CollegiateSubreddit collegiateSubreddit2 = CollegiateSubreddit.builder().name("College 2")
                .location("Czech Republic").subreddit("czechu").id(2L).build();
        CollegiateSubreddit collegiateSubreddit3 = CollegiateSubreddit.builder().name("College 3").location("Norway")
                .subreddit("Norwayu").id(3L).build();

        ArrayList<CollegiateSubreddit> expectedCollegiateSubreddits = new ArrayList<>();
        expectedCollegiateSubreddits
                .addAll(Arrays.asList(collegiateSubreddit1, collegiateSubreddit2, collegiateSubreddit3));

        when(collegiateSubredditRepository.findAll()).thenReturn(expectedCollegiateSubreddits);

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits/all"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(collegiateSubredditRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddits);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @Test
    public void api_all_with_no_subreddits() throws Exception {

        // arrange
        ArrayList<CollegiateSubreddit> expectedCollegiateSubreddits = new ArrayList<>();

        when(collegiateSubredditRepository.findAll()).thenReturn(expectedCollegiateSubreddits);

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits/all"))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddits);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    // Test /post endpoint

    @Test
    public void api_post() throws Exception {
        // arrange

        CollegiateSubreddit expectedCollegiateSubreddit = CollegiateSubreddit.builder().name("College 1")
                .location("Iceland").subreddit("icelandu").id(0L).build();

        when(collegiateSubredditRepository.save(eq(expectedCollegiateSubreddit)))
                .thenReturn(expectedCollegiateSubreddit);

        // act
        MvcResult response = mockMvc.perform(
                post("/api/collegiateSubreddits/post?name=College 1&location=Iceland&subreddit=icelandu")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).save(expectedCollegiateSubreddit);
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddit);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    // Test api /get with id parameter endpoint
    @Test
    public void api_get_id_returns_a_subreddit_that_exists() throws Exception {

        // arrange
        CollegiateSubreddit expectedCollegiateSubreddit = CollegiateSubreddit.builder().name("College 1")
                .location("Iceland").subreddit("icelandu").id(1L).build();
        when(collegiateSubredditRepository.findById(eq(1L))).thenReturn(Optional.of(expectedCollegiateSubreddit));

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits?id=1"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(collegiateSubredditRepository, times(1)).findById(eq(1L));
        String expectedJson = mapper.writeValueAsString(expectedCollegiateSubreddit);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    // Test api /get Collegiate Subreddit id that doesn't exist
    @Test
    public void api_get_id_returns_a_subreddit_that_does_not_exist() throws Exception {

        // arrange
        when(collegiateSubredditRepository.findById(eq(7L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/collegiateSubreddits?id=7"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(collegiateSubredditRepository, times(1)).findById(eq(7L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Collegiate Subreddit with id 7 not found", responseString);
    }

    @Test
    public void api_put_subreddit() throws Exception {

        // arrange
        CollegiateSubreddit colSub = CollegiateSubreddit.builder().id(1L).name("Gamer College").location("Greenland")
                .subreddit("r/gamercollege").build();

        CollegiateSubreddit updatedColSub = CollegiateSubreddit.builder().id(1L).name("Cool Guy College")
                .location("Finland").subreddit("r/coolguycollege").build();

        CollegiateSubreddit correctColSub = CollegiateSubreddit.builder().id(1L).name("Cool Guy College")
                .location("Finland").subreddit("r/coolguycollege").build();

        String requestBody = mapper.writeValueAsString(updatedColSub);
        String expectedReturn = mapper.writeValueAsString(correctColSub);

        when(collegiateSubredditRepository.findById(eq(1L))).thenReturn(Optional.of(colSub));

        // act
        MvcResult response = mockMvc.perform(
                put("/api/collegiateSubreddits?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(collegiateSubredditRepository, times(1)).findById(1L);
        verify(collegiateSubredditRepository, times(1)).save(correctColSub);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedReturn, responseString);
    }

    @Test
    public void api_put_nonexistent_subreddit() throws Exception {
        // arrange

        CollegiateSubreddit updatedColSub = CollegiateSubreddit.builder().id(1L).name("Gamer College")
                .location("Greenland").subreddit("r/gamercollege").build();

        String requestBody = mapper.writeValueAsString(updatedColSub);

        MvcResult response = mockMvc.perform(
                put("/api/collegiateSubreddits?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        verify(collegiateSubredditRepository, times(1)).findById(1L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("Collegiate Subreddit with id 1 not found", responseString);
    }
}
