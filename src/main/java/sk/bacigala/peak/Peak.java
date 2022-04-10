package sk.bacigala.peak;

public class Peak {

    private final long id, height;
    private final String name, latitude, longitude;

    public Peak(long id, String name, long height, String latitude, String longitude) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public long getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

}
