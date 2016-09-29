package com.kurre.calloff;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kurre on 28-09-2016.
 */
public class Contact implements Parcelable{

    public String name;
    public String phone_number;
    public String ip_address;

    public Contact(String name, String phone_number, String ip_address) {
        this.name = name;
        this.phone_number = phone_number;
        this.ip_address = ip_address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone_number);
        dest.writeString(ip_address);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    // "De-parcel object
    public Contact(Parcel in) {
        name = in.readString();
        phone_number = in.readString();
        ip_address = in.readString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this.phone_number.equals(((Contact)obj).phone_number);
    }
}
