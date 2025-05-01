package beadando.isports_app.domain;

import com.google.firebase.Timestamp;

public class User {

    private String uid;
    private String email;
    private String username;
    private String fullName;
    private String description;
    private int age;
    private String searchName;
    private Timestamp createdAt;
    private Timestamp lastOnline;

    public User() {}

    public User(String uid, String email, String username, String fullName, String description, int age, String searchName, Timestamp lastOnline, Timestamp createdAt) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.description = description;
        this.age = age;
        this.searchName = searchName;
        this.lastOnline = lastOnline;
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public Timestamp getCreatedAt() {return createdAt; }

    public void setCreatedAt(Timestamp createdAt) {this.createdAt = createdAt; }

    public Timestamp getLastOnline() {return lastOnline; }

    public void setLastOnline(Timestamp lastOnline) {this.lastOnline = lastOnline; }
}
