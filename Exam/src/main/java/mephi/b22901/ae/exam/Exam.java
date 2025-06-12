

package mephi.b22901.ae.exam;

import mephi.b22901.ae.exam.Connection.DBConnection;
import mephi.b22901.ae.exam.GUI.MainFrame;

/**
 *
 * @author artyom_egorkin
 */
public class Exam {

    public static void main(String[] args) {
        DBConnection.getConnection();
        new MainFrame().setVisible(true);
    }
}
