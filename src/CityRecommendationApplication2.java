import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 * Class for CityRecomendation application
 */
public class CityRecommendationApplication2 {
	
  //static h main , static k ta antikeimena pou xrhsimopoiei
  private static JSONFileReader json = new JSONFileReader();
  private static DatabaseConnector db = new DatabaseConnector();

  public static void main(String args[]) throws InterruptedException {
	
    List<Traveller> travellers = null;
    try {
      // Try to read travellers from file
      travellers = json.readJSON();
    } catch (Exception e){ 
      // If file does not exist, set a new empty list
      travellers = new ArrayList<>();
      System.out.println("Could not load travellers from json");
    }
    
    Map<String, City> cities = null;
    
   
    db.makeJDBCConnection();
    try {
		cities=db.ReadCities();
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		System.out.println("Could not load cities from db");
		cities = new HashMap<>();
	}
    
    
    int option = 0;
    Scanner scanner = new Scanner(System.in);

    System.out.println("Welcome to City Recommendation application");

    while (option != BasicMenu.EXIT.getCode()) {
      // Show menu and validate input
      try {
        showMenu();
        option = Integer.parseInt(scanner.nextLine());
      } catch (Exception e) {
        System.out.println("Please provide a valid option number");
        continue;
      }

      // Choose option from menu
      switch (BasicMenu.getByCode(option)) {
        case ADD_CITY:
          addCity(cities, scanner);
          break;

        case ADD_TRAVELLER:
          addTraveller(travellers, scanner);
          break;

        case CALCULATE_SIMILARITIES:
          calculateSimilarities(cities, travellers);
          break;

        case COMPARE_CITIES:
          compareCities(cities, travellers);
          break;

        case FREE_TICKET:
          freeTicket(cities, travellers);
          break;

        case CALC_SIM:
          calcSimil(cities, scanner, travellers);
          break;

        case EXIT:
          System.out.println("Thank you for using City Recommendation Application!");
          System.exit(0);
          break;

        default:
          System.out.println("Please provide a valid option number");
          break;
      }
    }
    scanner.close();

  }

  /**
   * Shows menu to user.
   */
  private static void showMenu() {
    System.out.println("Please choose an option from the menu below:");
    System.out.println("1. Add city");
    System.out.println("2. Add traveller");
    System.out.println("3. Calculate similarities");
    System.out.println("4. Compare cities");
    System.out.println("5. Free ticket");
    System.out.println("6. Calculate similarity for the city of ur choice");
    System.out.println("7. Exit");
  }

  /**
   * Add a city to the provided list.
   * 
   * @param cities
   *          the list of cities
   * @param scanner
   *          the scanner object to read input
   */
  private static void addCity(Map<String, City> cities, Scanner scanner) {
    System.out.println("Please provide city name");
    String name = scanner.nextLine();
    System.out.println("Please provide city country code");
    String country = scanner.nextLine();
    City city = new City(name, country);
    city.retrieveWikiAndLocationData();
    cities.put(name, city);
    db.addDataToDB(name, country, city.getGeodesicVector()[0],city.getGeodesicVector()[1], city.getTermsVector()[0], city.getTermsVector()[1], city.getTermsVector()[2], city.getTermsVector()[3], city.getTermsVector()[4], city.getTermsVector()[5], city.getTermsVector()[6], city.getTermsVector()[7], city.getTermsVector()[8], city.getTermsVector()[9]);
  }

  /**
   * Add a traveller to the provided list.
   * 
   * @param travellers
   *          the list of travellers
   * @param scanner
   *          the scanner object to read input
   */
  private static void addTraveller(List<Traveller> travellers, Scanner scanner) {

    System.out.println("Please give the age of traveller. Possible values are 16-115");
    int age = 0;
    // Check for traveller age
    while (true) {
      try {
        age = Integer.parseInt(scanner.nextLine());
        if (age < 16 || age > 115) {
          System.out.println("Please provide a number in the valid range");
        } else {
          break;
        }
      } catch (Exception e) {
        System.out.println("Please provide a valid age number");
      }
    }

    // Create traveller based on age
    Traveller traveller = null;
    if (age >= 16 && age <= 25) {
      traveller = new YoungTraveller();
    } else if (age > 25 && age <= 60) {
      traveller = new MiddleTraveller();
    } else {
      traveller = new ElderTraveller();
    }

    // Enter travellers info

    System.out.println("Please give me your full name");
    String name = scanner.nextLine();

    while (true) {
      try {
        System.out.println("Please provide traveller's latitude");
        double lat = Double.parseDouble(scanner.nextLine());
        System.out.println("Please provide traveller's longitude");
        double lon = Double.parseDouble(scanner.nextLine());
        double[] geodesicVector = { lat, lon };
        traveller.setGeodesicVector(geodesicVector);
        traveller.setTimestamp((System.currentTimeMillis()));
        int[] termVector = new int[10];
        traveller.setName(name);
        // Create vector randomly
        Random r = new Random();
        for (int i = 0; i < termVector.length; i++) {
          termVector[i] = r.nextInt(11);
        }
        traveller.setTermVectorInterest(termVector);
        travellers.add(traveller);
        break;

      } catch (Exception e) {
        System.out.println("Please provide a valid number");
      }
    }
    System.out.println(name);
    try {
      json.writeJSON(travellers);
    } catch (Exception e) {
      System.out.println("Could not write travellers to json file");
    } 
  }

