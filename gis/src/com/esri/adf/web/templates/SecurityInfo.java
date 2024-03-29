/*
 Copyright � 2006 ESRI

 All rights reserved under the copyright laws of the United States
 and applicable international laws, treaties, and conventions.

 You may freely redistribute and use this sample code, with or
 without modification, provided you include the original copyright
 notice and use restrictions.
 See use restrictions at /arcgis/developerkit/userestrictions.
 */
package com.esri.adf.web.templates;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class SecurityInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private String user;

  private String roles;

  private List<String> rolesList;

  private boolean isSecured;

  private boolean isLogoutAvailable;

  public SecurityInfo() {

    isSecured = isLogoutAvailable = false;
    user = null;
    roles = null;
    rolesList = null;

    Object o = FacesContext.getCurrentInstance().getExternalContext().getRequest();

    if (o instanceof HttpServletRequest) {
      HttpServletRequest r = (HttpServletRequest) o;
      if (r.getRemoteUser() != null) {
        isSecured = true;
        user = r.getRemoteUser();
      } else if (r.getSession().getAttribute("user_roles") != null) {
        isSecured = isLogoutAvailable = true;
        String userAndRoles = (String) r.getSession().getAttribute("user_roles");
        int splitIndex = userAndRoles.indexOf(":");
        user = userAndRoles.substring(0, splitIndex);
        roles = userAndRoles.substring(splitIndex + 1);
        rolesList = new ArrayList<String>();
        for (String role : roles.split(",")) {
          rolesList.add(role);
        }
      }
    }
  }

  public String getUser() {
    return user;
  }

  public String getRoles() {
    return roles;
  }

  public List<String> getRolesList() {
    return rolesList;
  }

  public boolean isSecured() {
    return isSecured;
  }

  public boolean isLogoutAvailable() {
    return this.isLogoutAvailable;
  }

  public void setLogoutAvailable(boolean isLogoutAvailable) {
    this.isLogoutAvailable = isLogoutAvailable;
  }

  public String logOut() {
    Object o = FacesContext.getCurrentInstance().getExternalContext().getRequest();
    if (o instanceof HttpServletRequest && isSecured && isLogoutAvailable) {
      HttpServletRequest r = (HttpServletRequest) o;
      r.getSession().invalidate();
      return "login";
    }
    return null;
  }
}
