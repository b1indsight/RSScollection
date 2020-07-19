package com.RSScollection.demo.RSScollection.models;

public class Rss{
  private Integer id;
  private String title = new String();
  private String url = new String();
  private Integer userId;

  public Rss() {
    
  }

  public Rss(String url, int userId) {
    this.url = url;
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "{" + id + title + url + "}";
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
    return;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
    return;
  }

  public int getUserId() {
    return this.userId;
  }
}
