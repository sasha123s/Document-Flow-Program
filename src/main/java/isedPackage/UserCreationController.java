package isedPackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class UserCreationController implements Initializable {
    @FXML
    private TextField login;
    @FXML
    private TextField password;
    @FXML
    private TextField status;
    @FXML
    private Button button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void saveAndClose() throws Exception {
        ConnectionManager connectionManager = new ConnectionManager();
        PreparedStatement st = connectionManager.getStatement().getConnection().prepareStatement("insert into names(id, login, password, status) values (?, ?, ?, ?)");
        int id = Integer.parseInt(IDGetter.getInstance().getID()) + 1;
        st.setInt(1, id);
        st.setNString(2, login.getText());
        st.setNString(3, password.getText());
        st.setString(4, status.getText());
        int a = st.executeUpdate();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }
}
