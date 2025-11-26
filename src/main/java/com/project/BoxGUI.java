package com.project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class BoxGUI extends Application
{
    Stage window;
    Button type1Button, type2Button, type3Button;
    Button genButton, downButton;

    int boxType = 1;
    float width, height, depth = 100;
    float numTabs;  //I'm not sure if we need this still or if the tab count is auto calculated
    int engravingSide = 1;
    String engraving = "FA";
    String font = "";   //I don't think font is needed either. It doesnt seem to be assigned anywhere
    String fileName = "Untitled";
    float conversion = 1;   //I don't know what this does

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Fat Albert Box Generator");
        
        //Buttons for selecting type of box
        type1Button = new Button("Base Box");
        type1Button.setOnAction(e -> boxType = 1);
        type2Button = new Button("Closed Box");
        type2Button.setOnAction(e -> boxType = 2);
        type3Button = new Button("Type 3 Box");
        type3Button.setOnAction(e -> boxType = 3);

        HBox topButtons = new HBox(10);
        topButtons.setPadding(new Insets(10,10,10,10));
        topButtons.getChildren().addAll(type1Button, type2Button, type3Button);

        //Grid for the user input of variables
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10,10,10,10));
        grid.setVgap(5);
        grid.setHgap(8);

        //Unit input
        Label unitLabel = new Label("Unit:");
        GridPane.setConstraints(unitLabel, 0, 0);
        ChoiceBox<String> unitChoice = new ChoiceBox<>();
        unitChoice.getItems().addAll("Millimeters", "Inches");
        unitChoice.setValue("Millimeters");
        GridPane.setConstraints(unitChoice, 1, 0);
        //Doesn't change anything yet

        //Width input
        Label widthLabel = new Label("Width:");
        GridPane.setConstraints(widthLabel, 0, 1);
        TextField widthInput = new TextField("100");
        GridPane.setConstraints(widthInput, 1, 1);
        widthInput.setId("Width Input");
        final String[] lastValue = {widthInput.getText()};
        widthInput.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if(newFocus){
                widthInput.setStyle("-fx-control-inner-background: #ffffffff;");
                return;
            }
            if(!newFocus){  //When user clicks off box
                String current = widthInput.getText();
                
                if(!current.equals(lastValue[0])){  //If value has changed
                    System.out.println("New width");
                    isFloat(widthInput, current);      //Check if it is a valid float
                    //check if it is in min/max range
                }   
            }
        });
        

        //Height input
        Label heightLabel = new Label("Height:");
        GridPane.setConstraints(heightLabel, 0, 2);
        TextField heightInput = new TextField("100");
        GridPane.setConstraints(heightInput, 1, 2);
        heightInput.setId("Height Input");
        
        heightInput.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if(newFocus){
                heightInput.setStyle("-fx-control-inner-background: #ffffffff;");
                return;
            }
            if(!newFocus){  //When user clicks off box
                String current = heightInput.getText();
                
                if(!current.equals(lastValue[0])){  //If value has changed
                    System.out.println("New height");
                    isFloat(heightInput, current);      //Check if it is a valid float
                    //check if it is in min/max range
                }   
            }
        });

        //Depth input
        Label depthLabel = new Label("Depth:");
        GridPane.setConstraints(depthLabel, 0, 3);
        TextField depthInput = new TextField("100");
        GridPane.setConstraints(depthInput, 1, 3);
        depthInput.setId("Depth Input");
        depthInput.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if(newFocus){
                depthInput.setStyle("-fx-control-inner-background: #ffffffff;");
                return;
            }
            if(!newFocus){  //When user clicks off box
                String current = depthInput.getText();
                
                if(!current.equals(lastValue[0])){  //If value has changed
                    System.out.println("New depth");
                    isFloat(depthInput, current);      //Check if it is a valid float
                }   
            }
        });

        //Engraving input
        Label engraveLabel = new Label("Engraving:");
        GridPane.setConstraints(engraveLabel, 0, 4);
        TextField engraveInput = new TextField("FA");
        GridPane.setConstraints(engraveInput, 1, 4);
        engraveInput.setId("Engraving Input");
        engraveInput.setOnMouseClicked(e -> {
            engraveInput.setStyle("-fx-control-inner-background: #ffffffff;");
        });
        engraveInput.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if(!newFocus){  //When user clicks off box
                String current = engraveInput.getText();
                
                if(!current.equals(lastValue[0])){  //If value has changed
                    System.out.println("New engraving");
                    checkEngravement(engraveInput, current);      //Check if it is a valid value
                }   
            }
        });

        //Engraving Side input
        Label sideLabel = new Label("Engrave on side:");
        GridPane.setConstraints(sideLabel, 0, 5);
        ChoiceBox<String> sideChoice = new ChoiceBox<>();
        sideChoice.getItems().add("One side, width side");   //add one item to dropdown menu
        sideChoice.getItems().addAll("Two sides, opposite sides", "Three sides", "All four sides");   //add multiple items to dropdown menu
        sideChoice.setValue("One side, front"); //set default value
        GridPane.setConstraints(sideChoice, 1, 5);
        //Set engravingSide variable
        sideChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newvalue) -> {
            switch(newvalue){
                case "One side, width side":
                    engravingSide = 1;
                    break;
                case "Two sides, opposite sides":
                    engravingSide = 2;
                    break;
                case "Three sides":
                    engravingSide = 3;
                    break;
                case "All four sides":
                    engravingSide = 4;
                    break;
            }
        });

        //FileName input
        Label fileLabel = new Label("File name:");
        GridPane.setConstraints(fileLabel, 0, 6);
        TextField fileInput = new TextField("Untitled");
        GridPane.setConstraints(fileInput, 1, 6);
        fileInput.setId("File Name Input");

        grid.getChildren().addAll(unitLabel, unitChoice, widthLabel, widthInput, heightLabel, heightInput, depthLabel, 
            depthInput, engraveLabel, engraveInput, sideLabel, sideChoice, fileLabel, fileInput);

        //Buttons for generating and downloading svg
        /*
        Button previewButton = new Button("Preview");
        previewButton.setOnAction(e -> {
            Boolean correctInput = true;
            if(!isFloat(widthInput, widthInput.getText()))
                correctInput = false;
            if(!isFloat(heightInput, heightInput.getText()))
                correctInput = false;
            if(!isFloat(depthInput, depthInput.getText()))
                correctInput = false;
            
            if(!checkEngravement(engraveInput, engraveInput.getText()))
                correctInput = false;
            //check fileName

            //if correct generate preview
        });  
        
        
    private boolean isInRange(TextField input, String value){
        if(!hasInput(input, value)) return false;

        try{
            float val = Float.parseFloat(value);
            if(val < 30 || val > 1000){
                input.setStyle("-fx-control-inner-background: #ff9999;");
                errorMessage("ERROR", "'" + value + "' is out of range. Must be between 30 and 1000.");
                return false;
            }
            return true;
        }catch(NumberFormatException e){
            input.setStyle("-fx-control-inner-background: #ff9999;");
            errorMessage("ERROR", "'" + value + "' is not a float.");
            return false;
        }
    }
        
        */
        Button downloadButton = new Button("Download");
        downloadButton.setOnAction(e -> {
            System.err.println("Generate " + fileInput.getText());
            generateSVG(Float.parseFloat(widthInput.getText()), Float.parseFloat(heightInput.getText()), 
                Float.parseFloat(depthInput.getText()), engraveInput.getText(), fileInput.getText());
        });

        HBox bottomButtons = new HBox(10);
        bottomButtons.setPadding(new Insets(10,10,10,10));
        bottomButtons.getChildren().addAll(downloadButton); //add preview button

        //Set left layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets (10,10,10,10));
        layout.getChildren().addAll(topButtons, grid, bottomButtons);

        /*        JavaFx doesn't support diplaying image as SVG
        //Image preview
        Image image = new Image("file:exports/preview.svg"); 
        ImageView view = new ImageView(image);

        //set final layout
        HBox screen = new HBox(10);
        screen.setPadding(new Insets(10,10,10,10));
        screen.getChildren().addAll(layout, view);  */

        //Create scene
        Scene scene = new Scene(layout, 400, 400);
        window.setScene(scene);
        window.show();
    }

    private boolean isInt(TextField input, String value){
        if(!hasInput(input, value)) return false;
        
        try{
            Integer.parseInt(value);
            return true;
        }catch(NumberFormatException e){
            input.setStyle("-fx-control-inner-background: #ff9999;");
            errorMessage("ERROR", "'" + value + "' is not a integer.");
            return false;
        }
    }

    private boolean isFloat(TextField input, String value){
        if(!hasInput(input, value)) return false;
        
        try{
            float num = Float.parseFloat(value);

            if(num < 30){
                input.setStyle("-fx-control-inner-background: #ff9999;");
                errorMessage("ERROR", "'" + input.getId() + "'' can not be lower than 30.");
                return false;
            }

            return true;
        }catch(NumberFormatException e){
            input.setStyle("-fx-control-inner-background: #ff9999;");
            errorMessage("ERROR", "'" + value + "' is not a float.");
            return false;
        }
    }

    private boolean checkEngravement(TextField input, String value){
        if(!hasInput(input, value)) return false;

        Pattern pattern = Pattern.compile(".*\\d.*");
        Matcher matcher = pattern.matcher(value);

        if(value.length() > 2 || matcher.matches()){    // Ensures that the engraving is two characters and has no numbers
            input.setStyle("-fx-control-inner-background: #ff9999;");
            errorMessage("ERROR", "'" + value + "' can only be 2 letters.");
            return false;
        } 
        else
            return true;
    }

    private boolean hasInput(TextField input, String value){
            if(value.isEmpty()){
                input.setStyle("-fx-control-inner-background: #ff9999;");
                errorMessage("ERROR", "'" + input.getId() +"' can not be empty.");
                return false;
            }
            else return true;
    }

    private void errorMessage(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(100);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Ok");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private void generateSVG(float width, float height, float depth, String engraving, String fileName){

        Box guiBox = new Box(boxType,width,height,depth,10,engraving,"Arial",engravingSide,fileName,1);
        guiBox.print();
    }



}
