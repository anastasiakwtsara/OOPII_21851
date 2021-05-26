import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddTravellerFrame {
  private JFrame frame = new JFrame("Add Traveller");

  public AddTravellerFrame(JSONFileReader json, List<Traveller> travellers) {

    JTextField travellerName = new JTextField("Your fullname");
    travellerName.setBounds(50, 60, 150, 20);
    JTextField travellerAge = new JTextField("Your age");
    travellerAge.setBounds(50, 80, 150, 20);
    JTextField lat = new JTextField("Your latitude");
    lat.setBounds(50, 100, 150, 20);
    JTextField longt = new JTextField("Your longtitude");
    longt.setBounds(50, 120, 150, 20);
    JButton addTravellerButtons = new JButton("Add Traveller");
    addTravellerButtons.setBounds(50, 150, 150, 20);
    addTravellerButtons.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        String name = travellerName.getText();
        int age = Integer.parseInt(travellerAge.getText());
        double latitude = Double.parseDouble(lat.getText());
        double longtitude = Double.parseDouble(longt.getText());
        int[] termVector = new int[10];

        Traveller traveller = null;
        if (age >= 16 && age <= 25) {
          traveller = new YoungTraveller();

          traveller.setName(name);
        } else if (age > 25 && age <= 60) {
          traveller = new MiddleTraveller();
        } else {
          traveller = new ElderTraveller();
        }
        double[] geodesicVector = { latitude, longtitude };
        traveller.setName(name);
        traveller.setGeodesicVector(geodesicVector);
        traveller.setTimestamp((System.currentTimeMillis()));
        Random r = new Random();
        for (int i = 0; i < termVector.length; i++) {
          termVector[i] = r.nextInt(11);
        }
        traveller.setTermVectorInterest(termVector);
        travellers.add(traveller);
        try {
          json.writeJSON(travellers);
          JOptionPane.showMessageDialog(frame, "Traveller successfully saved!");
        } catch (Exception e) {
          System.out.println("Could not write travellers to json file");
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
    frame.add(travellerAge);
    frame.add(lat);
    frame.add(longt);
    frame.add(addTravellerButtons);
    frame.add(backButton);
    frame.setSize(300, 300);
    frame.setLayout(null);
    frame.setVisible(true);
  }
}
