package com.rk.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;

import com.rk.demo.model.LdapUser;

@Service
public class LdapUserService {

	@Autowired
	LdapTemplate ldapTemplate;

	private static final String BASE_DN = "ou=users,ou=system";

	// uid=goru,ou=users,ou=system
	public void addUser(LdapUser ldapUser) {
		ldapTemplate.bind("uid=" + ldapUser.getUsername() + "," + BASE_DN, null, ldapUser.toAttributes());
	}

	public List<LdapUser> getAllUsers() {
		List<LdapUser> users = ldapTemplate.search(BASE_DN, "(objectClass=inetOrgPerson)", getLdapAttributeMapper());
		return users;
	}

	private AttributesMapper<LdapUser> getLdapAttributeMapper() {
		return (AttributesMapper<LdapUser>) attributes -> {
			System.out.println("Found entry: " + attributes);
			LdapUser ldapUser = new LdapUser();
			ldapUser.setCn(attributes.get("cn").get().toString());
			ldapUser.setSn(attributes.get("sn").get().toString());
			ldapUser.setUsername(attributes.get("uid").get().toString());
			ldapUser.setPassword(attributes.get("userPassword").get().toString());
			return ldapUser;
		};
	}

	public LdapUser getUserByUid(String uid) {
		List<LdapUser> users = ldapTemplate.search(BASE_DN, "(uid=" + uid + ")", getLdapAttributeMapper());
		if (users != null && users.size() > 0) {
			return users.get(0);
		}
		return null;
	}

}
