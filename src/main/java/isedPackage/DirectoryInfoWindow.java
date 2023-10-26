package isedPackage;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DirectoryInfoWindow {
    public DirectoryInfoWindow(int num) {
        AnchorPane pane = new AnchorPane();
        Label info = new Label("Кількість елементів: " + num);
        pane.getChildren().add(info);
        Scene scene = new Scene(pane, 300, 100);
        Stage stage = new Stage();
        stage.setTitle("Властивості папки");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setX(600);
        stage.setY(400);
        stage.getIcons().add( new Image("C:\\Users\\Sasha\\IdeaProjects\\ISED\\src/main/resources/isedPackage/isedIcon.png"));
        stage.show();
    }
}
