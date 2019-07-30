package io.pivotal.pal.tracker;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private static List<TimeEntry> timeEntries;

    private static Long controle = 1L;

    public InMemoryTimeEntryRepository () {
        controle = 1L;
        timeEntries = new ArrayList<TimeEntry>();
    }


    public TimeEntry create(TimeEntry timeEntry) {
        if (timeEntry.getId() == null) {
            timeEntry.setId(controle);
        }
        timeEntries.add(timeEntry);
        controle++;
        return timeEntry;
    }

    public TimeEntry find(Long id) {
        Optional<TimeEntry> timeEntry = this.timeEntries.stream().filter(entity -> entity.getId() == id).findFirst();
        return timeEntry.orElse(null);
    }

    public List<TimeEntry> list() {
        return this.timeEntries;
    }

    public TimeEntry update(Long id, TimeEntry timeEntry) {
        Boolean achou = Boolean.FALSE;

        for (Integer i = 0; i < this.list().size(); i++) {
            if (id == this.list().get(i).getId()) {
                timeEntry.setId(id);
                this.timeEntries.set(i, timeEntry);
                achou = Boolean.TRUE;
                break;
            }
        }

        if(!achou) {
            timeEntry = null;
        }

        return timeEntry;
    }

    public void delete(Long id) {
        TimeEntry aux = new TimeEntry();
        aux.setId(id);
        this.timeEntries.remove(aux);
    }
}
