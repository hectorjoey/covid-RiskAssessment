package hector.developers.covid19riskassessment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Users implements Serializable {


    private Long id;

    @SerializedName("firstname")
    @Expose
    private String firstname;

    @SerializedName("lastname")
    @Expose
    private String lastname;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("designation")
    @Expose
    private String designation;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("age")
    @Expose
    private  String age;

    @SerializedName("userType")
    @Expose
    private String userType;

    @SerializedName("gender")
    @Expose
    private String gender;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("lga")
    @Expose
    private String lga;

    @SerializedName("address")
    @Expose
    private String address;

    private String enabled;

    @SerializedName("supervisedBy")
    @Expose
    private String supervisedBy;

    public Users() {
    }

    public Users(Long id, String firstname, String lastname, String phone, String designation,
                 String email, String password, String userType, String gender, String state, String address, String enabled, String supervisedBy) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.designation = designation;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.gender = gender;
        this.state = state;
        this.address = address;
        this.enabled = enabled;
        this.supervisedBy = supervisedBy;
    }


    public Users(String firstname, String lastname, String age, String designation, String phone, String email,
                 String gender, String address, String state, String lga) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.designation = designation;
        this.email = email;
        this.gender = gender;
        this.state = state;
        this.address = address;
        this.age = age;
        this.lga = lga;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getSupervisedBy() {
        return supervisedBy;
    }

    public void setSupervisedBy(String supervisedBy) {
        this.supervisedBy = supervisedBy;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLga() {
        return lga;
    }

    public void setLga(String lga) {
        this.lga = lga;
    }
}