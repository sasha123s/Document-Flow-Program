package isedPackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML
    private Label username;
    @FXML
    private CheckBox inBox;
    @FXML
    private CheckBox outBox;
    @FXML
    private CheckBox finishedBox;
    private ConnectionManager connectionManager;
    private String id;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            id = IDGetter.getInstance().getID();
            connectionManager = new ConnectionManager();
            username.setText(connectionManager.getUserByID(Integer.parseInt(id)).getNString("Login"));
            inBox.setSelected(connectionManager.getUserByID(Integer.parseInt(id)).getInt("AccessToIncome") == 1);
            outBox.setSelected(connectionManager.getUserByID(Integer.parseInt(id)).getInt("AccessToOutcome") == 1);
            finishedBox.setSelected(connectionManager.getUserByID(Integer.parseInt(id)).getInt("AccessToFinished") == 1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAccess() throws Exception {
        PreparedStatement st = connectionManager.getStatement().getConnection().prepareStatement("update names set accesstoincome = ? where id = '" + id + "'");
        if(inBox.isSelected()) {
          st.setInt(1, 1);
        } else {
            st.setInt(1, 0);
        }
        int a = st.executeUpdate();
        PreparedStatement st2 = connectionManager.getStatement().getConnection().prepareStatement("update names set accesstooutcome = ? where id = '" + id + "'");
        if (outBox.isSelected()) {
            st2.setInt(1, 1);
        } else {
            st2.setInt(1, 0);
        }
        int b = st2.executeUpdate();
        PreparedStatement st3 = connectionManager.getStatement().getConnection().prepareStatement("update names set accesstofinished = ? where id = '" + id + "'");
        if (finishedBox.isSelected()) {
            st3.setInt(1, 1);
        } else {
            st3.setInt(1, 0);
        }
        int c = st3.executeUpdate();
        Stage stage = (Stage) finishedBox.getScene().getWindow();
        stage.close();
    }

    public void deleteUser() throws Exception {
        PreparedStatement st2 = connectionManager.getStatement().getConnection().prepareStatement("DELETE FROM documents WHERE nameid = '" + id + "' OR signerid = '" + id + "' OR dilovodid = '" + id + "'");
        int b = st2.executeUpdate();
        PreparedStatement st = connectionManager.getStatement().getConnection().prepareStatement("delete from names where id = '" + id + "'");
        int a = st.executeUpdate();

        Stage stage = (Stage) finishedBox.getScene().getWindow();
        stage.close();
    }
}
