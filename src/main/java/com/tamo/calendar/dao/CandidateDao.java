package com.tamo.calendar.dao;

import com.tamo.calendar.exceptions.CandidateNotFoundException;
import com.tamo.calendar.model.client.Candidate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class CandidateDao extends Dao {

    public void saveCandidate(Candidate candidate) {
        getSession().save(candidate);
    }

    public List<Candidate> getCandidatesList() {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Candidate> cq = builder.createQuery(Candidate.class);
        Root<Candidate> root = cq.from(Candidate.class);
        cq.select(root);

        TypedQuery<Candidate> allQuery = getSession().createQuery(cq);

        return allQuery.getResultList();

    }

    public Candidate getCandidateById(String id) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Candidate> cq = builder.createQuery(Candidate.class);
        Root<Candidate> root = cq.from(Candidate.class);
        cq.select(root).where(builder.equal(root.get("id"), id));

        TypedQuery<Candidate> query = getSession().createQuery(cq);

        try {
            return query.getSingleResult();
        }
        catch (Exception ex) {
            throw new CandidateNotFoundException("Candidate with id " + id + " not found");
        }
    }
}
