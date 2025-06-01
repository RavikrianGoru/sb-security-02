What is LDAP?
LDAP stands for Lightweight Directory Access Protocol.
It's an open protocol used to access and manage directory services — like user information, authentication, and organizational data — over a network.

✅ Common Uses of LDAP:
	Centralized authentication (SSO-like behavior).
	Storing organizational hierarchies (users, groups, roles).
	Managing user credentials.
	Access control (permissions based on groups/roles).

How does LDAP work?
LDAP works by organizing data in a hierarchical tree-like structure, known as a Directory Information Tree (DIT).

Example DIT:
	dc=example,dc=com
	│
	├── ou=People
	│   ├── uid=john,ou=People,dc=example,dc=com
	│   └── uid=jane,ou=People,dc=example,dc=com
	│
	└── ou=Groups
		└── cn=Admins,ou=Groups,dc=example,dc=com

Basic Operation Flow:
1. Bind – Client authenticates to LDAP server.
2. Search – Search for data (e.g., user by email or UID).
3. Compare – Check if data matches a condition.
4. Add/Modify/Delete – Create or update directory entries (based on permission).
5. Unbind – Close connection.

What is LDIF?
LDIF = LDAP Data Interchange Format
It's a text-based format used to represent LDAP entries and changes (like a batch script for directory data).

LDIF Example:
	dn: uid=john,ou=People,dc=example,dc=com
	objectClass: inetOrgPerson
	cn: John Doe
	sn: Doe
	uid: john
	mail: john@example.com
	userPassword: password123

You can:
	Import data into LDAP using LDIF.
	Export directory data from LDAP as LDIF.
	Tools like ldapadd or ldapmodify use LDIF files to communicate with the LDAP server.
-----------------------------
AD- Active Directory: Directory service Database.
	used to provide authentication, group and user management.
	policies, authentication and suthorization.
	
LDAP- Lightweight Directory Access Protocal : runs over the TCP IP protocal - open and cross- platform
	  Centralized Idenity Management 
	  Single Sign-On(SSO)
	  Directory Services
	  Authentication and Authorization
	  Secure Transamission of Data
	  n/w security and access controll


why LDAP:
	Create policy to everyone.
	Block one user


	  LDAP
App <-------> LDAP Server <---> LDAP DB

														root
														 |
												dc-example,dc=com
												   |			|
												ou=user 	 oc=group
													|
												cn=nohn

														ABC 
													     |
												Dev		Buz		Fin
												 |				 |
												backend			Account
												  |
												name=john,adde=ind,phone=1933033

	

Terms:
	dc		- Domain component
	o		- organization name
	ou		- organization unit
	cn		- common name
	sn		- sur name
	dn		- distinguish name
	User	- inetOrgPersion
	User	- groupOfUniqueName
Authentication Types:	
	1) Simple -- user name and pwd to bind
	2) SASL   -- 

Apache Directory Studio:
-----------------------
	Download and install Apache Directory studio
	open ApacheDirectoryStudio.ini file and update -vm java path to java 11 as LDAP server is not starting for java 17
	Open[Run as Admin]-> Go to LDAP servers-> New Server--[Create new Server]--Start
	
	Right click om newly created connection --> Create a connection
	Go to Connection: Right click --> Properties: Can check host url and port number
			localhost
			10389
	Add user:
		go to ou-users--> Right click--> New -> New Entry--select 1st Create ...-> slect inetOrgPerson--> RDN: select cn or uid
	Update some fields: like password
		click on the use you created -> click addtribute option on main window-> select userPassword-> Finish
		update details in popup window.
	Verify Password:
		click on nelwy added hoshed password- gove plain pwd in very password and verify.
		
	Find the dn and url of user:
		select user->right click -> properties
		DN:		cn=ravi,ou=users,ou=system
		URL:	ldap://localhost:10389/cn=ravi,ou=users,ou=system
		
	ou-groups
		click on cn=Administrators --> uniqueMember tag is responsible to add user into group.
	add 1 user to admin group:
		click on user -> Properties --Copy DN
		click on ou-groups--> click on attribute option --> select uniqueMember-> finish--> past copied DN aginish uniqueMember
	Verify any user has admin access or not?
		

Sample Java Code to connect ldap server:
		Properties env = new Properties();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://localhost:10389");

		env.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system");
		env.put(Context.SECURITY_CREDENTIALS, "secret");

		// env.put(Context.SECURITY_PRINCIPAL, "cn=goru,ou=users,ou=system");
		// env.put(Context.SECURITY_CREDENTIALS, "1");

		try {
			DirContext con = new InitialDirContext(env);
			System.out.println("Connected to Ldap server!!");
		} catch (AuthenticationException e) {
			System.out.println(e.getMessage());
		} catch (NamingException e) {
			System.out.println("Unable to connect Ldap server");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	
Possible Error during connection:
--------------	----------------
	1) Invalid class name: com.sun.jndi.ldap.LdapCtxFactory1
		Cannot instantiate class: com.sun.jndi.ldap.LdapCtxFactory1 [Root exception is java.lang.ClassNotFoundException: com.sun.jndi.ldap.LdapCtxFactory1]
	2) InValid url: ldap1://localhost:10389
		Cannot parse url: ldap1://localhost:10389 [Root exception is java.net.MalformedURLException: Not an LDAP URL: ldap1://localhost:10389]
	2) InValid url: ldap://localhost1:10389	
		javax.naming.CommunicationException: localhost1:10389 [Root exception is java.net.UnknownHostException: localhost1]
	2) InValid url: ldap://localhost:103891	
		javax.naming.CommunicationException: localhost:103891 [Root exception is java.lang.IllegalArgumentException: port out of range:103891]
	3) Invalid principal: uid=admin1,ou=system  
		[LDAP: error code 49 - INVALID_CREDENTIALS: Bind failed: Attempt to lookup non-existant entry: uid=admin1,ou=system]
	3) Invalid principal: uid=admin,ou=system1  
		[LDAP: error code 49 - INVALID_CREDENTIALS: Bind failed: ERR_268 Cannot find a partition for uid=admin,ou=system1
	3) Invalid principal: uid1=admin,ou=system
	3) Invalid principal: uid=admin,ou1=system
		[LDAP: error code 49 - INVALID_CREDENTIALS: Bind failed: Invalid authentication]
	4) Invalid credentials : secret1
		[LDAP: error code 49 - INVALID_CREDENTIALS: Bind failed: ERR_229 Cannot authenticate user uid=admin,ou=system]
	5) Ldap server is stopped:
		javax.naming.CommunicationException: localhost:10389 [Root exception is java.net.ConnectException: Connection refused: connect]

Retrieve data(cn,sn) from ldap through plain java code.
add user.
add user to group.
delete user.
delete user to group.
search user.
search user and & condition.
search user or | condition.
Authenticate user.
Change password of user.
Update user details like update pwd.


==============================================
Spring Boot + LDAP
==============================================
	
sb application
	web
	securty
	ldap
	
spring ldap official doc:n https://spring.io/guides/gs/authenticating-ldap to get sample code.
	copy  and create rest api Controller to access localhost:8080 by comment and uncomment spring security in pom.xml
	Create Config file and copy from official doc.
	
	1) Keep all ldap configs in Config file : @Profile("ldap-config-java")
		LdapTemplate:
		LdapContext Source:
		Authentication Manager:
	java -jar sb-security-02-0.0.1-SNAPSHOT.jar --spring.profiles.active=ldap-config-java
	
		
	2) Keep all ldap configs in properties file:@Profile("ldap-config-file")
	



	


	
	