package com.example.bcsd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Post {
    private Number id;
    private String description;
    private static List<Map<String, Object>> posts = new ArrayList<>();

    @JsonIgnore
    public Number getId() {
        return id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public List<Map<String, Object>> getPosts() {
        return posts;
    }

    public void setPosts() {
        Map<String, Object> post = new HashMap<>();
        int newId = posts.size();
        post.put("id", newId);
        post.put("description", this.description);
        
        this.id = newId;
        posts.add(post);
    }

    public void deletePosts(Integer id) {
        if (id >= 0 && id < posts.size()) {
            posts.remove(id.intValue());

            for (int i = id; i < posts.size(); i++) {
                posts.get(i).put("id", i);
            }
        }
    }

    public String getDescription(Integer id) {
        if (id >= 0 && id < posts.size()) {
            return (String) posts.get(id).get("description");
        }
        return null;
    }
}
