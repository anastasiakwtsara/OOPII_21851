import java.io.IOException;

import exception.WikipediaNoArcticleException;
import model.Location;

/**
 * Class holding city information.
 */
public class City {

  private int[] termsVector = new int[10];

  private double[] geodesicVector = new double[2];

  private String name;

  private String country;

  public City() {

  }

  /**
   * Constructor using name of city and country code.
   * 
   * @param name
   *          the name of city
   * @param country
   *          the country code of city
   */
  public City(String name, String country) {
    super();
    this.name = name;
    this.country = country;
  }

  /**
   * @return the termsVector
   */
  public int[] getTermsVector() {
    return termsVector;
  }

  /**
   * @return the geodesicVector
   */
  public double[] getGeodesicVector() {
    return geodesicVector;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name
   *          the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * @param country
   *          the country to set
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Pick the traveller with max similarity and give a free ticket.
   * 
   * @param travellers
   *          the list of travellers
   * @return the traveller with max similarity for this city
   */
  public Traveller freeTicket(Traveller[] travellers) {
    double maxSimilarity = 0;
    Traveller maxTraveller = null;
    for (int i = 0; i < travellers.length; i++) {
      double similarity = travellers[i].calculateSimilarity(this);
      if (similarity > maxSimilarity) {
        maxTraveller = travellers[i];
        maxSimilarity = similarity;
      }
    }
    return maxTraveller;
  }

  /**
   * Retrieve the location data and criterion data from wikipedia
   */
  public void retrieveWikiAndLocationData() {
    try {
      String wikiArticle = OpenDataRest.RetrieveWikipedia(name);
      Location location = OpenDataRest.RetrieveOpenWeatherMap(name, country,
          "712259cb3d2c2691b6363207459e2bc7");
      geodesicVector[0] = location.getLatitude();
      geodesicVector[1] = location.getLongitude();

      String[] criterions = { "museum", "theatre", "sea", "cafe", "mountain", "park", "restaurant",
          "river", "monument", "bar" };
      for (int i = 0; i < criterions.length; i++) {
        termsVector[i] = OpenDataRest.countCriterionfCity(wikiArticle, criterions[i]);
      }
    } catch (IOException | WikipediaNoArcticleException e) {
      System.out.println("Cannot find wikipedia article or location for city: " + name);
    }
  }

}
