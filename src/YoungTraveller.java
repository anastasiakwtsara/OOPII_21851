/**
 * Young traveller class.
 */
public class YoungTraveller extends Traveller {

  @Override
  public double calculateSimilarity(City city) {
    return 0.95 * calculateSimilarityTermsVector(city)
        + (1 - 0.95) * calculateSimilarityGeodesicVector(city);
  }

  /**
   * Calculate similarity using Euclidean metric.
   * 
   * @param city
   *          the city to calculate term vector similarity for
   * @return the similarity number
   */
  private double calculateSimilarityTermsVector(City city) {
    double distance = 0;
    for (int i = 0; i < getTermVectorInterest().length; i++) {
      int difference = getTermVectorInterest()[i] - city.getTermsVector()[i];
      double diff = Math.pow(difference, 2);
      distance += diff;
    }
    double euclideanDistance = Math.sqrt(distance);
    double similarity = 1 / (1 + euclideanDistance);
    return similarity;
  }
}
