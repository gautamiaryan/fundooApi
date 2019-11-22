package com.bridgelabz.fundoo.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.dao.UserDAO;
import com.bridgelabz.fundoo.model.User;

@Repository
public class UserDAOImpl implements UserDAO {

	@Autowired
	private EntityManager entityManager;

	@Override
	public User register(User user) {
		Session currentSession =entityManager.unwrap(Session.class);
		currentSession.save(user);
		return user;
	}

	@Override
	public List<User> getAllUser() {
		Session currentSession=entityManager.unwrap(Session.class);
		Query<User> query=currentSession.createQuery("from User",User.class);
		List<User> userList=query.getResultList();
		return userList;
	}

	@Override
	public boolean isVarified(User user) {
		List<User> userList =getAllUser();
		for(User userObj : userList) {
			if(userObj.getStatus()) {
				return true;
			}

		}
		return false;

	}

	@Override
	public User getUserById(Integer id) {
		List<User> userList=getAllUser();
		User user=null;
		for(User userObj:userList) {
			if(userObj.getId().equals(id)) {
				user=userObj;
			}
		}
		return user;
	}

}
