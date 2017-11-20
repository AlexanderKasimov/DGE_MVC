package Controller;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

public class Controller {
    @FXML
    AnchorPane ap = new AnchorPane();

    public void AddLineClicked(MouseEvent mouseEvent)
    {


        //DoubleProperty
        Line l1 = new Line(Math.random()*500,Math.random()*500,Math.random()*500+100,Math.random()*500+100);
        ap.getChildren().add(l1);

    }
}
