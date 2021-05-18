package com.example.drinkup;

import android.os.Parcel;
import android.os.Parcelable;

public class DrinkSource implements Parcelable {

    public static final Parcelable.Creator<DrinkSource> CREATOR = new Parcelable.Creator<DrinkSource>() {
        @Override
        public DrinkSource createFromParcel(Parcel source) {
            return new DrinkSource(source);
        }

        @Override
        public DrinkSource[] newArray(int size) {
            return new DrinkSource[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String id;
    private String name;

    public DrinkSource(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public DrinkSource() {
    }

    @Override
    public String toString() {
        return "DrinkSource{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.name = source.readString();
    }

    protected DrinkSource(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }




}
