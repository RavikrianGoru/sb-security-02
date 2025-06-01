package com.rk.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.web.SecurityFilterChain;

@Profile("ldap-config-java")
@Configuration
@EnableWebSecurity
public class LdapWebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> authorize.anyRequest().fullyAuthenticated())
				.formLogin(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	public LdapTemplate ldapTemplate() {
		return new LdapTemplate(contextSource());
	}

	@Bean
	@Primary
	public ContextSource contextSource() {
		LdapContextSource ldapContextSource = new LdapContextSource();
		ldapContextSource.setUrl("ldap://localhost:10389");
		// ldapContextSource.setUserDn("uid=admin,ou=system");
		// ldapContextSource.setPassword("secret");
		return ldapContextSource;
	}

	@Bean
	AuthenticationManager authManager(BaseLdapPathContextSource source) {
		LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(source);
		factory.setUserDnPatterns("cn={0},ou=users,ou=system");// takes user from login page
		return factory.createAuthenticationManager();
	}

}
