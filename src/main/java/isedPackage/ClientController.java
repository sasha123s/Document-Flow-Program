package isedPackage;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    private String id;
    @FXML
    private TreeView treeView;
    @FXML
    private ListView<String> listView;
    @FXML
    private Label directoryName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ConnectionManager connectionManager = new ConnectionManager();
            TreeItem<String> rootItem = new TreeItem<>("Папки");
            TreeItem<String> rootItem1 = new TreeItem<>("Документи");
            TreeItem<String> branchIn = new TreeItem<>("Вхідні");
            TreeItem<String> branchOut = new TreeItem<>("Вихідні");
            TreeItem<String> branchFinished = new TreeItem<>("Завершені");
            TreeItem<String> rootItem2 = new TreeItem<>("Організація");
            TreeItem<String> branchItem4 = new TreeItem<>("Будівельна компанія");
            TreeItem<String> allDocuments = new TreeItem<>("Документи організації");

            id = IDGetter.getInstance().getID();
            if (connectionManager.getUserByID(Integer.parseInt(id)).getInt("AccessToIncome") == 1) {
                rootItem1.getChildren().add(branchIn);
            }
            if (connectionManager.getUserByID(Integer.parseInt(id)).getInt("AccessToOutcome") == 1) {
                rootItem1.getChildren().add(branchOut);
            }
            if (connectionManager.getUserByID(Integer.parseInt(id)).getInt("AccessToFinished") == 1) {
                rootItem1.getChildren().add(branchFinished);
            }
            rootItem2.getChildren().setAll(branchItem4, allDocuments);
            rootItem.getChildren().setAll(rootItem1, rootItem2);

            treeView.setRoot(rootItem);

            treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        if (mouseEvent.getClickCount() == 2) {
                            TreeItem<String> selectedItem = (TreeItem<String>) treeView.getSelectionModel().getSelectedItem();
                            directoryName.setText(selectedItem.getValue());
                            listView.getItems().clear();
                            if (selectedItem != null) {
                                String where = "";
                                if (selectedItem == branchFinished) {
                                    where = "WHERE (? in (d.NameID, d.SignerID, d.DilovodID) and d.Status = 5) or (? = d.SignerID and d.Status in (3, 4))";
                                } else if (selectedItem == branchOut) {
                                    where = " WHERE d.NameID = ? and d.Status in (1, 2, 3, 4)";
                                } else if (selectedItem == branchIn) {
                                    where = " WHERE (d.SignerID = ? and d.Status = 2 ) or (d.DilovodID = ? and d.Status = 4)";
                                } else if (selectedItem == branchItem4) {
                                    String query = "SELECT Login FROM Names d where d.Status in (0, 2)";
                                    PreparedStatement statement = connectionManager.getStatement().getConnection().prepareStatement(query);
                                    ResultSet resultSet = statement.executeQuery();
                                    while (resultSet.next()) {
                                        listView.getItems().add(resultSet.getNString("Login"));
                                    }
                                } else if (selectedItem == allDocuments) {
                                    String query = "SELECT * FROM Documents d join DocumentState s on s.id = d.status";
                                    PreparedStatement statement = connectionManager.getStatement().getConnection().prepareStatement(query);
                                    ResultSet resultSet = statement.executeQuery();
                                    while (resultSet.next()) {
                                        listView.getItems().add(resultSet.getNString("DocumentName") + " (" + resultSet.getString("DocumentStateName") + ")");
                                    }
                                }
                                if (!where.equals("")) {
                                    String query = "SELECT DocumentName, s.DocumentStateName FROM Documents d join DocumentState s on s.id = d.status " + where;
                                    PreparedStatement statement = connectionManager.getStatement().getConnection().prepareStatement(query);
                                    statement.setString(1, id);
                                    if (selectedItem == branchFinished || selectedItem == branchIn) {
                                        statement.setString(2, id);
                                    }
                                    ResultSet resultSet = statement.executeQuery();
                                    while (resultSet.next()) {
                                        listView.getItems().add(resultSet.getString("DocumentName") + " (" + resultSet.getString("DocumentStateName") + ")");
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        if (mouseEvent.getClickCount() == 2) {
                            if (listView.getSelectionModel().getSelectedItem().contains("Документ")) {
                                IDGetter.getInstance().setID(connectionManager.getDocumentByName(listView.getSelectionModel().getSelectedItem()).getNString("ID"));
                                IDGetter.getInstance().setUserID(id);
                                DocumentWindow documentWindow = new DocumentWindow(listView.getSelectionModel().getSelectedItem());
                            }
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

    public void callOrderWindow() throws Exception {
        IDGetter.getInstance().setID(id);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("document-creation-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Новий документ");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add( new Image("C:\\Users\\Sasha\\IdeaProjects\\ISED\\src/main/resources/isedPackage/isedIcon.png"));
        stage.show();
    }

    public void callDirectoryInfo() {

        DirectoryInfoWindow directoryInfoWindow = new DirectoryInfoWindow(listView.getItems().size());
    }

}