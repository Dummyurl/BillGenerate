package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZingoHotels.com on 04-11-2017.
 */

public class Documents {

    @SerializedName("DocumentId")
    private int DocumentId;
    @SerializedName("DocumentType")
    private String DocumentType;
    @SerializedName("DocumentNumber")
    private String DocumentNumber;
    @SerializedName("DocumentName")
    private String DocumentName;
    @SerializedName("Status")
    private String Status;
    @SerializedName("ReEnterDocumentNumber")
    private String ReEnterDocumentNumber;
    @SerializedName("ProfileId")
    private int ProfileId;

    public int getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(int documentId) {
        DocumentId = documentId;
    }

    public String getDocumentType() {
        return DocumentType;
    }

    public void setDocumentType(String documentType) {
        DocumentType = documentType;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    public String getDocumentName() {
        return DocumentName;
    }

    public void setDocumentName(String documentName) {
        DocumentName = documentName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getReEnterDocumentNumber() {
        return ReEnterDocumentNumber;
    }

    public void setReEnterDocumentNumber(String reEnterDocumentNumber) {
        ReEnterDocumentNumber = reEnterDocumentNumber;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }
}
