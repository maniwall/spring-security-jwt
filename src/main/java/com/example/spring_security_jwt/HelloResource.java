package com.example.spring_security_jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_security_jwt.model.AuthenticationRequest;
import com.example.spring_security_jwt.model.AuthenticationResponse;
import com.example.spring_security_jwt.model.MyUserDetailsService;
import com.example.spring_security_jwt.util.JwtUtil;

@RestController
public class HelloResource {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@RequestMapping("/hello")
	public String hello() {
		return "Welcome";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest autheRequest)
			throws Exception {

		try {
			
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(autheRequest.getUsername(), autheRequest.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		UserDetails userdetails = userDetailsService.loadUserByUsername(autheRequest.getUsername());
		final String jwt = jwtUtil.generateToken(userdetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
