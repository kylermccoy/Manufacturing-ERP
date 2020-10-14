package com.kennuware.erp.manufacturing.service.auth;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class AuthFilter extends AbstractPreAuthenticatedProcessingFilter {

  @Override
  protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
    return request.getSession();
  }

  @Override
  protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
    return null;
  }
}
