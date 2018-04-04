package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by CSC on 11/3/2017.
 */

public class Profile1 {

    @SerializedName("ProfileId")
    private int ProfileId;

    @SerializedName("Status")
    private String Status;



    @SerializedName("Plans")

    private String Plans;

    @SerializedName("UniqueId")
    private String UniqueId;

    @SerializedName("FirstName")
    private String FirstName;
    @SerializedName("MiddleName")
    private String MiddleName;
    @SerializedName("LastName")
    private String LastName;

    @SerializedName("UserName")
    private String UserName;
    @SerializedName("Password")
    private String Password;
    @SerializedName("CityId")
    private int CityId;

    @SerializedName("Email")
    private String Email;
    @SerializedName("PhoneNumber")
    private String PhoneNumber;
    @SerializedName("Address")
    private String Address;
    @SerializedName("PinCode")
    private String PinCode;
    @SerializedName("UserRoleId")
    private int UserRoleId;
    @SerializedName("Gender")
    private String UserGender;
    @SerializedName("ProfilePhoto")
    private String ProfilePhoto;
    @SerializedName("FrontSidePhoto")
    private String FrontSidePhoto;
    @SerializedName("BackSidePhoto")
    private String BackSidePhoto;


    @SerializedName("PlaceName")
    private String PlaceName;



    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public int getUserRoleId() {
        return UserRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        UserRoleId = userRoleId;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String placeName) {
        PlaceName = placeName;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        this.ProfileId = profileId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        this.MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

//    public int get_city() {
//        return CityId;
//    }

//    public void set_city(int _city) {
//        this.CityId = _city;
//    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        this.PinCode = pinCode;
    }

    public void setUserGender(String userGender) {
        UserGender = userGender;
    }

    public String getUserGender() {
        return UserGender;
    }

    public void setBackSidePhoto(String backSidePhoto) {
        BackSidePhoto = backSidePhoto;
    }

    public String getBackSidePhoto() {
        return BackSidePhoto;
    }

    public void setFrontSidePhoto(String frontSidePhoto) {
        FrontSidePhoto = frontSidePhoto;
    }

    public String getFrontSidePhoto() {
        return FrontSidePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getPlans() {
        return Plans;
    }

    public void setPlans(String plans) {
        Plans = plans;
    }
    //    public int get_userRole() {
//        return UserRoleId;
//    }

//    public void set_userRole(int _userRole) {
//        this.UserRoleId = _userRole;
//    }

//    public List<Documents> getProfileList() {
//        return profileList;
//    }

//    public void setProfileList(List<Documents> profileList) {
//        this.profileList = profileList;
//    }



//    @SerializedName("documentsList")
//    private List<Documents> profileList;
}
