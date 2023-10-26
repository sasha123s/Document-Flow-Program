package isedPackage;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdministratorController implements Initializable {
    @FXML
    private ListView<String> listView;
    private ConnectionManager connectionManager;
    private String id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            id = IDGetter.getInstance().getID();
            connectionManager = new ConnectionManager();
            refresh();
            listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        if (mouseEvent.getClickCount() == 2) {
                            IDGetter.getInstance().setID(connectionManager.getUserByName(listView.getSelectionModel().getSelectedItem()).getNString("id"));
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("user-view.fxml"));
                            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                            Stage stage = new Stage();
                            stage.setTitle(listView.getSelectionModel().getSelectedItem());
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.getIcons().add( new Image("C:\\Users\\Sasha\\IdeaProjects\\ISED\\src/main/resources/isedPackage/isedIcon.png"));
                            stage.show();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void refresh() throws Exception {
        listView.getItems().clear();
        String query = "SELECT Login FROM Names where id != '" + id + "'";
        PreparedStatement statement = connectionManager.getStatement().getConnection().prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            listView.getItems().add(resultSet.getNString("Login"));
        }
    }

    public void createUser() throws Exception {
        ResultSet rs = connectionManager.getStatement().executeQuery("SELECT * FROM names ORDER BY id DESC LIMIT 1");
        rs.next();
        IDGetter.getInstance().setID(rs.getNString("id"));
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("user-creation.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 380, 150);
        Stage stage = new Stage();
        stage.setTitle("Новий користувач");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add( new Image("C:\\Users\\Sasha\\IdeaProjects\\ISED\\src/main/resources/isedPackage/isedIcon.png"));
        stage.show();
    }
}