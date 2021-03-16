/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.springframework.samples.petclinic.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.samples.petclinic.rollout.FlagsService;

/**
 * Controller used to showcase login screen
 *
 * <p/>
 * Also see how a view that resolves to "login" has been added ("login.html").
 */

@Controller
class LoginController {

	Logger logger = LoggerFactory.getLogger(LoginController.class);

	private String username;

	private String password;

	@Autowired
	private FlagsService flags;

	@GetMapping("/login")
	public String login() {
		this.username = "";
		this.password = "";
		return "login";
	}

}
