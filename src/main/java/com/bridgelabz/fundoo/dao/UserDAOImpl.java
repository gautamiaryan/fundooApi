package com.bridgelabz.fundoo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class UserDAOImpl implements IUserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User persistUser(User user) {
	entityManager.persist(user);
	return user;
    }

    @Override
    public List<User> getAllUser() {
	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	CriteriaQuery<User> cq = cb.createQuery(User.class);
	Root<User> root = cq.from(User.class);
	cq.select(root);
	TypedQuery<User> query = entityManager.createQuery(cq);
	return query.getResultList();
    }

    @Override
    public User getUserByEmail(String emailId) {

	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	CriteriaQuery<User> cq = cb.createQuery(User.class);

	Root<User> root = cq.from(User.class);

	Predicate emailPredicate = cb.equal(root.get("emailId"), emailId);

	cq.select(root).where(cb.or(emailPredicate));

	TypedQuery<User> query = entityManager.createQuery(cq);

	List<User> result = query.getResultList();

	return result.isEmpty() ? null : result.get(0);
    }

    @Override
    public User getUserById(Long id) {
	List<User> userList = getAllUser();
	User user = null;
	for (User userObj : userList) {
	    if (userObj.getUserId().equals(id)) {
		user = userObj;
	    }
	}
	return user;
    }

}
