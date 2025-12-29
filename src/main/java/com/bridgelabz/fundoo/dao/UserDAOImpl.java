package com.bridgelabz.fundoo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.fundoo.model.User;

@Repository
public class UserDAOImpl implements IUserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
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
