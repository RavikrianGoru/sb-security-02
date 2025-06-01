package com.rk.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.rk.demo.model.LdapUser;
import com.rk.demo.service.LdapUserService;

@Controller
public class UserController {

	@Autowired
	LdapUserService ldapService;

	@GetMapping("/addUserForm")
	public String addUserForm(Model model) {
		model.addAttribute("ldapUser", new LdapUser());
		return "addUser";
	}

	@PostMapping("/addUser")
	public String addUser(LdapUser ldapUser) {
		ldapService.addUser(ldapUser);
		return "addUserSuccess";
	}

	@GetMapping("/userList")
	public String userList(Model model) {
		model.addAttribute("userList", ldapService.getAllUsers());
		return "userList";
	}
}
