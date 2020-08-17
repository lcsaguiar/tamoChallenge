package com.tamo.calendar.model.client;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tamo.calendar.model.interview.Availability;
import com.tamo.calendar.model.interview.Duration;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="client_type")
public class Client {

    @ApiModelProperty(hidden = true)
    @Id
    @GeneratedValue
    private Long id;

    @ApiModelProperty(example = "test", required = true)
    @NotNull(message = "Must specify a name")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @ApiModelProperty(example = "test@test.com", required = true)
    @NotNull(message = "Must specify an email")
    @Email(message = "Email must be valid")
    private String email;

    @ApiModelProperty(hidden = true)
    @OneToMany(mappedBy="client", fetch = FetchType.EAGER)
    @Null
    private List<Availability> availabilities;

    public Client() {
    }
    public Client(@NotEmpty(message = "Name cannot be empty") String name, @NotNull(message = "Must specify an email") @Email(message = "Email must be valid") String email) {
        this.name = name;
        this.email = email;
    }

    public Client(Long id, @NotEmpty(message = "Name cannot be empty") String name, @NotNull(message = "Must specify an email") @Email(message = "Email must be valid") String email, List<Availability> availabilities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.availabilities = availabilities;
    }

    public void setAvailabilities(List<Availability> availabilities) {
        this.availabilities = availabilities;
    }

    public Long getId() {
        return id;
    }

    @JsonManagedReference
    public List<Availability> getAvailabilities() {
        return availabilities;
    }

    public List<Duration> returnDatesList() {
        List<Duration> datesList = new LinkedList<>();
        for(Availability a: availabilities) {
            datesList.add(new Duration(a.getStart_duration(), a.getEnd_duration()));
        }
        return datesList;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) &&
                Objects.equals(name, client.name) &&
                Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}
