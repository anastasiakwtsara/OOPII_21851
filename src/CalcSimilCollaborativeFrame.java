
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class CalcSimilCollaborativeFrame {
  private JFrame frame = new JFrame("Calc Simil");

  public CalcSimilCollaborativeFrame(Map<String, City> cities, List<Traveller> travellers,
      DatabaseConnector database) {

    JTextField travellerName = new JTextField("Your fullname");
    travellerName.setBounds(50, 60, 150, 20);
    JButton calcSimilButton = new JButton("Calculate Similarity");
    calcSimilButton.setBounds(50, 150, 150, 20);
    calcSimilButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        try {
          String fullname = travellerName.getText();
          List<Traveller> travellers2 = new ArrayList<>();
          for (Traveller traveller : travellers) {
            if (fullname.equals(traveller.getName())) {
              travellers2.add(traveller);
            }
          }
          Collections.sort(travellers2);
          Traveller tr1 = travellers2.get(0);

          Traveller mostSimilarTraveller = travellers.stream().filter(traveller -> !traveller.getName().equals(fullname))
              .map(traveller -> new AbstractMap.SimpleEntry<>(traveller,
                  calculateSimilarity(traveller, tr1)))
              .max(Comparator.comparing(AbstractMap.SimpleEntry::getValue)).get().getKey();

          JOptionPane.showMessageDialog(frame, "Your most similar city is " + mostSimilarTraveller.getVisit().getName());

        } catch (Exception e) {
          JOptionPane.showMessageDialog(frame,
              "You are not added as a traveller in the system yet");

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
    frame.add(calcSimilButton);
    frame.add(backButton);
    frame.setSize(300, 300);
    frame.setLayout(null);
    frame.setVisible(true);

  }

  private int calculateSimilarity(Traveller tr1, Traveller tr2) {
    int dotProduct = 0;
    for (int i = 0; i < tr1.getTermVectorInterest().length; i++) {
      dotProduct += tr1.getTermVectorInterest()[i] * tr2.getTermVectorInterest()[i];
    }
    return dotProduct;
  }
}
