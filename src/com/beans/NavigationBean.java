package com.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class NavigationBean implements Serializable {
  private static final long serialVersionUID = 1L;
 
  private String page="index.xhtml";
 
  public String getPage() {
    return page;
  }
 
  public void setPage(String currentPage) {
    this.page=currentPage;
  }
}