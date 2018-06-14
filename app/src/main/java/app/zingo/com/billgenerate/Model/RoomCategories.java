package app.zingo.com.billgenerate.Model;


import com.google.gson.annotations.SerializedName;


public class RoomCategories {

    @SerializedName("RoomCategoryId")
    private int RoomCategoryId;

    @SerializedName("CategoryName")
    private String CategoryName;

    @SerializedName("Description")
    private String Description;

    public int getRoomCategoryId() {
        return RoomCategoryId;
    }

    public void setRoomCategoryId(int roomCategoryId) {
        RoomCategoryId = roomCategoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }




}

