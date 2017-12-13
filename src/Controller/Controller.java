package Controller;

import Model.ExtendedLine;
import Model.ExtendedLineList;
import View.Main;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;


public class Controller {
    @FXML
    AnchorPane ap = new AnchorPane();
    static ExtendedLine cur_selected;
    static Boolean ctrlPressed=false;
    static ArrayList<ExtendedLine> lineList=new ArrayList<ExtendedLine>();
    static ArrayList<ExtendedLine> selectedLineList=new ArrayList<ExtendedLine>();
    static ArrayList<LineGroup> groupOutliner=new ArrayList<LineGroup>();

    public static Integer lineNumber=0;
    public static Integer groupNumber=0;


    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().getName().equals("Ctrl")){
           ctrlPressed = true;
        }
        if (keyEvent.getCode().getName().equals("G")){
            groupNumber++;
            LineGroup lineGroup = new LineGroup("Group"+groupNumber);
            groupOutliner.add(lineGroup);

            for (ExtendedLine line : selectedLineList) {
                if (line.group==null)
                {
                    line.group=lineGroup;
                }
                else
                {
                    //LineGroup parent;
                    LineGroup curGroup=line.group;
                    do {

                        //parent=parent.parentGroup;

                        if (curGroup.parentGroup!=null)
                        {
                            curGroup=groupOutliner.get(groupOutliner.indexOf(curGroup.parentGroup));
                        }

                    } while (curGroup.parentGroup!=null);

                    if (!curGroup.equals(lineGroup))
                    {
                        groupOutliner.get(groupOutliner.indexOf(curGroup)).parentGroup=lineGroup;
                    }
                }

            }

//            for (ExtendedLine line : lineList) {
//                if (line.group!=null)
//                System.out.println(line.group.name);
//            }

            for (LineGroup lineGroup1 : groupOutliner) {
                if (lineGroup1.parentGroup!=null)
                System.out.println(lineGroup1.name+" parent:" +lineGroup1.parentGroup.name);
                else
                    System.out.println(lineGroup1.name+" parent: Null");
            }

        }


    }

    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode().getName().equals("Ctrl")){
            ctrlPressed = false;
        }
    }


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

                if (ctrlPressed) {

                    ExtendedLine extendedLine = (ExtendedLine) event.getSource();
                    selectedLineList.add((ExtendedLine)event.getSource());
                    extendedLine.line.setStroke(Color.hsb(182.9268, 0.6891, 0.9333));

//                    for (ExtendedLine line : selectedLineList) {
//                        System.out.println(line.name);
//                    }



                } else

                {
                    selectedLineList.clear();

                    if (cur_selected != null) {
                        if (cur_selected.equals(event.getSource())) {
                           // System.out.println("equals");


                        } else {
                            //System.out.println("Not equals");
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
                    } else {

                        //System.out.println("null");
                        ExtendedLine extendedLine = (ExtendedLine) event.getSource();
                        cur_selected = extendedLine;
                        extendedLineManipulation.makeDraggable(extendedLine);
                        extendedLine.first_controller.setVisible(true);
                        extendedLine.second_controller.setVisible(true);
                        extendedLine.line.setStroke(Color.ORCHID);
                    }

                }
            }
        };

        EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {

            }
        };

        EventHandler<MouseEvent> lineOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {

                startDelta.x=cur_selected.first_controller.getCenterX()-e.getX();
                startDelta.y=cur_selected.first_controller.getCenterY()-e.getY();
                endDelta.x=cur_selected.second_controller.getCenterX()-e.getX();
                endDelta.y=cur_selected.second_controller.getCenterY()-e.getY();

            }

        };

        EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {

                Circle p = ((Circle) (e.getSource()));
                p.setCenterX(e.getSceneX());
                p.setCenterY(e.getSceneY());

            }
        };

        EventHandler<MouseEvent> lineOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {

                cur_selected.first_controller.setCenterX(e.getX()+startDelta.x);
                cur_selected.first_controller.setCenterY(e.getY()+startDelta.y);
                cur_selected.second_controller.setCenterX(e.getX()+endDelta.x);
                cur_selected.second_controller.setCenterY(e.getY()+endDelta.y);

            }
        };
    }


    public void AddLineClicked(MouseEvent mouseEvent) {

        lineNumber++;
        ExtendedLine extendedLine = new ExtendedLine();
        ap.getChildren().addAll(extendedLine);
        ExtendedLineManipulation lineManipulation = new ExtendedLineManipulation();
        extendedLine.setOnMouseClicked(lineManipulation.ExtendedLineOnMouseClickEventHandler);
        lineList.add(extendedLine);
//        for (ExtendedLine line : extendedLineList){
//            System.out.println(line.name);
//        }

    }

    private class Delta { double x,y;

    }

    public class LineGroup
    {

       public String name;
       public LineGroup parentGroup;

        public LineGroup(String name) {
            this.name = name;
        }
    }



}

