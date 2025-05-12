package com.example.bcsd;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Post {
    @JsonIgnore
    private Integer id;
    private String content;
    private String title;
    private String author;
    private LocalDateTime date;
    
    @JsonIgnore
    private static List<Post> allPosts = new ArrayList<>();
    
    public Post() {
    }
    
    public Post(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.date = LocalDateTime.now();
    }

    public void save() {
        this.id = allPosts.size();
        if (this.date == null) {
            this.date = LocalDateTime.now();
        }
        allPosts.add(this);
    }
    
    @JsonIgnore
    public static Post findById(Integer id) {
        if (id >= 0 && id < allPosts.size()) {
            return allPosts.get(id);
        }
        return null;
    }
    
    @JsonIgnore
    public static List<Post> findAll() {
        return allPosts;
    }
    
    public static void deleteById(Integer id) {
        if (id >= 0 && id < allPosts.size()) {
            allPosts.remove(id.intValue());
            
            for (int i = id; i < allPosts.size(); i++) {
                allPosts.get(i).id = i;
            }
        }
    }
    
    public void update(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.date = LocalDateTime.now();
    }

    @JsonIgnore
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
