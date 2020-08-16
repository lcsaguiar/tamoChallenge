package com.tamo.calendar.dao;

import com.tamo.calendar.exceptions.CandidateNotFoundException;
import com.tamo.calendar.exceptions.ClientNotFoundException;
import com.tamo.calendar.model.client.Candidate;
import com.tamo.calendar.model.client.Client;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
@Transactional
public class ClientDao extends Dao {

    public Client getClientById(String id) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Client> cq = builder.createQuery(Client.class);
        Root<Client> root = cq.from(Client.class);
        cq.select(root).where(builder.equal(root.get("id"), id));

        TypedQuery<Client> query = getSession().createQuery(cq);

        try {
            return query.getSingleResult();
        }
        catch (Exception ex) {
            throw new ClientNotFoundException("Client with id " + id + " not found");
        }
    }
}
