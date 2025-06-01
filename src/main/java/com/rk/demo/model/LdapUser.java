package com.rk.demo.model;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import lombok.Data;

@Data
public class LdapUser {

	private String username;// uid
	private String cn;
	private String sn;
	private String password;

	public Attributes toAttributes() {
		Attributes attributes = new BasicAttributes();
		attributes.put("objectClass", "inetOrgPerson");
		attributes.put("uid", username);
		attributes.put("cn", cn);
		attributes.put("sn", sn);
		attributes.put("userPassword", password);
		return attributes;
	}
}
