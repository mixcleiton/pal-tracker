package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository repository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.repository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = this.repository.create(timeEntryToCreate);

        actionCounter.increment();
        timeEntrySummary.record(repository.list().size());

        return new ResponseEntity(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") Long timeEntryId) {
        TimeEntry timeEntry = this.repository.find(timeEntryId);
        ResponseEntity<TimeEntry> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (timeEntry != null) {
            actionCounter.increment();
            resposta = new ResponseEntity<>(timeEntry, HttpStatus.OK);
        }

        return resposta;
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return new ResponseEntity(this.repository.list(), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable("id") Long timeEntryId, @RequestBody TimeEntry expected) {
        TimeEntry timeEntry = this.repository.update(timeEntryId, expected);
        ResponseEntity<TimeEntry> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (timeEntry != null) {
            actionCounter.increment();
            resposta = new ResponseEntity<>(timeEntry, HttpStatus.OK);
        }

        return resposta;
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long timeEntryId) {
       this.repository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(repository.list().size());

       return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
