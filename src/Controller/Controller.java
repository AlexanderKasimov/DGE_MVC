package Controller;

import Model.ExtendedLine;
import View.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Controller {
    @FXML
    AnchorPane ap = new AnchorPane();
    @FXML
    Slider morfSlider;
    @FXML
    VBox matrixBox;
    @FXML
    GridPane matrixGrid;

    static ExtendedLine cur_selected;
    static Boolean ctrlPressed=false;
    static Boolean shiftPressed=false;
    static ArrayList<ExtendedLine> lineList=new ArrayList<ExtendedLine>();
    static ArrayList<ExtendedLine> selectedLineList=new ArrayList<ExtendedLine>();
    static ArrayList<LineGroup> groupOutliner=new ArrayList<LineGroup>();
    static Map<ExtendedLine,ArrayList<Delta>> deltaMap= new HashMap<>();
    static ArrayList<ExtendedLine> morfGroup1=new ArrayList<ExtendedLine>();
    static ArrayList<ExtendedLine> morfGroup2=new ArrayList<ExtendedLine>();
    static ArrayList<ExtendedLine> selectedForMorf=new ArrayList<ExtendedLine>();
    static Color morfColor=Color.RED;
    Map<ExtendedLine,ArrayList<Delta>> morfMap=new HashMap<>();

    public static Integer lineNumber=0;
    public static Integer groupNumber=0;

    public void FormClicked(MouseEvent mouseEvent)
    {
        if (mouseEvent.getButton().equals( MouseButton.SECONDARY))
        {
            for (ExtendedLine extendedLine : lineList) {
                extendedLine.manipulation.makeUnDraggable(extendedLine);
                extendedLine.first_controller.setVisible(false);
                extendedLine.second_controller.setVisible(false);
                extendedLine.line.setStroke(Color.BLACK);

            }

            cur_selected=null;
            morfColor=Color.RED;
            selectedForMorf.clear();
            morfGroup1.clear();
            morfGroup2.clear();
            selectedLineList.clear();
        }

    }

    public void StartMorfingClicked(MouseEvent mouseEvent){
        morfMap.clear();

        ChangeListener<Number> changeListener = this::onSliderChange;



        morfSlider.valueProperty().removeListener(changeListener);


        for (ExtendedLine extendedLine : morfGroup1){
            ArrayList<Delta> deltaList = new ArrayList<Delta>();
            Delta start = new Delta();
            Delta end = new Delta();
            start.x=extendedLine.first_controller.getCenterX();
            start.y=extendedLine.first_controller.getCenterY();
            end.x=extendedLine.second_controller.getCenterX();
            end.y=extendedLine.second_controller.getCenterY();
            deltaList.add(start);
            deltaList.add(end);
            morfMap.put(extendedLine,deltaList);
        }
        morfSlider.valueProperty().addListener(changeListener);

    }

    public void OpenTree(MouseEvent mouseEvent){
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("tree.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage=new Stage();
        stage.setTitle("Tree");
        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }


    public void OpenMatrix(MouseEvent mouseEvent){
        if (matrixBox.isVisible()){
            matrixBox.setVisible(false);
        }
        else
            matrixBox.setVisible(true);

    }


    public void ApplyMatrix(MouseEvent mouseEvent){
        if (!selectedLineList.isEmpty()){
            Double[][] matrix=new Double[3][3];
            Integer row=0;
            Integer col=0;
            for (Node node : matrixGrid.getChildren()) {
                TextField tf =(TextField) node;
                matrix[row][col]=Double.valueOf(tf.getText());
                col++;
                if (col>2){
                    col=0;
                    row++;
                }
            }
            for (ExtendedLine extendedLine : selectedLineList) {

                Double x1 = extendedLine.first_controller.getCenterX();
                Double y1 = extendedLine.first_controller.getCenterY();
                Double x2 = extendedLine.second_controller.getCenterX();
                Double y2 = extendedLine.second_controller.getCenterY();


                Double[] point_1= new Double[3];
                point_1[0]=x1;
                point_1[1]=y1;
                point_1[2]=1.0;
                Double[] point_1_rez= new Double[3];
                point_1_rez[0]=0.0;
                point_1_rez[1]=0.0;
                point_1_rez[2]=0.0;


                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        point_1_rez[i]+=point_1[j]*matrix[j][i];
                    }
                }

                if (point_1_rez[2]!=1.0){
                    for (int i = 0; i < point_1_rez.length; i++) {
                        point_1_rez[i]=point_1_rez[i]/point_1_rez[2];
                    }
                }

                extendedLine.first_controller.setCenterX(point_1_rez[0]);
                extendedLine.first_controller.setCenterY(point_1_rez[1]);

                Double[] point_2= new Double[3];
                point_2[0]=x2;
                point_2[1]=y2;
                point_2[2]=1.0;
                Double[] point_2_rez= new Double[3];
                point_2_rez[0]=0.0;
                point_2_rez[1]=0.0;
                point_2_rez[2]=0.0;


                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        point_2_rez[i]+=point_2[j]*matrix[j][i];
                    }
                }

                if (point_2_rez[2]!=1.0){
                    for (int i = 0; i < point_2_rez.length; i++) {
                        point_2_rez[i]=point_2_rez[i]/point_2_rez[2];
                    }
                }

                extendedLine.second_controller.setCenterX(point_2_rez[0]);
                extendedLine.second_controller.setCenterY(point_2_rez[1]);
            }


        }
        else
        if (cur_selected!=null){

            Double x1 = cur_selected.first_controller.getCenterX();
            Double y1 = cur_selected.first_controller.getCenterY();
            Double x2 = cur_selected.second_controller.getCenterX();
            Double y2 = cur_selected.second_controller.getCenterY();
            Double[][] matrix=new Double[3][3];
            Integer row=0;
            Integer col=0;
            for (Node node : matrixGrid.getChildren()) {
                TextField tf =(TextField) node;
                matrix[row][col]=Double.valueOf(tf.getText());
                col++;
                if (col>2){
                    col=0;
                    row++;
                }
            }

            Double[] point_1= new Double[3];
            point_1[0]=x1;
            point_1[1]=y1;
            point_1[2]=1.0;
            Double[] point_1_rez= new Double[3];
            point_1_rez[0]=0.0;
            point_1_rez[1]=0.0;
            point_1_rez[2]=0.0;


            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    point_1_rez[i]+=point_1[j]*matrix[j][i];
                }
            }

            if (point_1_rez[2]!=1.0){
                for (int i = 0; i < point_1_rez.length; i++) {
                    point_1_rez[i]=point_1_rez[i]/point_1_rez[2];
                }
            }

            cur_selected.first_controller.setCenterX(point_1_rez[0]);
            cur_selected.first_controller.setCenterY(point_1_rez[1]);

            Double[] point_2= new Double[3];
            point_2[0]=x2;
            point_2[1]=y2;
            point_2[2]=1.0;
            Double[] point_2_rez= new Double[3];
            point_2_rez[0]=0.0;
            point_2_rez[1]=0.0;
            point_2_rez[2]=0.0;


            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    point_2_rez[i]+=point_2[j]*matrix[j][i];
                }
            }

            if (point_2_rez[2]!=1.0){
                for (int i = 0; i < point_2_rez.length; i++) {
                    point_2_rez[i]=point_2_rez[i]/point_2_rez[2];
                }
            }

            cur_selected.second_controller.setCenterX(point_2_rez[0]);
            cur_selected.second_controller.setCenterY(point_2_rez[1]);

        }





    }


    public void SaveClicked(MouseEvent mouseEvent){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save List");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(Main.mainStage);

        String lineListInfo = "";
        for (ExtendedLine extendedLine : lineList) {
            lineListInfo+=extendedLine.first_controller.getCenterX() + "," + extendedLine.first_controller.getCenterY()   +","+  extendedLine.second_controller.getCenterX() + "," + extendedLine.second_controller.getCenterY()+ "," +extendedLine.name + ",";
            if (extendedLine.group!=null){
                lineListInfo+=extendedLine.group.name + ";";
            }
            else
                lineListInfo+="null;";
        }
        try (FileWriter fw = new FileWriter(file.getPath())){
            fw.write(lineListInfo);
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public void LoadClicked(MouseEvent mouseEvent){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load List");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(Main.mainStage);
        try (FileReader fr = new FileReader(file.getPath())){
            Scanner sc = new Scanner(fr);
            String lineListInfo = "";
            while(sc.hasNextLine()){
                lineListInfo += sc.nextLine();
            }
            if (!"".equals(lineListInfo)){
                for (ExtendedLine extendedLine : lineList) {
                    ap.getChildren().remove(extendedLine);
                }

                lineList.clear();
                selectedLineList.clear();
                morfGroup1.clear();
                morfGroup2.clear();
                selectedForMorf.clear();
                cur_selected=null;
                String[] lines = lineListInfo.split(";");
                for (String line : lines){
                    String[] coord = line.split(",");
                    ExtendedLine extendedLine = new ExtendedLine();
                    extendedLine.first_controller.setCenterX(Double.valueOf(coord[0]));
                    extendedLine.first_controller.setCenterY(Double.valueOf(coord[1]));
                    extendedLine.second_controller.setCenterX(Double.valueOf(coord[2]));
                    extendedLine.second_controller.setCenterY(Double.valueOf(coord[3]));
                    extendedLine.name=coord[4];
                    ap.getChildren().addAll(extendedLine);
                    ExtendedLineManipulation lineManipulation = new ExtendedLineManipulation();
                    extendedLine.manipulation = lineManipulation;
                    extendedLine.setOnMouseClicked(lineManipulation.ExtendedLineOnMouseClickEventHandler);
                    lineList.add(extendedLine);
                }
            }
            fr.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }



    private void onSliderChange(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        Integer index1 = 0;
        for (ExtendedLine extendedLine : morfGroup1) {
            extendedLine.first_controller.setCenterX(morfMap.get(extendedLine).get(0).x * (1 - newValue.doubleValue()) + morfGroup2.get(index1).first_controller.getCenterX() * newValue.doubleValue());
            extendedLine.first_controller.setCenterY(morfMap.get(extendedLine).get(0).y * (1 - newValue.doubleValue()) + morfGroup2.get(index1).first_controller.getCenterY() * newValue.doubleValue());
            extendedLine.second_controller.setCenterX(morfMap.get(extendedLine).get(1).x * (1 - newValue.doubleValue()) + morfGroup2.get(index1).second_controller.getCenterX() * newValue.doubleValue());
            extendedLine.second_controller.setCenterY(morfMap.get(extendedLine).get(1).y * (1 - newValue.doubleValue()) + morfGroup2.get(index1).second_controller.getCenterY() * newValue.doubleValue());
            index1++;
        }

    }




    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().getName().equals("Ctrl")){
           ctrlPressed = true;
        }
        if (keyEvent.getCode().getName().equals("Shift")){
           shiftPressed = true;
        }

        if (keyEvent.getCode().getName().equals("1")){
            for (ExtendedLine extendedLine : selectedForMorf) {
                morfGroup1.add(extendedLine);
            }
            morfColor=Color.GREEN;
            selectedForMorf.clear();

        }

        if (keyEvent.getCode().getName().equals("2")){
            for (ExtendedLine extendedLine : selectedForMorf) {
                morfGroup2.add(extendedLine);
            }
            morfColor=Color.RED;
            selectedForMorf.clear();
        }

        if (keyEvent.getCode().getName().equals("Delete")){
            if (cur_selected!=null){
                lineList.remove(cur_selected);
                ap.getChildren().remove(cur_selected);
                cur_selected=null;
            }
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
                        //add to children_list?

                        groupOutliner.get(groupOutliner.indexOf(curGroup)).parentGroup=lineGroup;
                    }
                }

            }



            for (LineGroup lineGroup1 : groupOutliner) {
                if (lineGroup1.parentGroup!=null)
                System.out.println(lineGroup1.name+" parent:" +lineGroup1.parentGroup.name);
                else
                    System.out.println(lineGroup1.name+" parent: Null");
            }

        }

        if (keyEvent.getCode().getName().equals("U")){
            if (!selectedLineList.isEmpty()){
                LineGroup rez_group=null;
                for (ExtendedLine extendedLine : selectedLineList) {
                    LineGroup parent;
                    if (extendedLine.group!=null){
                        LineGroup cur_group=extendedLine.group;
                        parent = cur_group.parentGroup;
                        while (parent!=null) {
                            cur_group=parent;
                            parent=cur_group.parentGroup;
                        }
                        rez_group=cur_group;
                        if (extendedLine.group.parentGroup==null){
                            extendedLine.group=null;
                        }

                    }
                }
                if (rez_group!=null){
                    for (LineGroup lineGroup : groupOutliner) {
                        if (lineGroup.parentGroup==rez_group){
                            lineGroup.parentGroup=null;
                        }
                    }
                    groupOutliner.remove(rez_group);
                }

            }
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getCode().getName().equals("Ctrl")){
            ctrlPressed = false;
        }
        if (keyEvent.getCode().getName().equals("Shift")){
            shiftPressed = false;
        }

    }


    public class ExtendedLineManipulation {

        final ExtendedLineManipulation extendedLineManipulation=this;
        final Delta startDelta = new Delta();
        final Delta endDelta = new Delta();



        public void makeDraggable(ExtendedLine extendedLine) {

            extendedLine.line.setOnMousePressed(lineOnMousePressedEventHandler);
            extendedLine.line.setOnMouseDragged(lineOnMouseDraggedEventHandler);
            extendedLine.first_controller.setOnMouseDragged(circleOnMouseDraggedEventHandler);
            extendedLine.second_controller.setOnMouseDragged(circleOnMouseDraggedEventHandler);
        }

        public void makeUnDraggable(ExtendedLine extendedLine) {

            extendedLine.line.setOnMousePressed(null);
            extendedLine.line.setOnMouseDragged(null);
            extendedLine.first_controller.setOnMouseDragged(null);
            extendedLine.second_controller.setOnMouseDragged(null);
        }

        EventHandler<MouseEvent> ExtendedLineOnMouseClickEventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //System.out.println("Started!");
                if (shiftPressed){
                    ExtendedLine extendedLine = (ExtendedLine) event.getSource();
                    selectedForMorf.add(extendedLine);
                    extendedLine.line.setStroke(morfColor);
                }
                else

                if (ctrlPressed) {

                    ExtendedLine extendedLine = (ExtendedLine) event.getSource();
                    selectedLineList.add((ExtendedLine)event.getSource());
                    extendedLine.line.setStroke(Color.hsb(182.9268, 0.6891, 0.9333));
                    if (extendedLine.group!=null) {
                        LineGroup next_group=extendedLine.group;
                        do {
                            for (ExtendedLine line: lineList){
                                if ((line.group!=null)&&(selectedLineList.lastIndexOf(line)==-1)&&(line.group.equals(next_group))){
                                    selectedLineList.add(line);
                                    line.line.setStroke(Color.hsb(182.9268, 0.6891, 0.9333));
                                }
                                else
                                {
                                    if ((line.group!=null)&&(line.group.parentGroup!=null)&&(selectedLineList.lastIndexOf(line)==-1)&&(line.group.parentGroup.equals(next_group))){
                                        selectedLineList.add(line);
                                        line.line.setStroke(Color.hsb(182.9268, 0.6891, 0.9333));
                                    }

                                }
                            }

                            next_group=next_group.parentGroup;

                        }
                        while (next_group!=null);
                    }









                    //if (cur_selected.group!=null)

                    for (ExtendedLine line : selectedLineList) {
                        //System.out.println(line.name);
                        line.manipulation.makeDraggable(line);
                    }



                } else
                    if (selectedLineList.isEmpty())
                {

                    for (ExtendedLine line : selectedLineList) {
                        //System.out.println(line.name);
                        line.manipulation.makeUnDraggable(line);
                    }
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



        EventHandler<MouseEvent> lineOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
               if (!selectedLineList.isEmpty()) {
                   System.out.println("SelectedLines");
                   deltaMap.clear();
                   for (ExtendedLine extendedLine : selectedLineList) {
                       ArrayList<Delta> deltaList = new ArrayList<Delta>();
                       Delta start = new Delta();
                       Delta end = new Delta();
                       start.x=extendedLine.first_controller.getCenterX()-e.getX();
                       start.y=extendedLine.first_controller.getCenterY()-e.getY();
                       end.x=extendedLine.second_controller.getCenterX()-e.getX();
                       end.y=extendedLine.second_controller.getCenterY()-e.getY();
                       deltaList.add(start);
                       deltaList.add(end);
                       deltaMap.put(extendedLine,deltaList);
                   }

               }
               else{
                     System.out.println("Pressed!");
                   startDelta.x=cur_selected.first_controller.getCenterX()-e.getX();
                   startDelta.y=cur_selected.first_controller.getCenterY()-e.getY();
                   endDelta.x=cur_selected.second_controller.getCenterX()-e.getX();
                   endDelta.y=cur_selected.second_controller.getCenterY()-e.getY();
               }



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

                if (!selectedLineList.isEmpty()){
                    for (ExtendedLine extendedLine : selectedLineList) {
                        extendedLine.first_controller.setCenterX(e.getX()+deltaMap.get(extendedLine).get(0).x);
                        extendedLine.first_controller.setCenterY(e.getY()+deltaMap.get(extendedLine).get(0).y);
                        extendedLine.second_controller.setCenterX(e.getX()+deltaMap.get(extendedLine).get(1).x);
                        extendedLine.second_controller.setCenterY(e.getY()+deltaMap.get(extendedLine).get(1).y);

                    }
                }
                else
                {
                    cur_selected.first_controller.setCenterX(e.getX()+startDelta.x);
                    cur_selected.first_controller.setCenterY(e.getY()+startDelta.y);
                    cur_selected.second_controller.setCenterX(e.getX()+endDelta.x);
                    cur_selected.second_controller.setCenterY(e.getY()+endDelta.y);

                }

            }
        };
    }


    public void AddLineClicked(MouseEvent mouseEvent) {

        lineNumber++;
        ExtendedLine extendedLine = new ExtendedLine();
        ap.getChildren().addAll(extendedLine);
        ExtendedLineManipulation lineManipulation = new ExtendedLineManipulation();
        extendedLine.manipulation = lineManipulation;
        extendedLine.setOnMouseClicked(lineManipulation.ExtendedLineOnMouseClickEventHandler);
        lineList.add(extendedLine);


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

