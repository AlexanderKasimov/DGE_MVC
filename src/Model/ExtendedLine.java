package Model;

import Controller.Controller;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;

public class ExtendedLine extends Group {


    public String name;
    public Line line;
    public Circle first_controller;
    public Circle second_controller;
    public Controller.LineGroup group;
    public Controller.ExtendedLineManipulation manipulation;

    public ExtendedLine()
    {

        line = new Line(Math.random()*300,Math.random()*300,Math.random()*300+100,Math.random()*300+100);
        name ="Line "+ Controller.lineNumber ;
        DropShadow ds = new DropShadow();
        ds.setOffsetX(3.0);
        ds.setOffsetY(3.0);
        //ds.setSpread(20);
        ds.setColor(Color.RED);
        //line.setEffect(ds);
        line.setStrokeWidth(3);
        first_controller = new Circle(line.getStartX(),line.getStartY(),4);
        first_controller.setStroke(Color.hsb(1, 1, 1, 0.5   ));
        first_controller.setStrokeWidth(2);
        first_controller.setStrokeType(StrokeType.OUTSIDE);
        first_controller.setFill(Color.hsb(1,1,1,0));
        first_controller.setVisible(false);

        second_controller = new Circle(line.getEndX(),line.getEndY(),4);
        second_controller.setStroke(Color.hsb(1, 1, 1, 0.5   ));
        second_controller.setStrokeWidth(2);
        second_controller.setStrokeType(StrokeType.OUTSIDE);
        second_controller.setFill(Color.hsb(1,1,1,0));
        second_controller.setVisible(false);
        line.startXProperty().bind(first_controller.centerXProperty());
        line.startYProperty().bind(first_controller.centerYProperty());
        line.endXProperty().bind(second_controller.centerXProperty());
        line.endYProperty().bind(second_controller.centerYProperty());

        this.getChildren().addAll( line,first_controller,second_controller);
    }


}
