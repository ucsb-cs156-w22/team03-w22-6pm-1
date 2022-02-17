package edu.ucsb.cs156.example.controllers;

import edu.ucsb.cs156.example.entities.CollegiateSubreddit;
import edu.ucsb.cs156.example.repositories.CollegiateSubredditRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.DeleteMapping;

import javax.validation.Valid;
import java.util.Optional;

@Api(description = "CollegiateSubredditController")
@RequestMapping("/api/collegiateSubreddits")
@RestController
@Slf4j
public class CollegiateSubredditController extends ApiController {

    public class SubredditOrError {
        Long id;
        CollegiateSubreddit subreddit;
        ResponseEntity<String> error;

        public SubredditOrError(Long id) {
            this.id = id;
        }
    }

    @Autowired
    CollegiateSubredditRepository collegiateSubredditRepository;

    @Autowired
    ObjectMapper mapper;

    @ApiOperation(value = "List all collegiate subreddits in the database")
    @GetMapping("/all")
    public Iterable<CollegiateSubreddit> index() {
        Iterable<CollegiateSubreddit> subreddits = collegiateSubredditRepository.findAll();
        return subreddits;
    }

    @ApiOperation(value = "Create a new entry in the table")
    @PostMapping("/post")
    public CollegiateSubreddit createEntry(
            @ApiParam("name") @RequestParam String name,
            @ApiParam("location") @RequestParam String location,
            @ApiParam("subreddit") @RequestParam String subreddit) {
        CollegiateSubreddit sub = new CollegiateSubreddit();
        sub.setName(name);
        sub.setLocation(location);
        sub.setSubreddit(subreddit);
        CollegiateSubreddit savedSub = collegiateSubredditRepository.save(sub);
        return savedSub;
    }

    @ApiOperation(value = "Get a collegiate subreddit with given id")
    // @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<String> getSubredditById(
            @ApiParam("id") @RequestParam Long id) throws JsonProcessingException {
        SubredditOrError soe = new SubredditOrError(id);

        soe = doesSubredditExist(soe);
        if (soe.error != null) {
            return soe.error;
        }
        String body = mapper.writeValueAsString(soe.subreddit);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Update a single CollegiateSubreddit")
    @PutMapping("")
    public ResponseEntity<String> putSubredditById(
            @ApiParam("id") @RequestParam Long id,
            @RequestBody @Valid CollegiateSubreddit incomingCollegiateSubreddit) throws JsonProcessingException {

        SubredditOrError soe = new SubredditOrError(id);

        soe = doesSubredditExist(soe);
        if (soe.error != null) {
            return soe.error;
        }

        collegiateSubredditRepository.save(incomingCollegiateSubreddit);

        String body = mapper.writeValueAsString(incomingCollegiateSubreddit);
        return ResponseEntity.ok().body(body);
    }

    @ApiOperation(value = "Delete another user's todo")
    @DeleteMapping("")
    public ResponseEntity<String> deleteSubreddit(
            @ApiParam("id") @RequestParam Long id) {

        SubredditOrError soe = new SubredditOrError(id);

        soe = doesSubredditExist(soe);
        if (soe.error != null) {
            return soe.error;
        }

        collegiateSubredditRepository.deleteById(id);

        return ResponseEntity.ok().body(String.format("Collegiate Subreddit with id %d deleted", id));
    }

    public SubredditOrError doesSubredditExist(SubredditOrError soe) {

        Optional<CollegiateSubreddit> optionalSubreddit = collegiateSubredditRepository.findById(soe.id);

        if (optionalSubreddit.isEmpty()) {
            soe.error = ResponseEntity
                    .badRequest()
                    .body(String.format("Collegiate Subreddit with id %d not found", soe.id));
        } else {
            soe.subreddit = optionalSubreddit.get();
        }
        return soe;
    }
}