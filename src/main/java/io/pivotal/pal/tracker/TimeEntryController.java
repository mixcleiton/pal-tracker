package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository repository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.repository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        return new ResponseEntity(this.repository.create(timeEntryToCreate), HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") Long timeEntryId) {
        TimeEntry timeEntry = this.repository.find(timeEntryId);
        ResponseEntity<TimeEntry> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (timeEntry != null) {
            resposta = new ResponseEntity<>(timeEntry, HttpStatus.OK);
        }

        return resposta;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity(this.repository.list(), HttpStatus.OK);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(@PathVariable("id") Long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry timeEntry = this.repository.update(timeEntryId, expected);
        ResponseEntity<TimeEntry> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (timeEntry != null) {
            resposta = new ResponseEntity<>(timeEntry, HttpStatus.OK);
        }

        return resposta;
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable("id") Long timeEntryId) {
       this.repository.delete(timeEntryId);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
