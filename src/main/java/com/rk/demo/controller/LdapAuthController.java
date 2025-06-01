package com.rk.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rk.demo.model.LdapUser;
import com.rk.demo.service.LdapUserService;

@RestController
public class LdapAuthController {

	@Autowired
	LdapUserService ldapService;

	@GetMapping("/")
	public String index() {
		return "Welcome to the home page!";
	}

	@GetMapping("/user")
	public String getUserDetails(Authentication authentication) {
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();

		String UserDetails = "User details --> useName:+" + userDetails.getUsername() + "\n password:"
				+ userDetails.getPassword() + "\n accNonExpired?:" + userDetails.isAccountNonExpired()
				+ "\n accNonLocked?:" + userDetails.isAccountNonLocked() + "\n credNonExpired?:"
				+ userDetails.isCredentialsNonExpired();
		return UserDetails;
	}

	@GetMapping("/allusers")
	public List<LdapUser> getAllUserDetails() {
		return ldapService.getAllUsers();
	}

	@GetMapping("/user/{uid}")
	public LdapUser getUserByUid(@PathVariable String uid) {
		return ldapService.getUserByUid(uid);
	}

	@GetMapping("/deleteuser/{uid}")
	public String deleteUserByUid(@PathVariable String uid) {
		ldapService.removeUserByUid(uid);
		return "User " + uid + " is deleted";
	}

}
