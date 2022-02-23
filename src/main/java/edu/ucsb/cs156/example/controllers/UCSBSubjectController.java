package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.UCSBSubject;
import edu.ucsb.cs156.example.entities.User;
import edu.ucsb.cs156.example.models.CurrentUser;
import edu.ucsb.cs156.example.repositories.UCSBSubjectRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@Api(description = "UCSBSubject")
@RequestMapping("/api/ucsbsubjects")
@RestController
@Slf4j
public class UCSBSubjectController extends ApiController{

    public class UCSBSubjectOrError {
        Long id;
        UCSBSubject subject;
        ResponseEntity<String> error;

        public UCSBSubjectOrError(Long id) {
            this.id = id;
        }
    }

    @Autowired
    UCSBSubjectRepository ucsbSubjectRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "Lists all UCSB Subject information in the database")
    @GetMapping("/all")
    public Iterable<UCSBSubject> subjectInfo() {
        Iterable<UCSBSubject> subjects = ucsbSubjectRepository.findAll();
        return subjects;
    }

    @ApiOperation(value = "Create a new UCSB Subject entry")
    @PostMapping("/post")
    public UCSBSubject postUCSBSubject(
            @ApiParam("id") @RequestParam long id,
            @ApiParam("subject code") @RequestParam String subjectCode,
            @ApiParam("subject translation") @RequestParam String subjectTranslation,
            @ApiParam("dept code") @RequestParam String deptCode,
            @ApiParam("college code") @RequestParam String collegeCode,
            @ApiParam("related dept code") @RequestParam String relatedDeptCode,
            @ApiParam("inactive") @RequestParam boolean inactive) {
      
        UCSBSubject newSubject = new UCSBSubject();
        newSubject.setId(id);
        newSubject.setSubjectCode(subjectCode);
        newSubject.setSubjectTranslation(subjectTranslation);
        newSubject.setDeptCode(deptCode);
        newSubject.setCollegeCode(collegeCode);
        newSubject.setRelatedDeptCode(relatedDeptCode);
        newSubject.setInactive(inactive);

        UCSBSubject savedSubject = ucsbSubjectRepository.save(newSubject);
        return savedSubject;
    }


    @ApiOperation(value = "Get a UCSB Subject with given id")
    // @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<String> getSubjectById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        UCSBSubjectOrError usoe = new UCSBSubjectOrError(id);

        usoe = doesUCSBSubjectExist(usoe);
        if (usoe.error != null) {
            return usoe.error;
        }
        String body = mapper.writeValueAsString(usoe.subject);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Update a single UCSBSubject")
    @PutMapping("")
    public ResponseEntity<String> putSubjectById(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid UCSBSubject incomingUCSBSubject) throws JsonProcessingException {

        UCSBSubjectOrError usoe = new UCSBSubjectOrError(id);

        usoe = doesUCSBSubjectExist(usoe);
        if (usoe.error != null) {
            return usoe.error;
        }

        ucsbSubjectRepository.save(incomingUCSBSubject);

        String body = mapper.writeValueAsString(incomingUCSBSubject);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Delete a single UCSBSubject")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("")
    public ResponseEntity<String> deleteUCSBSubject(
            @ApiParam("id") @RequestParam Long id) {

        UCSBSubjectOrError usoe = new UCSBSubjectOrError(id);

        usoe = doesUCSBSubjectExist(usoe);
        if (usoe.error != null) {
            return usoe.error;
        }

        ucsbSubjectRepository.deleteById(id);

        return ResponseEntity.ok().body(String.format("UCSBSubject with id %d deleted", id));
    }

    public UCSBSubjectOrError doesUCSBSubjectExist(UCSBSubjectOrError usoe) {

        Optional<UCSBSubject> optionalUCSBSubject = ucsbSubjectRepository.findById(usoe.id);

        if (optionalUCSBSubject.isEmpty()) {
            usoe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("UCSBSubject with id %d not found", usoe.id));
        } else {
            usoe.subject = optionalUCSBSubject.get();
        }
        return usoe;
    }
}
