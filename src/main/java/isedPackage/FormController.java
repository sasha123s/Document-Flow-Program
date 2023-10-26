package isedPackage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class FormController {
    @FXML
    private TextField loginTF = new TextField();
    @FXML
    private PasswordField passwordField = new PasswordField();
    @FXML
    private Label wrong = new Label();

    public void loadClientForm() throws Exception {
        ConnectionManager connectionManager = new ConnectionManager();
        boolean loginVerification = false;
        boolean pwVerification = false;
        String status = "";

        wrong.setVisible(false);

        while (connectionManager.getRSNames().next()) {
            if (Objects.equals(loginTF.getText(), connectionManager.getRSNames().getNString("Login"))) {
                loginVerification = true;
                IDGetter.getInstance().setID(connectionManager.getRSNames().getNString("ID"));
                if (Objects.equals(passwordField.getText(), connectionManager.getRSNames().getNString("Password"))) {
                    pwVerification = true;
                }
                status = connectionManager.getRSNames().getNString("Status");
                break;
            }

        }

        connectionManager.closeConnection();

        if (loginVerification && pwVerification) {
            if (status.equals("0") || status.equals("2")) {
                ClientWindow clientWindow = new ClientWindow(loginTF.getText());
            }
            if (status.equals("1")) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("administrator-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                Stage stage = new Stage();
                stage.setTitle(loginTF.getText());
                stage.setScene(scene);
                stage.setResizable(false);
                stage.getIcons().add( new Image("C:\\Users\\Sasha\\IdeaProjects\\ISED\\src/main/resources/isedPackage/isedIcon.png"));
                stage.show();
            }
        } else {
            wrong.setVisible(true);
        }
    }

}
