package vn.lifetimetech.base.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "test")
public class UsersEntity {
    private int id;
    private String username;
    private String password;
    private String email;
    private String fullname;
    private String phoneNumber;
    private String skype;
    private String role;
    private int isDelFlag;
    private Integer createdId;
    private Timestamp createdAt;
    private Integer modifiedId;
    private Timestamp modifiedAt;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "fullname")
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Basic
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "skype")
    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    @Basic
    @Column(name = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "is_del_flag")
    public int getIsDelFlag() {
        return isDelFlag;
    }

    public void setIsDelFlag(int isDelFlag) {
        this.isDelFlag = isDelFlag;
    }

    @Basic
    @Column(name = "created_id")
    public Integer getCreatedId() {
        return createdId;
    }

    public void setCreatedId(Integer createdId) {
        this.createdId = createdId;
    }

    @Basic
    @Column(name = "created_at")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "modified_id")
    public Integer getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(Integer modifiedId) {
        this.modifiedId = modifiedId;
    }

    @Basic
    @Column(name = "modified_at")
    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Timestamp modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsersEntity that = (UsersEntity) o;
        return id == that.id &&
                isDelFlag == that.isDelFlag &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(email, that.email) &&
                Objects.equals(fullname, that.fullname) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(skype, that.skype) &&
                Objects.equals(role, that.role) &&
                Objects.equals(createdId, that.createdId) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(modifiedId, that.modifiedId) &&
                Objects.equals(modifiedAt, that.modifiedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, email, fullname, phoneNumber, skype, role, isDelFlag, createdId, createdAt, modifiedId, modifiedAt);
    }
}
