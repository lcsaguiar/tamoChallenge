package com.tamo.calendar.dao;

import com.tamo.calendar.model.interview.Availability;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class AvailabilityDao extends Dao {
    public void saveAvailability(Availability availability) {
            getSession().save(availability);
    }
    
    public List<Availability> getAvailabilityList() {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<Availability> cq = builder.createQuery(Availability.class);
        Root<Availability> root = cq.from(Availability.class);
        cq.select(root);
        TypedQuery<Availability> query = getSession().createQuery(cq);

        return query.getResultList();
    }
    
}
