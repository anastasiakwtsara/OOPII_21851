/**
 * Middle traveller class.
 */
public class MiddleTraveller extends Traveller {

  @Override
  public double calculateSimilarity(City city) {
    return 0.5 * calculateSimilarityTermsVector(city)
        + (1 - 0.5) * calculateSimilarityGeodesicVector(city);
  }

  /**
   * Calculate similarity using cosine metric.
   * 
   * @param city
   *          the city to calculate term vector similarity for
   * @return the similarity number
   */
  private double calculateSimilarityTermsVector(City city) {
    int arithmitis = 0;
    double sunoloA = 0;
    double sunoloB = 0;
    for (int i = 0; i < getTermVectorInterest().length; i++) {
      arithmitis += (getTermVectorInterest()[i]) * (city.getTermsVector()[i]);
      sunoloA += Math.pow(getTermVectorInterest()[i], 2);
      sunoloB += Math.pow(city.getTermsVector()[i], 2);
    }
    double similarity = arithmitis / Math.sqrt(sunoloA * sunoloB);
    return similarity;
  }

}
