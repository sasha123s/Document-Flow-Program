package isedPackage;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DocumentWindow {
    public DocumentWindow(String name) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("document-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle(name);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add( new Image("C:\\Users\\Sasha\\IdeaProjects\\ISED\\src/main/resources/isedPackage/isedIcon.png"));
        stage.show();
    }
}
