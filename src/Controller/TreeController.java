package Controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Random;

public class TreeController {
    @FXML
    AnchorPane ap;
    @FXML
    Slider angleSlider;
    @FXML
    Slider probSlider;
    @FXML
    Slider iterationSlider;


    static Double angle_param;
    static Double probability_param;
    static Integer iterations;
    static ArrayList<Line> linesList=new ArrayList<Line>();


    public void Generate(MouseEvent mouseEvent){
        angle_param=angleSlider.getValue();
        probability_param=probSlider.getValue();
        iterations=(int) iterationSlider.getValue();
        for (Line line : linesList) {
            ap.getChildren().remove(line);
        }
        tree(400.0,500.0,-90.0,iterations);
    }


    public void tree(Double x1,Double y1, Double angle, Integer iteration){
        if (iteration==0) return;
        Double r_angle=Math.random()*20-Math.random()*20;
        Double r_prob=Math.random()*15-Math.random()*15;
        Double x2=x1 + (Math.cos(Math.toRadians(angle+r_angle))*iteration*(probability_param+r_prob));
        Double y2=y1 + (Math.sin(Math.toRadians(angle+r_angle))*iteration*(probability_param+r_prob));
        Line line = new Line(x1,y1,x2,y2);
        linesList.add(line);
        ap.getChildren().add(line);
        tree(x2,y2,angle-angle_param,iteration-1);
        tree(x2,y2,angle+angle_param,iteration-1);
    }


}
