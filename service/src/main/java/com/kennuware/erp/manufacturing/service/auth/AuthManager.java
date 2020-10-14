package com.kennuware.erp.manufacturing.service.auth;

import javax.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class AuthManager implements AuthenticationManager {

  public static final String AUTH = "authorized";

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    HttpSession session = (HttpSession) authentication.getPrincipal();
    authentication.setAuthenticated((boolean) session.getAttribute(AUTH));
    return authentication;
  }
}
