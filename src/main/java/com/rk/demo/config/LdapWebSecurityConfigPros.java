package com.rk.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.web.SecurityFilterChain;

@Profile("ldap-config-file")
@Configuration
@EnableWebSecurity
public class LdapWebSecurityConfigPros {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> authorize.anyRequest().fullyAuthenticated())
				.formLogin(Customizer.withDefaults());

		return http.build();
	}

	@Bean
	AuthenticationManager authManager(BaseLdapPathContextSource source) {
		LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(source);
		factory.setUserDnPatterns("cn={0}");// takes user from login page
		return factory.createAuthenticationManager();
	}

}
