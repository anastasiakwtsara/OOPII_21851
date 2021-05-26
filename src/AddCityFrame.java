
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddCityFrame {

  private JFrame frame = new JFrame("Add City");

  public AddCityFrame(DatabaseConnector database, Map<String, City> citiesMap) {

    JTextField cityName = new JTextField("Add city name");
    cityName.setBounds(50, 50, 150, 20);
    JTextField countryCode = new JTextField("Add country code");
    countryCode.setBounds(50, 80, 150, 20);
    JButton addCityButton = new JButton("Add City");
    addCityButton.setBounds(50, 110, 150, 20);
    addCityButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        String name = cityName.getText();
        String code = countryCode.getText();
        City city = new City(name, code);
        city.retrieveWikiAndLocationData();
        database.addDataToDB(name, code, city.getGeodesicVector()[0], city.getGeodesicVector()[1],
            city.getTermsVector()[0], city.getTermsVector()[1], city.getTermsVector()[2],
            city.getTermsVector()[3], city.getTermsVector()[4], city.getTermsVector()[5],
            city.getTermsVector()[6], city.getTermsVector()[7], city.getTermsVector()[8],
            city.getTermsVector()[9]);
        citiesMap.put(name, city);
        JOptionPane.showMessageDialog(frame, "City successfully saved!");
      }
    });
    
    JButton backButton = new JButton("Back to menu");
    backButton.setBounds(50, 140, 150, 20);
    backButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        CityRecommendationUI.MAIN_FRAME.setVisible(true);
        frame.setVisible(false);
        frame.dispose();
      }
    });
    
    frame.add(cityName);
    frame.add(countryCode);
    frame.add(addCityButton);
    frame.add(backButton);
    frame.setSize(300, 300);
    frame.setLayout(null);
    frame.setVisible(true);
  }

}
