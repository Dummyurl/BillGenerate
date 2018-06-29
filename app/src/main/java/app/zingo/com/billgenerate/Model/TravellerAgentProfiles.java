package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels Tech on 05-05-2018.
 */

public class TravellerAgentProfiles {

    @SerializedName("TravellerAgentProfileId")
    private int TravellerAgentProfileId;

    @SerializedName("Status")
    private String Status;

    @SerializedName("Plans")
    private String Plans;

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

    @SerializedName("Gender")
    private String UserGender;

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

    @SerializedName("PlaceName")
    private String PlaceName;

    /*@SerializedName("UserRoles")
    private UserRole _userRole;*/

    @SerializedName("ProfilePhoto")
    private String ProfilePhoto;

    @SerializedName("FrontSidePhoto")
    private String FrontSidePhoto;

    @SerializedName("BackSidePhoto")
    private String BackSidePhoto;

    @SerializedName("CommissionPercentage")
    private long CommissionPercentage;

    @SerializedName("CommissionAmount")
    private long CommissionAmount;

    @SerializedName("ReferralCodeUsed")
    private String ReferralCodeUsed;



    @SerializedName("ReferralAmount")
    private long ReferralAmount;

    @SerializedName("ReferralCodeToUseForOtherProfile")
    private String ReferralCodeToUseForOtherProfile;

    @SerializedName("ReferralAmountForOtherProfile")
    private long ReferralAmountForOtherProfile;

    @SerializedName("WalletBalance")
    private int WalletBalance;

    @SerializedName("UsedAmount")
    private int UsedAmount;

    public int getWalletBalance() {
        return WalletBalance;
    }

    public void setWalletBalance(int walletBalance) {
        WalletBalance = walletBalance;
    }

    public int getUsedAmount() {
        return UsedAmount;
    }

    public void setUsedAmount(int usedAmount) {
        UsedAmount = usedAmount;
    }

    public int getTravellerAgentProfileId() {
        return TravellerAgentProfileId;
    }

    public void setTravellerAgentProfileId(int travellerAgentProfileId) {
        TravellerAgentProfileId = travellerAgentProfileId;
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

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserGender() {
        return UserGender;
    }

    public void setUserGender(String userGender) {
        UserGender = userGender;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
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

    /*public UserRole get_userRole() {
        return _userRole;
    }

    public void set_userRole(UserRole _userRole) {
        this._userRole = _userRole;
    }*/

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }

    public String getFrontSidePhoto() {
        return FrontSidePhoto;
    }

    public void setFrontSidePhoto(String frontSidePhoto) {
        FrontSidePhoto = frontSidePhoto;
    }

    public String getBackSidePhoto() {
        return BackSidePhoto;
    }

    public void setBackSidePhoto(String backSidePhoto) {
        BackSidePhoto = backSidePhoto;
    }

    public long getCommissionPercentage() {
        return CommissionPercentage;
    }

    public void setCommissionPercentage(long commissionPercentage) {
        CommissionPercentage = commissionPercentage;
    }

    public long getCommissionAmount() {
        return CommissionAmount;
    }

    public void setCommissionAmount(long commissionAmount) {
        CommissionAmount = commissionAmount;
    }

    public String getReferralCodeUsed() {
        return ReferralCodeUsed;
    }

    public void setReferralCodeUsed(String referralCodeUsed) {
        ReferralCodeUsed = referralCodeUsed;
    }

    public long getReferralAmount() {
        return ReferralAmount;
    }

    public void setReferralAmount(long referralAmount) {
        ReferralAmount = referralAmount;
    }

    public String getReferralCodeToUseForOtherProfile() {
        return ReferralCodeToUseForOtherProfile;
    }

    public void setReferralCodeToUseForOtherProfile(String referralCodeToUseForOtherProfile) {
        ReferralCodeToUseForOtherProfile = referralCodeToUseForOtherProfile;
    }

    public long getReferralAmountForOtherProfile() {
        return ReferralAmountForOtherProfile;
    }

    public void setReferralAmountForOtherProfile(long referralAmountForOtherProfile) {
        ReferralAmountForOtherProfile = referralAmountForOtherProfile;
    }
}
