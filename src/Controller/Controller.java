package Controller;

import Model.ExtendedLine;
import View.Main;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


public class Controller {
    @FXML
    AnchorPane ap = new AnchorPane();
    static ExtendedLine cur_selected;

    public class ExtendedLineManipulation {

        final ExtendedLineManipulation extendedLineManipulation=this;
        final Delta startDelta = new Delta();
        final Delta endDelta = new Delta();



        public void makeDraggable(ExtendedLine extendedLine) {

            extendedLine.line.setOnMousePressed(lineOnMousePressedEventHandler);
            extendedLine.line.setOnMouseDragged(lineOnMouseDraggedEventHandler);
            extendedLine.first_controller.setOnMousePressed(circleOnMousePressedEventHandler);
            extendedLine.first_controller.setOnMouseDragged(circleOnMouseDraggedEventHandler);
            extendedLine.second_controller.setOnMousePressed(circleOnMousePressedEventHandler);
            extendedLine.second_controller.setOnMouseDragged(circleOnMouseDraggedEventHandler);



        }

        public void makeUnDraggable(ExtendedLine extendedLine) {

            extendedLine.removeEventHandler(MouseEvent.MOUSE_PRESSED , lineOnMousePressedEventHandler);
            extendedLine.removeEventHandler(MouseEvent.MOUSE_DRAGGED, lineOnMouseDraggedEventHandler);

        }




        EventHandler<MouseEvent> ExtendedLineOnMouseClickEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (cur_selected!=null){
                    if (cur_selected.equals(event.getSource())){
                        System.out.println("equals");


                    }
                    else
                    {
                        System.out.println("Not equals");
                        extendedLineManipulation.makeUnDraggable(cur_selected);
                        cur_selected.first_controller.setVisible(false);
                        cur_selected.second_controller.setVisible(false);
                        cur_selected.line.setStroke(Color.BLACK);
                        ExtendedLine extendedLine = (ExtendedLine) event.getSource();
                        cur_selected = extendedLine;
                        extendedLineManipulation.makeDraggable(extendedLine);
                        extendedLine.first_controller.setVisible(true);
                        extendedLine.second_controller.setVisible(true);
                        extendedLine.line.setStroke(Color.ORCHID);
                    }
                }
                else
                {

                    System.out.println("null");
                    ExtendedLine extendedLine = (ExtendedLine) event.getSource();
                    cur_selected = extendedLine;
                    extendedLineManipulation.makeDraggable(extendedLine);
                    extendedLine.first_controller.setVisible(true);
                    extendedLine.second_controller.setVisible(true);
                    extendedLine.line.setStroke(Color.ORCHID);
                }

            }
        };




        EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {

//                orgSceneX = e.getSceneX();
//                orgSceneY = e.getSceneY();
//                System.out.println("CircleEv " + orgSceneX + " "+ orgSceneY);
//                Node p = ((Node) (e.getSource()));
//                //orgTranslateX = p.getTranslateX();
//                //orgTranslateY = p.getTranslateY();
//                //cur_selected.removeEventHandler(MouseEvent.MOUSE_PRESSED , LineOnMousePressedEventHandler);
//                //cur_selected.removeEventHandler(MouseEvent.MOUSE_DRAGGED, LineOnMouseDraggedEventHandler);

            }
        };

        EventHandler<MouseEvent> lineOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                startDelta.x=cur_selected.first_controller.getCenterX()-e.getX();
                startDelta.y=cur_selected.first_controller.getCenterY()-e.getY();
                endDelta.x=cur_selected.second_controller.getCenterX()-e.getX();
                endDelta.y=cur_selected.second_controller.getCenterY()-e.getY();
//                orgSceneX = e.getSceneX();
//                orgSceneY = e.getSceneY();
//                System.out.println("LineEv " + orgSceneX + " "+ orgSceneY);
//                //Node p = ((Node) (e.getSource()));
//                orgTranslateX = cur_selected.getTranslateX();
//                orgTranslateY = cur_selected.getTranslateY();
            }

        };

        EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {

               // double offsetX = e.getSceneX() - orgSceneX;
                //double offsetY = e.getSceneY() - orgSceneY;
                //double newTranslateX = orgTranslateX + offsetX;
                //double newTranslateY = orgTranslateY + offsetY;
                Circle p = ((Circle) (e.getSource()));
               // p.setTranslateX(newTranslateX);
               // p.setTranslateY(newTranslateY);
                p.setCenterX(e.getSceneX());
                p.setCenterY(e.getSceneY());
                //System.out.println("CircleEv " + p.getCenterX() + " "+ p.getCenterY());
               // cur_selected.line.setStartX( p.getCenterX());
               // cur_selected.line.setStartY(p.getCenterY());
            }
        };

        EventHandler<MouseEvent> lineOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {

                cur_selected.first_controller.setCenterX(e.getX()+startDelta.x);
                cur_selected.first_controller.setCenterY(e.getY()+startDelta.y);
                cur_selected.second_controller.setCenterX(e.getX()+endDelta.x);
                cur_selected.second_controller.setCenterY(e.getY()+endDelta.y);

                //Line  p= ((Line) (e.getSource()));
                //p.setStartX(e.getX());

                //double offsetX = e.getSceneX() - orgSceneX;
                //double offsetY = e.getSceneY() - orgSceneY;

                //double newTranslateX = orgTranslateX + offsetX;
                //double newTranslateY = orgTranslateY + offsetY;
               // Node p = ((Node) (e.getSource()));
               // cur_selected.setTranslateX(newTranslateX);
                //cur_selected.setTranslateY(newTranslateY);
            }
        };
    }


    public void AddLineClicked(MouseEvent mouseEvent) {

        ExtendedLine extendedLine = new ExtendedLine();
        ap.getChildren().addAll(extendedLine);
        ExtendedLineManipulation lineManipulation = new ExtendedLineManipulation();
        extendedLine.setOnMouseClicked(lineManipulation.ExtendedLineOnMouseClickEventHandler);

    }

    private class Delta { double x,y;

    }

}

