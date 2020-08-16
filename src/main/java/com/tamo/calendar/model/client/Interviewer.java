package com.tamo.calendar.model.client;

import com.tamo.calendar.enumtypes.ClientType;
import com.tamo.calendar.model.interview.Availability;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity
@DiscriminatorValue(ClientType.INTERVIEWER)
public class Interviewer extends Client {

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
