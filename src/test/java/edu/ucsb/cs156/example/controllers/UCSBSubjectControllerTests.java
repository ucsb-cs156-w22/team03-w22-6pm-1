package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.repositories.UserRepository;
import edu.ucsb.cs156.example.testconfig.TestConfig;
import edu.ucsb.cs156.example.ControllerTestCase;
import edu.ucsb.cs156.example.entities.UCSBSubject;
import edu.ucsb.cs156.example.repositories.UCSBSubjectRepository;
import edu.ucsb.cs156.example.controllers.UCSBSubjectController;

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

@WebMvcTest(controllers = UCSBSubjectController.class)
@Import(TestConfig.class)

public class UCSBSubjectControllerTests extends ControllerTestCase{
    @MockBean
    UCSBSubjectRepository ucsbSubjectRepository;

    @MockBean
    UserRepository userRepository;

    // Test /all endpoint 
    @Test
    public void api_all() throws Exception{

        // arrange
        UCSBSubject subject1 = UCSBSubject.builder().id(1L).subjectCode("code 1").subjectTranslation("translation 1").deptCode("dept code 1").collegeCode("college code 1").relatedDeptCode("related dept code 1").inactive(true).build();
        UCSBSubject subject2 = UCSBSubject.builder().id(2L).subjectCode("code 2").subjectTranslation("translation 2").deptCode("dept code 2").collegeCode("college code 2").relatedDeptCode("related dept code 2").inactive(true).build();
        UCSBSubject subject3 = UCSBSubject.builder().id(3L).subjectCode("code 3").subjectTranslation("translation 3").deptCode("dept code 3").collegeCode("college code 3").relatedDeptCode("related dept code 3").inactive(true).build();

        ArrayList<UCSBSubject> expectedUCSBSubject = new ArrayList<>();
        expectedUCSBSubject.addAll(Arrays.asList(subject1, subject2, subject3));

        when(ucsbSubjectRepository.findAll()).thenReturn(expectedUCSBSubject);

        // act
        MvcResult response = mockMvc.perform(get("/api/ucsbsubjects/all"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(ucsbSubjectRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    @Test
    public void api_all_with_no_subjects() throws Exception {

        // arrange
        ArrayList<UCSBSubject> expectedUCSBSubject = new ArrayList<>();

        when(ucsbSubjectRepository.findAll()).thenReturn(expectedUCSBSubject);

        // act
        MvcResult response = mockMvc.perform(get("/api/ucsbsubjects/all"))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findAll();
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    // Test /post endpoint
    @Test
    @WithMockUser(roles = { "USER" })
    public void api_post() throws Exception {
        // arrange
        UCSBSubject expectedUCSBSubject = UCSBSubject.builder().id(0L).subjectCode("code 1").subjectTranslation("translation 1").deptCode("dept code 1").collegeCode("college code 1").relatedDeptCode("related dept code 1").inactive(true).build();

        when(ucsbSubjectRepository.save(eq(expectedUCSBSubject))).thenReturn(expectedUCSBSubject);

        // act
        MvcResult response = mockMvc.perform(
                post("/api/ucsbsubjects/post?subjectCode=code 1&subjectTranslation=translation 1&deptCode=dept code 1&collegeCode=college code 1&relatedDeptCode=related dept code 1&inactive=true")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).save(expectedUCSBSubject);
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }


    //Test api /get with id parameter endpoint
    @Test
    public void api_get_id_returns_a_subject_that_exists() throws Exception {

        // arrange
        UCSBSubject expectedUCSBSubject = UCSBSubject.builder().id(1L).subjectCode("code 1").subjectTranslation("translation 1").deptCode("dept code 1").collegeCode("college code 1").relatedDeptCode("related dept code 1").inactive(true).build();
        when(ucsbSubjectRepository.findById(eq(1L))).thenReturn(Optional.of(expectedUCSBSubject));

        // act
        MvcResult response = mockMvc.perform(get("/api/ucsbsubjects?id=1"))
                .andExpect(status().isOk()).andReturn();

        // assert

        verify(ucsbSubjectRepository, times(1)).findById(eq(1L));
        String expectedJson = mapper.writeValueAsString(expectedUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedJson, responseString);
    }

    //Test api /get UCSB Subject id that doesn't exist
    @Test
    public void api_get_id_returns_a_subject_that_does_not_exist() throws Exception {

        // arrange
        when(ucsbSubjectRepository.findById(eq(7L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(get("/api/ucsbsubjects?id=7"))
                .andExpect(status().isBadRequest()).andReturn();

        // assert

        verify(ucsbSubjectRepository, times(1)).findById(eq(7L));
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSBSubject with id 7 not found", responseString);
    }

    //Test api /put subject given id
    @Test
    @WithMockUser(roles = { "USER" })
    public void api_subject_put_subject() throws Exception {
        // arrange

        UCSBSubject ucsbsubject1 = UCSBSubject.builder().id(1L).subjectCode("code 1").subjectTranslation("translation 1").deptCode("dept code 1").collegeCode("college code 1").relatedDeptCode("related dept code 1").inactive(true).build();

        UCSBSubject updatedUCSBSubject = UCSBSubject.builder().id(1L).subjectCode("code 2").subjectTranslation("translation 1").deptCode("dept code 1").collegeCode("college code 1").relatedDeptCode("related dept code 1").inactive(true).build();
        UCSBSubject correctUCSBSubject = UCSBSubject.builder().id(1L).subjectCode("code 2").subjectTranslation("translation 1").deptCode("dept code 1").collegeCode("college code 1").relatedDeptCode("related dept code 1").inactive(true).build();

        String requestBody = mapper.writeValueAsString(updatedUCSBSubject);
        String expectedReturn = mapper.writeValueAsString(correctUCSBSubject);

        when(ucsbSubjectRepository.findById(eq(1L))).thenReturn(Optional.of(ucsbsubject1));

        // act
        MvcResult response = mockMvc.perform(
                put("/api/ucsbsubjects?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(1L);
        verify(ucsbSubjectRepository, times(1)).save(correctUCSBSubject);
        String responseString = response.getResponse().getContentAsString();
        assertEquals(expectedReturn, responseString);
    }

    // Test api /put with Subject id that doesn't exist
    @Test
    @WithMockUser(roles = { "USER" })
    public void api_subject_put_that_does_not_exist() throws Exception {
        // arrange

        UCSBSubject updatedUCSBSubject = UCSBSubject.builder().id(1L).subjectCode("code 1").subjectTranslation("translation 1").deptCode("dept code 1").collegeCode("college code 1").relatedDeptCode("related dept code 1").inactive(true).build();

        String requestBody = mapper.writeValueAsString(updatedUCSBSubject);

        when(ucsbSubjectRepository.findById(eq(1L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                put("/api/ucsbsubjects?id=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8")
                        .content(requestBody)
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(1L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSBSubject with id 1 not found", responseString);
    }

    //Test api delete with given id
    @Test
    @WithMockUser(roles = { "ADMIN", "USER" })
    public void api_subject_delete_subject() throws Exception {
        // arrange
        UCSBSubject ucsbSubject = UCSBSubject.builder().id(1L).subjectCode("code 1").subjectTranslation("translation 1").deptCode("dept code 1").collegeCode("college code 1").relatedDeptCode("related dept code 1").inactive(true).build();
        when(ucsbSubjectRepository.findById(eq(1L))).thenReturn(Optional.of(ucsbSubject));

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/ucsbsubjects?id=1")
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(1L);
        verify(ucsbSubjectRepository, times(1)).deleteById(1L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSBSubject with id 1 deleted", responseString);
    }
    //Test api delete with Subject id that doesn't exist
    @Test
    @WithMockUser(roles = { "ADMIN", "USER" })
    public void api_subject_delete_subject_that_does_not_exist() throws Exception {
        // arrange

        when(ucsbSubjectRepository.findById(eq(17L))).thenReturn(Optional.empty());

        // act
        MvcResult response = mockMvc.perform(
                delete("/api/ucsbsubjects?id=17")
                        .with(csrf()))
                .andExpect(status().isBadRequest()).andReturn();

        // assert
        verify(ucsbSubjectRepository, times(1)).findById(17L);
        String responseString = response.getResponse().getContentAsString();
        assertEquals("UCSBSubject with id 17 not found", responseString);
    }
}
