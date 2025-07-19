package com.ezee.insurence.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezee.insurence.controller.IO.AuthIO;
import com.ezee.insurence.controller.IO.EmployeeIO;
import com.ezee.insurence.controller.IO.ResponseIO;
import com.ezee.insurence.dto.AuthDTO;
import com.ezee.insurence.service.AuthService;

@RestController
@RequestMapping("/insurance")
public class AuthController {
	@Autowired
	AuthService authService;

	@GetMapping("/login")
	public ResponseIO<AuthIO> getAuthToken(@RequestBody EmployeeIO employeeIO) {
		AuthIO authIO = new AuthIO();
		String username = employeeIO.getUsername();
		String password = employeeIO.getPassword();
		AuthDTO authDTO = authService.getAuthendication(username, password);
		if (authDTO != null) {
			authIO.setAuthToken(authDTO.getAuthToken());
			authIO.setUsername(authDTO.getEmlployeeDTO().getUsername());
			authIO.setRole(authDTO.getEmlployeeDTO().getEmployeeRole());
		}
		return ResponseIO.success(authIO);
	}

	@PostMapping("/logout")
	public ResponseIO<String> getLogout(@RequestHeader("authtoken") String authtoken) {
		AuthDTO authDTO = authService.verification(authtoken);
		authService.logout(authDTO);
		return ResponseIO.success("Log out Sucessfully");
	}

}
