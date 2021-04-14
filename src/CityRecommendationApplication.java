import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Class for CityRecomendation application
 */
public class CityRecommendationApplication {

  public static void main(String args[]) {

    ArrayList<City> cities = new ArrayList<>();
    ArrayList<Traveller> travellers = new ArrayList<>();
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

        case EXIT:
          System.out.println("Thank you for using Weather Application!");
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
    System.out.println("6. Exit");
  }

  /**
   * Add a city to the provided list.
   * 
   * @param cities
   *          the list of cities
   * @param scanner
   *          the scanner object to read input
   */
  private static void addCity(ArrayList<City> cities, Scanner scanner) {
    System.out.println("Please provide city name");
    String name = scanner.nextLine();
    System.out.println("Please provide city country code");
    String country = scanner.nextLine();
    City city = new City(name, country);
    city.retrieveWikiAndLocationData();
    cities.add(city);
  }

  /**
   * Add a traveller to the provided list.
   * 
   * @param travellers
   *          the list of travellers
   * @param scanner
   *          the scanner object to read input
   */
  private static void addTraveller(ArrayList<Traveller> travellers, Scanner scanner) {
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
    while (true) {
      try {
        System.out.println("Please provide traveller's latitude");
        double lat = Double.parseDouble(scanner.nextLine());
        System.out.println("Please provide traveller's longitude");
        double lon = Double.parseDouble(scanner.nextLine());
        double[] geodesicVector = { lat, lon };
        traveller.setGeodesicVector(geodesicVector);
        int[] termVector = new int[10];

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
  }

  /**
   * Calculate similarities for all cities and travellers.
   * 
   * @param cities
   *          the list of cities
   * @param travellers
   *          the list of travellers
   */
  private static void calculateSimilarities(ArrayList<City> cities,
      ArrayList<Traveller> travellers) {
    if (cities.size() == 0 || travellers.size() == 0) {
      System.out.println("Please add at least one city and one traveller");
    } else {
      for (City city : cities) {
        for (Traveller traveller : travellers) {
          System.out.println("Calculating similarity for user with coordinates ("
              + traveller.getGeodesicVector()[0] + ", " + traveller.getGeodesicVector()[1]
              + ") and city: " + city.getName());
          System.out.println(traveller.calculateSimilarity(city));
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
  private static void compareCities(ArrayList<City> cities, ArrayList<Traveller> travellers) {
    if (cities.size() == 0 || travellers.size() == 0) {
      System.out.println("Please add at least one city and one traveller");
    } else {
      for (Traveller traveller : travellers) {
        City city = traveller.compareCities(cities);
        System.out.println(
            "Most similar for traveller with coordinates (" + traveller.getGeodesicVector()[0]
                + ", " + traveller.getGeodesicVector()[1] + ") city is: " + city.getName());
      }
    }
  }

  /**
   * Calculate free tickets for each city.
   * 
   * @param cities the list of cities
   * @param travellers the list of travellers
   */
  private static void freeTicket(ArrayList<City> cities, ArrayList<Traveller> travellers) {
    if (cities.size() == 0 || travellers.size() == 0) {
      System.out.println("Please add at least one city and one traveller");
    } else {
      // Convert list to array
      Traveller[] travellerArray = new Traveller[travellers.size()];
      for (int i = 0; i < travellerArray.length; i++) {
        travellerArray[i] = travellers.get(i);
      }
      for (City city : cities) {
        Traveller traveller = city.freeTicket(travellerArray);
        System.out.println("Traveller with coordinates (" + traveller.getGeodesicVector()[0] + ", "
            + traveller.getGeodesicVector()[1] + ")" + " has a free ticket for city "
            + city.getName());
      }
    }
  }
}
