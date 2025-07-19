package com.ezee.insurence.service;

import com.ezee.insurence.dto.AuthDTO;

public interface AuthService {

	public AuthDTO getAuthendication(String username, String password);

	public AuthDTO verification(String authtoken);

	public int logout(AuthDTO authDTO);
}
