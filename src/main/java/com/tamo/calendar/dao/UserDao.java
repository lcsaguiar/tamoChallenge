package com.tamo.calendar.dao;

import com.tamo.calendar.exceptions.UserNotFoundException;
import com.tamo.calendar.model.user.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
@Transactional
public class UserDao extends Dao {

    public User getUserById(String id) {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<User> cq = builder.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root).where(builder.equal(root.get("id"), id));

        TypedQuery<User> query = getSession().createQuery(cq);

        try {
            return query.getSingleResult();
        }
        catch (Exception ex) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }
}
