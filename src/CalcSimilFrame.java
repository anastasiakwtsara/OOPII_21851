
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


import javax.swing.JButton;
	import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

	public class CalcSimilFrame {
	  private JFrame frame = new JFrame("Calc Simil");

	  public CalcSimilFrame(Map<String, City> cities,List<Traveller> travellers,DatabaseConnector database) {

	    JTextField travellerName = new JTextField("Your fullname");
	    travellerName.setBounds(50, 60, 150, 20);
	    JTextField cityName = new JTextField("Enter city");
	    cityName.setBounds(50, 80, 150, 20);
	    JButton calcSimilButton = new JButton("Calc Simil");
	    calcSimilButton.setBounds(50, 150, 150, 20);
	    calcSimilButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	    	  try {
			      System.out.println("Give me ur full name");
			      String fullname = travellerName.getText();
			      Traveller tr1 = null;
			      List<Traveller> travellers2 = new ArrayList<>();
			      for (Traveller traveller : travellers) {
			        if (fullname.equals(traveller.getName())) {
			          travellers2.add(traveller);
			          
			        }
			        
			      }
			 Collections.sort(travellers2);
			 tr1=travellers.get(0);
			      String cityname = cityName.getText();
			      City chosenCity = cities.get(cityname);
			      
			      if (chosenCity != null) {
			        double d = tr1.calculateSimilarity(chosenCity);
			        JOptionPane.showMessageDialog(frame,"Your similarity for the city you chose is " + d);
			      } else { 
			        City city = new City();
			        city.setName(cityname);
			        city.retrieveWikiAndLocationData();
			        cities.put(cityname, city);
			        database.addDataToDB(cityname, city.getCountry(), city.getGeodesicVector()[0],city.getGeodesicVector()[1], city.getTermsVector()[0], city.getTermsVector()[1], city.getTermsVector()[2], city.getTermsVector()[3], city.getTermsVector()[4], city.getTermsVector()[5], city.getTermsVector()[6], city.getTermsVector()[7], city.getTermsVector()[8], city.getTermsVector()[9]);
			        
			        double d = tr1.calculateSimilarity(city);
			        JOptionPane.showMessageDialog(frame,"Your similarity for the city you chose is " + d);
			      }
			    } catch (Exception e) {
			    	JOptionPane.showMessageDialog(frame,"You are not added as a traveller in the system yet");

			    }
			  }
	       
	    
	    });

	    JButton backButton = new JButton("Back to menu");
	    backButton.setBounds(50, 180, 150, 20);
	    backButton.addActionListener(new ActionListener() {

	      @Override
	      public void actionPerformed(ActionEvent e) {
	        CityRecommendationUI.MAIN_FRAME.setVisible(true);
	        frame.setVisible(false);
	        frame.dispose();
	      }
	    });

	    frame.add(travellerName);
	    frame.add(cityName);
	    frame.add(calcSimilButton);
	    frame.add(backButton);
	    frame.setSize(300, 300);
	    frame.setLayout(null);
	    frame.setVisible(true);
	  
	}
	  }
	

	
		
		   
		    

		  
