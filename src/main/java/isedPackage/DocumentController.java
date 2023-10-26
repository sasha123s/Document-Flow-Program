package isedPackage;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class DocumentController implements Initializable {
    private ConnectionManager connectionManager;
    @FXML
    private Label name;
    @FXML
    private TextArea documentText;
    @FXML
    private MenuItem saveAndClose;
    @FXML
    private Label pidpis;
    @FXML
    private Label register;
    @FXML
    private Button button;
    @FXML
    private Label author;
    private String documentID;
    private int userID;
    private int status;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            documentID = IDGetter.getInstance().getID();
            userID = Integer.parseInt(IDGetter.getInstance().getUserID());
            connectionManager = new ConnectionManager();
            name.setText(connectionManager.getDocumentByID(documentID).getNString("DocumentName"));
            documentText.setText(connectionManager.getDocumentByID(documentID).getNString("DocumentText"));
            documentText.positionCaret(documentText.getText().length());
            author.setText("Автор: " + connectionManager.getAuthorByDocumentID(documentID).getNString("Login"));
            ResultSet rs = connectionManager.getDocumentByID(documentID);
            status = rs.getInt("Status");
            if (status == 2) {
                if (connectionManager.getDocumentByID(documentID).getInt("SignerID") == userID) {
                    button.setVisible(true);
                    button.setText("ПІДПИСАТИ");
                }
            } else if (status == 3 || status == 4) {
                pidpis.setStyle("-fx-background-color: green");
                if (status == 4 && connectionManager.getDocumentByID(documentID).getInt("DilovodID") == userID) {
                    button.setVisible(true);
                    button.setText("ЗАРЕЄСТРУВАТИ");
                }
            } else if (status == 5) {
                pidpis.setStyle("-fx-background-color: green");
                register.setStyle("-fx-background-color: green");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDocument()throws Exception {
        PreparedStatement statement = connectionManager.getStatement().getConnection().prepareStatement("delete from Documents where ID = ?");
        statement.setInt(1, Integer.parseInt(documentID));
        int a = statement.executeUpdate();
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    public void setSaveAndClose() throws Exception {
        PreparedStatement statement = connectionManager.getStatement().getConnection().prepareStatement("update Documents set DocumentText = ? where ID = ?");
        statement.setString(1, documentText.getText());
        statement.setInt(2, Integer.parseInt(documentID));
        int a = statement.executeUpdate();
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    public void setButtonListener() throws Exception {
        PreparedStatement statement = connectionManager.getStatement().getConnection().prepareStatement("update Documents set Status = ? where ID = ?");
        if (status == 2) {
            statement.setInt(1, 3);
            statement.setInt(2, Integer.parseInt(documentID));
            int a = statement.executeUpdate();
            status = 3;
        } else if (status == 4) {
            statement.setInt(1, 5);
            statement.setInt(2, Integer.parseInt(documentID));
            int a = statement.executeUpdate();
            status = 5;
        }
    }

    /*public void setPidpisItem() throws Exception {
        if (status != 5) {
            AnchorPane pane = new AnchorPane();
            final Button OKButton = new Button("ОК");
            OKButton.setLayoutX(250);
            OKButton.setLayoutY(70);
            Label choose = new Label("Оберіть підписувача");
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setLayoutY(30);
            PreparedStatement st = connectionManager.getStatement().getConnection().prepareStatement("select * from Names where Status in (?, ?)");
            st.setInt(1, 0);
            st.setInt(2, 2);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                comboBox.getItems().add(rs.getNString("Login"));
            }
            pane.getChildren().add(choose);
            pane.getChildren().add(comboBox);
            pane.getChildren().add(OKButton);
            Scene scene = new Scene(pane, 300, 100);
            Stage stage = new Stage();
            stage.setTitle("На підпис");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            OKButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        PreparedStatement selectStatement = st.getConnection().prepareStatement("select * from Names where Login = ?");
                        selectStatement.setNString(1, comboBox.getSelectionModel().getSelectedItem());
                        ResultSet rs2 = selectStatement.executeQuery();
                        rs2.next();
                        int signerID = rs2.getInt("id");
                        PreparedStatement statement = connectionManager.getStatement().getConnection().prepareStatement("update Documents set Status = ? where ID = ? and SignerID = ?");
                        statement.setInt(1, 2);
                        statement.setInt(2, Integer.parseInt(documentID));
                        statement.setInt(3, signerID);
                        int a = statement.executeUpdate();
                        stage.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


        } else {
            AnchorPane pane = new AnchorPane();
            Label label = new Label("Документ вже було зареєстровано.");
            final Button OKButton = new Button("ОК");
            OKButton.setLayoutX(200);
            OKButton.setLayoutY(70);
            pane.getChildren().add(label);
            Scene scene = new Scene(pane, 300, 100);
            Stage stage = new Stage();
            stage.setTitle("На реєстрацію");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            OKButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    stage.close();
                }
            });
        }
    }*/
    public void setPidpisItem() throws Exception {
        if (status != 5) {
            AnchorPane pane = new AnchorPane();
            final Button OKButton = new Button("ОК");
            OKButton.setLayoutX(250);
            OKButton.setLayoutY(70);
            Label choose = new Label("Оберіть підписувача");
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.setLayoutY(30);
            PreparedStatement st = connectionManager.getStatement().getConnection().prepareStatement("select * from Names where Status in (?, ?)");
            st.setInt(1, 0);
            st.setInt(2, 2);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                comboBox.getItems().add(rs.getNString("Login"));
            }
            pane.getChildren().add(choose);
            pane.getChildren().add(comboBox);
            pane.getChildren().add(OKButton);
            Scene scene = new Scene(pane, 300, 100);
            Stage stage = new Stage();
            stage.setTitle("На підпис");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add( new Image("C:\\Users\\Sasha\\IdeaProjects\\ISED\\src/main/resources/isedPackage/isedIcon.png"));
            stage.show();
            OKButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        PreparedStatement selectStatement = connectionManager.getStatement().getConnection().prepareStatement("select * from Names where Login = ?");
                        selectStatement.setNString(1, comboBox.getSelectionModel().getSelectedItem());
                        System.out.println(comboBox.getSelectionModel().getSelectedItem());
                        ResultSet rs2 = selectStatement.executeQuery();
                        rs2.next();
                        int signerID = rs2.getInt("id");
                        PreparedStatement statement = connectionManager.getStatement().getConnection().prepareStatement("update Documents set Status = ? , SignerID = ? where ID = ?");
                        statement.setInt(1, 2);
                        statement.setInt(2, signerID);
                        statement.setInt(3, Integer.parseInt(documentID));

                        int rowsUpdated = statement.executeUpdate();
                        stage.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            AnchorPane pane = new AnchorPane();
            Label label = new Label("Документ вже було зареєстровано.");
            final Button OKButton = new Button("ОК");
            OKButton.setLayoutX(250);
            OKButton.setLayoutY(70);
            pane.getChildren().add(label);
            pane.getChildren().add(OKButton);
            Scene scene = new Scene(pane, 300, 100);
            Stage stage = new Stage();
            stage.setTitle("На підпис");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add( new Image("C:\\Users\\Sasha\\IdeaProjects\\ISED\\src/main/resources/isedPackage/isedIcon.png"));
            stage.show();
            OKButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    stage.close();
                }
            });
        }
    }

    public void setRegisterItem() throws Exception {
        if (status == 3) {
            PreparedStatement statement = connectionManager.getStatement().getConnection().prepareStatement("update Documents set Status = ? where ID = ?");
            statement.setInt(1, 4);
            statement.setInt(2, Integer.parseInt(documentID));
            int a = statement.executeUpdate();
            AnchorPane pane = new AnchorPane();
            Label label = new Label("Документ відправлено на реєстрацію.");
            final Button OKButton = new Button("ОК");
            OKButton.setLayoutX(250);
            OKButton.setLayoutY(70);
            pane.getChildren().add(label);
            pane.getChildren().add(OKButton);
            Scene scene = new Scene(pane, 300, 100);
            Stage stage = new Stage();
            stage.setTitle("На реєстрацію");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add( new Image("C:\\Users\\Sasha\\IdeaProjects\\ISED\\src/main/resources/isedPackage/isedIcon.png"));
            stage.show();
            OKButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    stage.close();
                }
            });
        } else if (status == 5) {
            AnchorPane pane = new AnchorPane();
            Label label = new Label("Документ вже було зареєстровано.");
            final Button OKButton = new Button("ОК");
            OKButton.setLayoutX(250);
            OKButton.setLayoutY(70);
            pane.getChildren().add(label);
            pane.getChildren().add(OKButton);
            Scene scene = new Scene(pane, 300, 100);
            Stage stage = new Stage();
            stage.setTitle("На реєстрацію");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add( new Image("C:\\Users\\Sasha\\IdeaProjects\\ISED\\src/main/resources/isedPackage/isedIcon.png"));
            stage.show();
            OKButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    stage.close();
                }
            });
        } else {
            AnchorPane pane = new AnchorPane();
            Label label = new Label("Документ ще не підписаний.");
            final Button OKButton = new Button("ОК");
            OKButton.setLayoutX(250);
            OKButton.setLayoutY(70);
            pane.getChildren().add(label);
            pane.getChildren().add(OKButton);
            Scene scene = new Scene(pane, 300, 100);
            Stage stage = new Stage();
            stage.setTitle("На реєстрацію");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add( new Image("C:\\Users\\Sasha\\IdeaProjects\\ISED\\src/main/resources/isedPackage/isedIcon.png"));
            stage.show();
            OKButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    stage.close();
                }
            });
        }
    }
}
