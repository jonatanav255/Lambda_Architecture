package com.example.batch_processing.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MyEntity {

    @Id
    private Long id;
    private String data;

    // Default constructor (required by JPA)
    public MyEntity() {
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MyEntity{"
                + "id=" + id
                + ", data='" + data + '\''
                + '}';
    }
}
