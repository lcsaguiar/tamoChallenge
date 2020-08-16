package com.tamo.calendar.dao;

import com.tamo.calendar.exceptions.InterviewerNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.tamo.calendar.model.client.Interviewer;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class InterviewerDao extends Dao {

    public void saveInterviewer(Interviewer Interviewer) {
        getSession().save(Interviewer);
    }

    public List<Interviewer> getInterviewersList() {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Interviewer> cq = builder.createQuery(Interviewer.class);
        Root<Interviewer> root = cq.from(Interviewer.class);
        cq.select(root);
        TypedQuery<Interviewer> query = getSession().createQuery(cq);

        return query.getResultList();

    }

    public Interviewer getInterviewerById(String id) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Interviewer> cq = builder.createQuery(Interviewer.class);
        Root<Interviewer> root = cq.from(Interviewer.class);
        cq.select(root).where(builder.equal(root.get("id"), id));
        TypedQuery<Interviewer> query = getSession().createQuery(cq);

        try {
            return query.getSingleResult();
        }
        catch (Exception ex) {
            throw new InterviewerNotFoundException("Interviewer with id " + id + " not found");
        }
    }



}
