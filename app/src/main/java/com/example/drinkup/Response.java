package com.example.drinkup;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response implements Parcelable {

    public static final Parcelable.Creator<Response> CREATOR = new Parcelable.Creator<Response>() {
        @Override
        public Response createFromParcel(Parcel source) {
            return new Response(source);
        }

        @Override
        public Response[] newArray(int size) {
            return new Response[size];
        }
    };


    public List<Drink> getDrinkList() {
        return drinkList;
    }

    public void setDrinkList(List<Drink> drinkList) {
        this.drinkList = drinkList;
    }
    @SerializedName(value = "drinks")
    private List<Drink> drinkList;

    public Response(String status, int totalResults, List<Drink> drinkList) {

        this.drinkList = drinkList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.drinkList);
    }

    @Override
    public String toString() {
        return "Response{" +
                ", drinkList=" + drinkList +
                '}';
    }


    public void readFromParcel(Parcel source) {

        this.drinkList = source.createTypedArrayList(Response.CREATOR);

    }
    protected Response(Parcel in) {
        this.drinkList = in.createTypedArrayList(Response.CREATOR);
    }


}
