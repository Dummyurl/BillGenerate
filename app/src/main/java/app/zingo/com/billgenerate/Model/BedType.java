package app.zingo.com.billgenerate.Model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

import java.io.Serializable;

/**
 * Created by CSC on 11/9/2017.
 */

public class BedType implements Serializable {

    @SerializedName("BedTypeId")
    private int bedTypeId;
    @SerializedName("BedTypeName")
    private String bedType;
    @SerializedName("IsExtraBedType")
    private boolean isExtrabedType;

    public BedType()
    {

    }

    public BedType(int bedTypeId, String bedType, boolean isExtrabedType)
    {
        this.bedTypeId = bedTypeId;
        this.bedType = bedType;
        this.isExtrabedType = isExtrabedType;
    }

    public BedType(String bedType, boolean isExtrabedType)
    {
        //this.bedTypeId = bedTypeId;
        this.bedType = bedType;
        this.isExtrabedType = isExtrabedType;
    }

    public void setBedTypeId(int bedTypeId) {
        this.bedTypeId = bedTypeId;
    }

    public int getBedTypeId() {
        return bedTypeId;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public String getBedType() {
        return bedType;
    }

    public void setExtrabedType(boolean extrabedType) {
        isExtrabedType = extrabedType;
    }
    public boolean getExtrabedType()
    {
        return isExtrabedType;
    }
}
