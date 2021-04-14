import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Abstract Traveller class.
 */
public abstract class Traveller {

  private int[] termVectorInterest;

  private double[] geodesicVector;

  public abstract double calculateSimilarity(City city);

  public static final int MAX_DIST = 9505;

  /**
   * @return the termVectorInterest
   */
  public int[] getTermVectorInterest() {
    return termVectorInterest;
  }

  /**
   * @param termVectorInterest
   *          the termVectorInterest to set
   */
  public void setTermVectorInterest(int[] termVectorInterest) {
    this.termVectorInterest = termVectorInterest;
  }

  /**
   * @return the geodesicVector
   */
  public double[] getGeodesicVector() {
    return geodesicVector;
  }

  /**
   * @param geodesicVector
   *          the geodesicVector to set
   */
  public void setGeodesicVector(double[] geodesicVector) {
    this.geodesicVector = geodesicVector;
  }

  /**
   * Return city with max similarity for traveller.
   * 
   * @param cities
   *          the list of cities
   * @return the city with max similarity
   */
  public City compareCities(ArrayList<City> cities) {
    double maxSimilarity = 0;
    City maxCity = null;
    for (City city : cities) {
      double similarity = calculateSimilarity(city);
      if (similarity > maxSimilarity) {
        maxSimilarity = similarity;
        maxCity = city;
      }
    }
    return maxCity;
  }

  /**
   * Return city with max number similarity for traveller.
   * 
   * @param cities the list of cities
   * @param number the number of max similarity
   * @return the city with max number similarity
   */
  public City compareCities(ArrayList<City> cities, int number) {
    Collections.sort(cities, new Comparator<City>() {

      @Override
      public int compare(City o1, City o2) {
        double similarity1 = calculateSimilarity(o1);
        double similarity2 = calculateSimilarity(o2);
        if (similarity1 < similarity2) {
          return -1;
        } else if (similarity1 == similarity2) {
          return 0;
        } else {
          return 1;
        }
      }
    });
    Collections.reverse(cities);
    return cities.get(number - 1);
  }

  /**
   * Calculate geodesic vector similarity
   * 
   * @param city the city to calculate similarity for
   * @return the similarity number
   */
  protected double calculateSimilarityGeodesicVector(City city) {
    double distance = distance(city.getGeodesicVector()[0], city.getGeodesicVector()[1],
        getGeodesicVector()[0], getGeodesicVector()[1]);
    double difference = 2 - (distance / MAX_DIST);
    double fraction = 2 / difference;
    double similarity = Math.log10(fraction) / Math.log10(2);
    return similarity;
  }

  /**
   * Return geodesic distance between to locations.
   * 
   * @param lat1 latitude of first location
   * @param lon1 longitude of first location
   * @param lat2 latitude of second location
   * @param lon2 longitude of second location
   * @return the distance in kilometers
   */
  private double distance(double lat1, double lon1, double lat2, double lon2) {
    if ((lat1 == lat2) && (lon1 == lon2)) {
      return 0;
    } else {
      double theta = lon1 - lon2;
      double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
          + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
              * Math.cos(Math.toRadians(theta));
      dist = Math.acos(dist);
      dist = Math.toDegrees(dist);
      dist = dist * 60 * 1.1515;
      dist = dist * 1.609344;
      return dist;
    }
  }
}
