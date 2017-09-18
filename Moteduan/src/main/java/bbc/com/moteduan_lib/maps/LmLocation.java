package bbc.com.moteduan_lib.maps;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class LmLocation {
    private double latitude;
    private double longitude;
    private String city;
    private String street;
    private String streetNumber;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    @Override
    public String toString() {
        return "LmLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                '}';
    }
}
