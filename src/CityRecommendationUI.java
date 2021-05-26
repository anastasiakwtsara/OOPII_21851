import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CityRecommendationUI {

  private static JSONFileReader json = new JSONFileReader();
  private static DatabaseConnector db = new DatabaseConnector();
  public static JFrame MAIN_FRAME = new JFrame("City Recommendation Application");

  public static void main(String args[]) {
    db.makeJDBCConnection();
    Map<String, City> cities = null;
    try {
      cities = db.ReadCities();
    } catch (SQLException e1) {
      // TODO Auto-generated catch block
      System.out.println("Could not load cities from db");
      cities = new HashMap<>();
    }
    Map<String, City> citiesMap = cities;

    JButton cityButton = new JButton("Add City");
    cityButton.setBounds(50, 50, 150, 20);
    cityButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        new AddCityFrame(db, citiesMap);
      }
    });
    List<Traveller> travellers = null;
    try {
      // Try to read travellers from file
      travellers = json.readJSON();
    } catch (Exception e) {
      // If file does not exist, set a new empty list
      travellers = new ArrayList<>();
      System.out.println("Could not load travellers from json");
    }

    List<Traveller> travellerList = travellers;

    JButton travellerButton = new JButton("Add Traveller");
    travellerButton.setBounds(50, 80, 150, 20);
    travellerButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        MAIN_FRAME.dispose();
        new AddTravellerFrame(json, travellerList);
      }
    });

    JButton calculateSimilaritiesButton = new JButton("Calculate Similarities");
    calculateSimilaritiesButton.setBounds(45, 110, 160, 20);
    calculateSimilaritiesButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {

        JOptionPane.showMessageDialog(MAIN_FRAME, calculateSimilarities(citiesMap, travellerList));
      }
    });

    JButton compareCitiesButton = new JButton("Compare Cities");
    compareCitiesButton.setBounds(50, 140, 150, 20);
    compareCitiesButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
    	  JOptionPane.showMessageDialog(MAIN_FRAME, compareCities(citiesMap, travellerList,json));
    	

      }
    });

    JButton freeTicketButton = new JButton("Free ticket");
    freeTicketButton.setBounds(50, 170, 150, 20);
    freeTicketButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
    	  JOptionPane.showMessageDialog(MAIN_FRAME, freeTicket(citiesMap, travellerList));
      }
    });

    JButton calcSimilButton = new JButton("Calc your simil for an exact city");
    calcSimilButton.setBounds(28, 200, 212, 20);
    calcSimilButton.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent ae) {
    	        MAIN_FRAME.dispose();
    	        new CalcSimilFrame(citiesMap, travellerList, db);
    	      

      }
    });

    MAIN_FRAME.add(cityButton);
    MAIN_FRAME.setDefaultCloseOperation(EXIT_ON_CLOSE);
    MAIN_FRAME.setLayout(null);
    MAIN_FRAME.add(travellerButton);
    MAIN_FRAME.add(calculateSimilaritiesButton);
    MAIN_FRAME.add(compareCitiesButton);
    MAIN_FRAME.add(freeTicketButton);
    MAIN_FRAME.add(calcSimilButton);
    MAIN_FRAME.setLayout(null);
    MAIN_FRAME.setSize(300, 300);
    MAIN_FRAME.setVisible(true);
  }

  /**
   * Calculate similarities for all cities and travellers.
   * 
   * @param cities
   *          the list of cities
   * @param travellers
   *          the list of travellers
   * @return result the result to be displayed as a message
   */
  private static String calculateSimilarities(Map<String, City> cities,
      List<Traveller> travellers) {
    StringBuilder builder = new StringBuilder();
    if (cities.size() == 0 || travellers.size() == 0) {
      builder.append("Please add at least one city and one traveller").append("\n");
    } else {
      for (Map.Entry<String, City> entry : cities.entrySet()) {
        for (Traveller traveller : travellers) {
          builder.append("Calculating similarity for user ").append(traveller.getName())
              .append(" with coordinates (").append(traveller.getGeodesicVector()[0]).append(", ")
              .append(traveller.getGeodesicVector()[1]).append(") and city: ")
              .append(entry.getKey()).append("\n")
              .append(traveller.calculateSimilarity(entry.getValue())).append("\n");
        }
      }
    }
    return builder.toString();
  }
  
  private static String compareCities(Map<String, City> cities, List<Traveller> travellers,JSONFileReader json) {
	  StringBuilder builder = new StringBuilder();
	    if (cities.size() == 0 || travellers.size() == 0) {
	      builder.append("Please add at least one city and one traveller").append("\n");
	    } else {
	      for (Traveller traveller : travellers) {
	        City visit = traveller.compareCities(cities);
	        traveller.setVisit(visit);
	        builder.append("Most similar for traveller ")
	        .append(traveller.getName()).append(" with coordinates (")
	        .append(traveller.getGeodesicVector()[0]).append(", ")
	        .append(traveller.getGeodesicVector()[1]).append(") city is: ").append(visit.getName()).append("\n");
	          
	      }
	    }
	    try {
	          json.writeJSON(travellers);
	          
	        } catch (Exception e) {
	         
	        }
	    return builder.toString();
	  }
  
  private static String freeTicket(Map<String, City> cities, List<Traveller> travellers) {
	  StringBuilder builder = new StringBuilder();
	    if (cities.size() == 0 || travellers.size() == 0) {
	      builder.append("Please add at least one city and one traveller");
	    } else 
	      for (Map.Entry<String, City> entry : cities.entrySet()) {
	        Traveller traveller = entry.getValue().freeTicket(travellers);
	        builder.append("Traveller ").append(traveller.getName()).append("with coordinates (")
	            .append(traveller.getGeodesicVector()[0]).append(", ").append(traveller.getGeodesicVector()[1])
	            .append( ")").append(" has a free ticket for city " ).append(entry.getKey()).append("\n");       
	      } 
	    return builder.toString();
	  }
}
