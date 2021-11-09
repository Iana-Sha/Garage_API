package com.shapovalova.microservices.core.user.datalayer;

import org.springframework.data.annotation.Id;
//import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserEntity {
    @Id
    private String id;

    @Version
    private Integer version;

    @Indexed(unique = true)
    private int userId;
    private String name;
    private String lastName;
    private String login;
    private String password;

    public UserEntity(){

    }

    public UserEntity(int userId, String name, String lastName, String login, String password) {
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.login = login;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
