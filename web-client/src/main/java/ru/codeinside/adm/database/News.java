/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * Copyright (c) 2013, MPL CodeInside http://codeinside.ru
 */

package ru.codeinside.adm.database;

import ru.codeinside.adm.AdminServiceProvider;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "news")
@NamedQueries({
    @NamedQuery(name = "allNews", query = "SELECT n FROM News n ORDER BY n.dateCreated desc")
})
@SequenceGenerator(name = "news_seq", sequenceName = "news_seq", allocationSize = 1)
public class News implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(generator = "news_seq")
  private Long id;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(columnDefinition = "text", nullable = false)
  private String text;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "creation_date", nullable = false)
  private Date dateCreated = new Date();

  public News() {

  }

  public News(String title, String text) {
    this.setTitle(title);
    this.setText(text);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getDateCreated() {
    return (dateCreated.getYear() + 1900) + "-" + addZero((dateCreated.getMonth() + 1)) + "-" + addZero(dateCreated.getDate()) + "   " + dateCreated.getHours() + ":" + dateCreated.getMinutes();
  }

  public String getDateCreated2() {
    return (dateCreated.getYear() + 1900) + "-" + addZero((dateCreated.getMonth() + 1)) + "-" + addZero(dateCreated.getDate());
  }

  public void setDateCreated(Date dateCreated) {
    this.dateCreated = dateCreated;
  }

  public static List<News> getNews() {
    return AdminServiceProvider.get().getNews();
  }

  public static Object addZero(int i) {
    if (i < 10) {
      return new StringBuilder("0" + i);
    } else {
      return i;
    }
  }
}
