package isedPackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class DocumentCreationController implements Initializable {
    @FXML
    private Label name;
    @FXML
    private MenuItem saveAndClose;
    @FXML
    private TextArea documentText;
    private ConnectionManager connectionManager;
    private String id;

    /*  private String getNextOrderNumber() throws Exception {
        ResultSet rs = connectionManager.getStatement().executeQuery("select count(1)+1 n from Documents where DocumentName like 'Документ%'");
        rs.next();
        return rs.getNString("n");
    }*/

    private int getNextIdOfDocument() throws Exception {
        //ResultSet rs = connectionManager.getStatement().executeQuery("select count(*)+1 n from Documents");
        ResultSet rs = connectionManager.getStatement().executeQuery("SELECT * FROM Documents ORDER BY id DESC LIMIT 1");
        rs.next();
        return rs.getInt("id");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            id = IDGetter.getInstance().getID();
            connectionManager = new ConnectionManager();

            name.setText("Документ№" + (getNextIdOfDocument() + 1));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setSaveAndClose() throws Exception {
        String document = "Документ№" + (getNextIdOfDocument() + 1);
        String text = documentText.getText();
        String query = "insert into Documents(ID, NameID, DocumentName, DocumentText, Status, DilovodID) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connectionManager.getStatement().getConnection().prepareStatement(query);
        statement.setInt(1, getNextIdOfDocument() + 1);
        statement.setInt(2, Integer.parseInt(id));
        statement.setString(3, document);
        statement.setString(4, text);
        statement.setInt(5, 1);
        statement.setInt(6, 2);
        int rs = statement.executeUpdate();
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }
}
