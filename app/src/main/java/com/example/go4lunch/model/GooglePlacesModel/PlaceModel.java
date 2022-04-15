package com.example.go4lunch.model.GooglePlacesModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PlaceModel implements Parcelable {

    @SerializedName("business_status")
    @Expose
    private String businessStatus;

    @SerializedName("geometry")
    @Expose
    private GeometryModel geometry;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("obfuscated_type")
    @Expose
    private List<Object> obfuscatedType = null;

    @SerializedName("photos")
    @Expose
    private List<Photo> photos;

    @SerializedName("place_id")
    @Expose
    private String placeId;


    @SerializedName("rating")
    @Expose
    private Double rating;

    @SerializedName("reference")
    @Expose
    private String reference;



    @SerializedName("scope")
    @Expose
    private String scope;

    @SerializedName("types")
    @Expose
    private List<String> types = null;

    @SerializedName("user_ratings_total")
    @Expose
    private Integer userRatingsTotal;

    @SerializedName("vicinity")
    @Expose
    private String vicinity;

    @SerializedName("opening_hours")
    @Expose
    private OpeningHoursModel mOpeningHoursModel;

    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("formatted_address")
    @Expose
    private String address;
    @SerializedName("international_phone_number")
    @Expose
    private String phone;

    protected PlaceModel(Parcel in) {
        businessStatus = in.readString();
        icon = in.readString();
        name = in.readString();
        placeId = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readDouble();
        }
        reference = in.readString();
        scope = in.readString();
        types = in.createStringArrayList();
        if (in.readByte() == 0) {
            userRatingsTotal = null;
        } else {
            userRatingsTotal = in.readInt();
        }
        vicinity = in.readString();
        website = in.readString();
        address = in.readString();
        phone = in.readString();
        isSaved = in.readByte() != 0;
    }

    public static final Creator<PlaceModel> CREATOR = new Creator<PlaceModel>() {
        @Override
        public PlaceModel createFromParcel(Parcel in) {
            return new PlaceModel(in);
        }

        @Override
        public PlaceModel[] newArray(int size) {
            return new PlaceModel[size];
        }
    };

    public OpeningHoursModel getOpeningHoursModel() {
        return mOpeningHoursModel;
    }

    public void setOpeningHoursModel(OpeningHoursModel openingHoursModel) {
        mOpeningHoursModel = openingHoursModel;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public OpeningHoursModel getOpeningHours() {
        return mOpeningHoursModel;
    }

    public void setOpeningHours(OpeningHoursModel openingHoursModel) {
        this.mOpeningHoursModel = openingHoursModel;
    }

    @Expose(serialize = false, deserialize = false)
    private boolean isSaved;

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public GeometryModel getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryModel geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getObfuscatedType() {
        return obfuscatedType;
    }

    public void setObfuscatedType(List<Object> obfuscatedType) {
        this.obfuscatedType = obfuscatedType;
    }


    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }


    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public Integer getUserRatingsTotal() {
        return userRatingsTotal;
    }

    public void setUserRatingsTotal(Integer userRatingsTotal) {
        this.userRatingsTotal = userRatingsTotal;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(businessStatus);
        dest.writeString(icon);
        dest.writeString(name);
        dest.writeString(placeId);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(rating);
        }
        dest.writeString(reference);
        dest.writeString(scope);
        dest.writeStringList(types);
        if (userRatingsTotal == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(userRatingsTotal);
        }
        dest.writeString(vicinity);
        dest.writeString(website);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeByte((byte) (isSaved ? 1 : 0));
    }
}
