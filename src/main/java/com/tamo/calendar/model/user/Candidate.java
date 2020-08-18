package com.tamo.calendar.model.user;

import com.tamo.calendar.enumtypes.UserType;
import com.tamo.calendar.model.interview.Availability;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue(UserType.CANDIDATE)
public class Candidate extends User {
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
