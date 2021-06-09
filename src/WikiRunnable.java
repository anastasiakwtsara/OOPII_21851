import java.io.IOException;

import exception.WikipediaNoArcticleException;

public class WikiRunnable implements Runnable {

  private String name;
  // We use volatile key word to read the latest value of the variable directly
  // from main memory instead of some cache
  private volatile String wikiArticle;

  public WikiRunnable(String name) {
    this.name = name;
  }

  @Override
  public void run() {
    System.out.println("Fetching wiki data");
    try {
      wikiArticle = OpenDataRest.RetrieveWikipedia(name);

    } catch (IOException | WikipediaNoArcticleException e) {
      System.out.println("An error occured while retrieving wiki data");
    } finally {
      System.out.println("Finished retrieving wiki data");
    }
  }

  /**
   * @return the wikiArticle
   */
  public String getWikiArticle() {
    return wikiArticle;
  }
}
