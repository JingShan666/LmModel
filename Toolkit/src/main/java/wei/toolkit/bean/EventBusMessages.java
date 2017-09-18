package wei.toolkit.bean;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class EventBusMessages {

    public static class MomentsMsgNotification {
        private String json;

        public MomentsMsgNotification(String json) {
            this.json = json;
        }

        public String getJson() {
            return json;
        }

        public void setJson(String json) {
            this.json = json;
        }
    }

    public static class GpsChangeNotification {
        private String city;
        private double latitude;
        private double longitude;


        public GpsChangeNotification(String city) {
            this.city = city;
        }

        public GpsChangeNotification(String city, double latitude, double longitude) {
            this.city = city;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

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
    }
}
