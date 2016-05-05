package mad.acai.com.awesomebutton.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import mad.acai.com.awesomebutton.logging.L;

/**
 * Created by Andy on 22/04/2016.
 */
public class Booking  implements Parcelable {

    private int id;
    private String passName;
    private String bookingStatus;
    private String pickUpAdd;
    private String dropOffAdd;
    private Date pickUpTime;
    private Date pickUpDate;
    private double fare;

    public Booking() {

    }

    public Booking(Parcel input) {

        id = input.readInt();
        passName = input.readString();
        bookingStatus = input.readString();
        pickUpAdd = input.readString();
        dropOffAdd = input.readString();
        pickUpTime = new Date(input.readString());
        pickUpDate = new Date(input.readString());
        fare = input.readDouble();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassName() {
        return passName;
    }

    public void setPassName(String passName) {
        this.passName = passName;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getPickUpAdd() {
        return pickUpAdd;
    }

    public void setPickUpAdd(String pickUpAdd) {
        this.pickUpAdd = pickUpAdd;
    }

    public String getDropOffAdd() {
        return dropOffAdd;
    }

    public void setDropOffAdd(String dropOffAdd) {
        this.dropOffAdd = dropOffAdd;
    }

    public Date getPickUpTime() {
        return pickUpTime;
    }

    public void setPickUpTime(Date pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    @Override
    public String toString() {

        return "ID: " + id + "\n " +
                "Booking Status: " + bookingStatus + "\n " +
                "Pick up Address: " + pickUpAdd + "\n " +
                "Drop odd Address: " + dropOffAdd + "\n " +
                "Pick up Time: " + pickUpTime + "\n " +
                "Pick up Date: " + pickUpDate + "\n " +
                "Fare: " + fare + "\n ";
    }

    @Override
    public int describeContents() {
        L.m("descirbe content views");
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        L.m("write to parcel Booking");

        dest.writeInt(id);
        dest.writeString(passName);
        dest.writeString(bookingStatus);
        dest.writeString(pickUpAdd);
        dest.writeString(dropOffAdd);
        dest.writeString(pickUpTime.toString());
        dest.writeString(pickUpDate.toString());
        dest.writeDouble(fare);

    }

    public static final Parcelable.Creator<Booking> CREATOR
            = new Parcelable.Creator<Booking>() {
        public Booking createFromParcel(Parcel in) {
            L.m("create from parcel :Booking");
            return new Booking(in);
        }

        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };
}
