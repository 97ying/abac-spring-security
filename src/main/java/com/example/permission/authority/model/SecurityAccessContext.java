package com.example.permission.authority.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SecurityAccessContext {
	private Object subject;
	private Object resource;
	private Object action;
}
