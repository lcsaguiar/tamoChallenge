package com.tamo.calendar.model.user;

import com.tamo.calendar.enumtypes.UserType;
import com.tamo.calendar.model.interview.Availability;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue(UserType.INTERVIEWER)
public class Interviewer extends User {

    public Interviewer() {
        super();
    }

    public Interviewer(String name, String email) {
        super(name, email);
    }

    public Interviewer(Long id, String name, String email, List<Availability> availabilities) {
        super(id, name, email, availabilities);
    }
}
