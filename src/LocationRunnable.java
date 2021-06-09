import java.io.IOException;

import model.Location;

public class LocationRunnable implements Runnable {

  private String name;
  private String country;
  private String apiKey;
  private volatile Location location;
  
  public LocationRunnable(String name, String country, String apiKey) {
    this.name = name;
    this.country = country;
    this.apiKey = apiKey;
  }

  @Override
  public void run() {
    System.out.println("Fetching location and location data");
    try {
      location = OpenDataRest.RetrieveOpenWeatherMap(name, country,
          apiKey);
    } catch (IOException e) {
      System.out.println("An error occured while retrieving location data");
    } finally {
      System.out.println("Finished retrieving location data");
    }
  }

  /**
   * @return the location
   */
  public Location getLocation() {
    return location;
  }
}
