package com.ezee.insurence.controller.IO;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthIO {

	private String username;
	private String authToken;
	private String role;

}
