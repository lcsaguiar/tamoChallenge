package com.tamo.calendar.model.client;

import com.tamo.calendar.enumtypes.ClientType;
import com.tamo.calendar.model.interview.Availability;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue(ClientType.CANDIDATE)
public class Candidate extends Client {
    public Candidate() {
        super();
    }

    public Candidate(String name, String email) {
        super(name, email);
    }

    public Candidate(Long id, String name, String email, List<Availability> availabilities) {
        super(id, name, email, availabilities);
    }
}
