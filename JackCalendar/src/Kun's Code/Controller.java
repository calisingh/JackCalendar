import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Controller class represents the controller for the application.
 * 
 * @authors Kunwarpreet, Jooyul, Carrisa
 */
public class Controller extends JPanel {

    private static final long serialVersionUID = 1L;

    public Controller(MyCalendar model) {
        View view = new View(model);
        JButton previousDayButton = new JButton("<");
        previousDayButton.addActionListener(e -> {
            GregorianCalendar cal = model.getGregorianCalendar();
            cal.add(Calendar.DAY_OF_MONTH, -1);
            model.updateListeners(cal);
            view.createButtons(0, cal);
        });
        JButton nextDayButton = new JButton(">");
        nextDayButton.addActionListener(e -> {
            GregorianCalendar cal = model.getGregorianCalendar();
            cal.add(Calendar.DAY_OF_MONTH, 1);
            model.updateListeners(cal);
            view.createButtons(0, cal);
        });
        JButton quitButton = new JButton("Quit");
        quitButton.setPreferredSize(new Dimension(60, 30));
        quitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
        quitButton.addActionListener(e -> {
            model.quit();
            Runtime.getRuntime().exit(0);
        });
        add(previousDayButton);
        add(nextDayButton);
        add(quitButton);
    }
}
