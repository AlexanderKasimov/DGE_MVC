package View;

import Controller.Controller;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Main extends Application {

    public Canvas canvas;
    //Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root2 = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("DGE_MVC");
        primaryStage.setScene(new Scene(root2, 800, 600));
       // controller=new Controller();
        //controller.handleCtrlKey();

        canvas = new Canvas(300, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }

    public void AddLineClicked(MouseEvent mouseEvent) {
    }
}
