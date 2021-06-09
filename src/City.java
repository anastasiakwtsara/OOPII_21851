import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

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

  public void setTermsVector(int[] termsVector) {
    this.termsVector = termsVector;
  }

  public void setGeodesicVector(double[] geodesicVector) {
    this.geodesicVector = geodesicVector;
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
  public Traveller freeTicket(List<Traveller> travellers) {
    double maxSimilarity = 0;
    Traveller maxTraveller = null;
    for (int i = 0; i < travellers.size(); i++) {
      double similarity = travellers.get(i).calculateSimilarity(this);
      if (similarity > maxSimilarity) {
        maxTraveller = travellers.get(i);
        maxSimilarity = similarity;
      }
    }
    return maxTraveller;
  }

  /**
   * Retrieve the location data and criterion data from wikipedia
   * 
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   * @throws WikipediaNoArcticleException
   */
  public void retrieveWikiAndLocationData() throws IOException, WikipediaNoArcticleException {
    // to open data rest gurnaei to location
    WikiRunnable wikiRunnable = new WikiRunnable(name);
    Thread threadWiki = new Thread(wikiRunnable);
    threadWiki.start();
    LocationRunnable locationRunnable = new LocationRunnable(name, country,
        "712259cb3d2c2691b6363207459e2bc7");
    Thread locationWiki = new Thread(locationRunnable);
    locationWiki.start();
    try {
      // Wait for both threads to finish in order to retrieve results
      threadWiki.join();
      locationWiki.join();

      // Retrieve results
      String wikiArticle = wikiRunnable.getWikiArticle();
      Location location = locationRunnable.getLocation();

      geodesicVector[0] = location.getLatitude();
      geodesicVector[1] = location.getLongitude();

      String[] criterions = { "museum", "theatre", "sea", "cafe", "mountain", "park", "restaurant",
          "river", "monument", "bar" };
      for (int i = 0; i < criterions.length; i++) {
        termsVector[i] = OpenDataRest.countCriterionfCity(wikiArticle, criterions[i]);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
//gia na xrhsimopoihsw ws key ton traveller sto calcsimil collaborative
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((country == null) ? 0 : country.hashCode());
    result = prime * result + Arrays.hashCode(geodesicVector);
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + Arrays.hashCode(termsVector);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    City other = (City) obj;
    if (country == null) {
      if (other.country != null)
        return false;
    } else if (!country.equals(other.country))
      return false;
    if (!Arrays.equals(geodesicVector, other.geodesicVector))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (!Arrays.equals(termsVector, other.termsVector))
      return false;
    return true;
  }
}
