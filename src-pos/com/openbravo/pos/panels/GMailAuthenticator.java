package com.openbravo.pos.panels;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

class GMailAuthenticator
  extends Authenticator
{
  String user;
  String pw;
  
  public GMailAuthenticator(String username, String password)
  {
    this.user = username;
    this.pw = password;
  }
  
  public PasswordAuthentication getPasswordAuthentication()
  {
    return new PasswordAuthentication(this.user, this.pw);
  }
}
