package com.rk.demo;

import java.util.Properties;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class JavaAppLdap {

	DirContext connection;

	private void newLdapConnection() {
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:10389");

		env.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system");
		env.put(Context.SECURITY_CREDENTIALS, "secret");

		// env.put(Context.SECURITY_PRINCIPAL, "uid=100,ou=users,ou=system");
		// env.put(Context.SECURITY_CREDENTIALS, "100");

		try {
			connection = new InitialDirContext(env);
			System.out.println("Connected to Ldap server!!");
		} catch (AuthenticationException e) {
			System.out.println(e.getMessage());
		} catch (NamingException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private void getAllUsers() throws NamingException {
		String searchFilter = "(objectClass=inetOrgPerson)";
		String[] reqAttribute = { "cn", "sn" };
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAttribute);

		NamingEnumeration<SearchResult> users = connection.search("ou=users,ou=system", searchFilter, controls);

		SearchResult searchResults = null;

		while (users.hasMore()) {
			searchResults = users.next();
			Attributes attrs = searchResults.getAttributes();
			System.out.println(attrs.get("cn").get(0) + "----" + attrs.get("sn").get(0));
		}
	}

	private void addUser(String userName) throws NamingException {
		Attributes attrs = new BasicAttributes();
		Attribute attr = new BasicAttribute("objectClass");
		attr.add("inetOrgPerson");
		attrs.put(attr);

		// user details
		attrs.put("sn", "g");

		connection.createSubcontext("cn=" + userName + ",ou=users,ou=system", attrs);
		System.out.println("added user " + userName + " successfully!!");
	}

	private void deleteUser(String userName) throws NamingException {
		connection.destroySubcontext("cn=" + userName + ",ou=users,ou=system");
		System.out.println("deleted user " + userName + " successfully!!");
	}

	private void addUserToGroup(String username, String groupName) throws NamingException {
		ModificationItem[] modItems = new ModificationItem[1];
		Attribute attr = new BasicAttribute("uniqueMember", "cn=" + username + ",ou=users,ou=system");
		modItems[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr);

		connection.modifyAttributes("cn=" + groupName + ",ou=groups,ou=system", modItems);
		System.out.println("added user " + username + " to group " + groupName + "successfully!!");
	}

	private void deleteUserToGroup(String username, String groupName) throws NamingException {
		ModificationItem[] modItems = new ModificationItem[1];
		Attribute attr = new BasicAttribute("uniqueMember", "cn=" + username + ",ou=users,ou=system");
		modItems[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr);

		connection.modifyAttributes("cn=" + groupName + ",ou=groups,ou=system", modItems);
		System.out.println("deleted user " + username + " to group " + groupName + "successfully!!");
	}

	private void searchUser(String userName) throws NamingException {
		String searchFilter = "(uid=" + userName + ")";
		// String searchFilter = "(&(uid=100)(sn=g))";
		// String searchFilter = "(|(uid=100)(sn=g))";
		String[] reqAttribute = { "uid", "cn", "sn" };
		SearchControls controls = new SearchControls();
		controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		controls.setReturningAttributes(reqAttribute);

		NamingEnumeration<SearchResult> users = connection.search("ou=users,ou=system", searchFilter, controls);

		SearchResult searchResults = null;

		while (users.hasMore()) {
			searchResults = users.next();
			Attributes attrs = searchResults.getAttributes();
			System.out.println(
					attrs.get("uid").get(0) + "----" + attrs.get("cn").get(0) + "----" + attrs.get("sn").get(0));
		}
	}

	private boolean authenticateUser(String userName, String password) {
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:10389");

		env.put(Context.SECURITY_PRINCIPAL, "uid=" + userName + ",ou=users,ou=system");
		env.put(Context.SECURITY_CREDENTIALS, password);

		// env.put(Context.SECURITY_PRINCIPAL, "uid=100,ou=users,ou=system");
		// env.put(Context.SECURITY_CREDENTIALS, "100");

		try {
			DirContext con = new InitialDirContext(env);
			System.out.println("Valid user - authenticated successfully!!");
			con.close();
			return true;
		} catch (Exception e) {
			System.out.println("Invalid user - authenticated Failed!!");
			return false;
		}

	}

	private void updateUserPwd(String userName, String password) {
		try {
			ModificationItem[] mods = new ModificationItem[1];
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", password));
			connection.modifyAttributes("uid=" + userName + ",ou=users,ou=system", mods);
			System.out.println("user " + userName + " pwd is updated successfully!!");
		} catch (Exception e) {
			System.out.println("user " + userName + " pwd is update failed!!");
		}
	}

	private void updateUserDetails(String userName, String mobileNbr) {
		try {
			ModificationItem[] mods = new ModificationItem[1];
			Attribute attr = new BasicAttribute("mobile", mobileNbr);
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
			connection.modifyAttributes("uid=" + userName + ",ou=users,ou=system", mods);
			System.out.println("user " + userName + " mobile is updated successfully!!");
		} catch (Exception e) {
			System.out.println("user " + userName + " mobile is update failed!!");
		}
	}

	public static void main(String[] args) throws NamingException, InterruptedException {

		JavaAppLdap app = new JavaAppLdap();
		app.newLdapConnection();

		System.out.println("Check authentication of user-valid case");
		app.authenticateUser("100", "100");
		Thread.sleep(2000);

		System.out.println("Update user pwd");
		app.updateUserPwd("100", "200");
		Thread.sleep(10000);

		System.out.println("Check authentication of user with old pwd -invalid case");
		app.authenticateUser("100", "100");
		Thread.sleep(2000);

		System.out.println("Update user pwd with old ");
		app.updateUserPwd("100", "100");
		System.out.println("Check authentication of user-valid case");
		app.authenticateUser("100", "100");
		Thread.sleep(2000);

		String username = "R-" + (int) (Math.random() * 100);
		app.addUser(username);
		System.out.println("After adding user --------------");
		app.addUserToGroup(username, "Administrators");
		app.getAllUsers();

		System.out.println("Search user adding user --------------");
		app.searchUser(username);
		System.out.println("Update user mobile");
		app.updateUserDetails("100", "1234567890");

		System.out.println("Waiting for 10 secs: check in Apache Directory studio");
		Thread.sleep(10000);
		System.out.println("WaitTime Complted!!");

		app.deleteUser(username);
		System.out.println("After deleting user --------------");
		app.deleteUserToGroup(username, "Administrators");
		app.getAllUsers();
		System.out.println("Update user mobile");
		app.updateUserDetails("100", "123");

	}

}
