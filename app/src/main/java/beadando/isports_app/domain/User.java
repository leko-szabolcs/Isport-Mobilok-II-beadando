package beadando.isports_app.domain;

import com.google.firebase.Timestamp;

public class User {

    private String uid;
    private String email;
    private String username;
    private String searchName;
    Timestamp createdAt;
    Timestamp lastOnline;

    public User() {}

    public User(String uid, String email, String username, String searchName, Timestamp lastOnline, Timestamp createdAt) {        this.uid = uid;
        this.email = email;
        this.username = username;
        this.searchName = searchName;
        this.lastOnline = lastOnline;
        this.createdAt = createdAt;
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
