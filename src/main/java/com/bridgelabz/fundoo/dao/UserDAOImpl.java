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
	public void register(User user) {
		Session currentSession =entityManager.unwrap(Session.class);
		currentSession.save(user);
	}

	@Override
	public boolean login(User user) {
		boolean status=false;
		List<User> userList=getAllUser();
		for(User userObj:userList) {
			if(userObj.getEmailId().equals(user.getEmailId()) && 
					userObj.getPassword().equals(user.getPassword())) {
				status=true;
				break;
			}

		}
		return status;
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
		boolean status=false;
		List<User> userList =getAllUser();
		for(User userObj : userList) {
			if(userObj.getStatus().equals(user.getStatus())) {
				status=true;
				break;
			}
			
		}
		return status;

	}




}