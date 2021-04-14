/**
 * Elder traveller class.
 */
public class ElderTraveller extends Traveller {

  @Override
  public double calculateSimilarity(City city) {
    return 0.5 * calculateSimilarityTermsVector(city)
        + (1 - 0.5) * calculateSimilarityGeodesicVector(city);
  }

  /**
   * Calculate similarity using Jaccard metric.
   * 
   * @param city the city to calculate term vector similarity for
   * @return the similarity number
   */
  private double calculateSimilarityTermsVector(City city) {
    int union = 0;
    int intersection = 0;
    // if a value in term vector is 0 we assume that it does not belong in the set
    // if value is greater than 0 we assume that it belongs in the set
    for (int i = 0; i < getTermVectorInterest().length; i++) {
      if (getTermVectorInterest()[i] > 0 && city.getTermsVector()[i] > 0) {
        // if both vectors have value > 0 then their union and intersection must be increased by one
        union++;
        intersection++;
      } else if (getTermVectorInterest()[i] > 0 && city.getTermsVector()[i] == 0) {
        // if at least one vector has value > 0 then their union must be increased by one
        union++;
      } else if (getTermVectorInterest()[i] == 0 && city.getTermsVector()[i] > 0) {
        union++;
      }
    }
    if (union == 0) {
      // if union is zero, means the two sets have only 0 values, so similarity is 1
      return 1.0;
    } else {
      return ((double) intersection) / union;
    }
  }
}
