package att.atthack2016.models;

/**
 * Created by Edgar Salvador Maurilio on 03/01/2016.
 */
public class StationModel {

    private int id;
    private String name;
    private String image_url;
    private double latitude;
    private double longitude;

    public StationModel(int id, String name, String image_url, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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