  /**
   * Calculate similarities for all cities and travellers.
   * 
   * @param cities
   *          the list of cities
   * @param travellers
   *          the list of travellers
   */
  private static void calculateSimilarities(Map<String, City> cities, List<Traveller> travellers) {
    if (cities.size() == 0 || travellers.size() == 0) {
      System.out.println("Please add at least one city and one traveller");
    } else {
      for (Map.Entry<String, City> entry : cities.entrySet()) {
        for (Traveller traveller : travellers) {
          System.out.println("Calculating similarity for user " + traveller.getName()
              + " with coordinates (" + traveller.getGeodesicVector()[0] + ", "
              + traveller.getGeodesicVector()[1] + ") and city: " + entry.getKey());
          System.out.println(traveller.calculateSimilarity(entry.getValue()));
        }
      }
    }
  }

  /**
   * Find the most similar city for each traveller.
   * 
   * @param cities
   *          the list of cities
   * @param travellers
   *          the list of travellers
   */
  private static void compareCities(Map<String, City> cities, List<Traveller> travellers) {
    if (cities.size() == 0 || travellers.size() == 0) {
      System.out.println("Please add at least one city and one traveller");
    } else {
      for (Traveller traveller : travellers) {
        City visit = traveller.compareCities(cities);
        traveller.setVisit(visit);
        System.out.println("Most similar for traveller " + traveller.getName()
            + " with coordinates (" + traveller.getGeodesicVector()[0] + ", "
            + traveller.getGeodesicVector()[1] + ") city is: " + visit.getName());
      }
    }
  }

  /**
   * Calculate free tickets for each city.
   * 
   * @param cities
   *          the list of cities
   * @param travellers
   *          the list of travellers
   */
  private static void freeTicket(Map<String, City> cities, List<Traveller> travellers) {
    if (cities.size() == 0 || travellers.size() == 0) {
      System.out.println("Please add at least one city and one traveller");
    } else {
      // Convert list to array
      /*
       * Traveller[] travellerArray = new Traveller[travellers.size()]; for (int
       * i = 0; i < travellerArray.length; i++) { travellerArray[i] =
       * travellers.get(i); }
       */
      for (Map.Entry<String, City> entry : cities.entrySet()) {
        Traveller traveller = entry.getValue().freeTicket(travellers);
        System.out.println("Traveller " + traveller.getName() + "with coordinates ("
            + traveller.getGeodesicVector()[0] + ", " + traveller.getGeodesicVector()[1] + ")"
            + " has a free ticket for city " + entry.getKey());
      }
    }
  }

  private static void calcSimil(Map<String, City> cities, Scanner scanner,
      List<Traveller> travellers) {
    try {
      System.out.println("Give me ur full name");
      String fullname = scanner.nextLine();
      Traveller tr1 = null;
      List<Traveller> travellers2 = new ArrayList<>();
      for (Traveller traveller : travellers) {
        if (fullname.equals(traveller.getName())) {
          travellers2.add(traveller);
          
        }
        
      }
 Collections.sort(travellers2);
 tr1=travellers.get(0);
      System.out.println("Please enter the city you want to search and the country code it belongs");
      String name = scanner.nextLine();
      String country = scanner.nextLine();
      City chosenCity = cities.get(name);
      
      if (chosenCity != null) {
        double d = tr1.calculateSimilarity(chosenCity);
        System.out.println("Your similarity for the city you chose is " + d);
      } else { 
        City city = new City();
        city.setName(name);
        city.setCountry(country);
        city.retrieveWikiAndLocationData();
        cities.put(name, city);
        db.addDataToDB(name, country, city.getGeodesicVector()[0],city.getGeodesicVector()[1], city.getTermsVector()[0], city.getTermsVector()[1], city.getTermsVector()[2], city.getTermsVector()[3], city.getTermsVector()[4], city.getTermsVector()[5], city.getTermsVector()[6], city.getTermsVector()[7], city.getTermsVector()[8], city.getTermsVector()[9]);
        
        double d = tr1.calculateSimilarity(city);
        System.out.println("Your similarity for the city you chose is " + d);
      }
    } catch (Exception e) {
      System.out.println("You are not added as a traveller in the system yet");

    }
  }
}

  