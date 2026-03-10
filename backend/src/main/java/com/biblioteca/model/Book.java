package com.biblioteca.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    
    @Column(name = "published_date")
    private Date publishedDate;
    
    @Column(name = "is_available")
    private boolean isAvailable;

    public Book() {
    }

    public Book(String title, String author, Date publishedDate, boolean isAvailable) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        this.isAvailable = isAvailable;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("available")
    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
