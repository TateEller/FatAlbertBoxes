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
    TextField widthInput;   // <-- now class-wide
    TextField heightInput;
    TextField depthInput;

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
    float conversion = 2.83465f;   //I don't know what this does
    int tabCount = 5;

    // Internal raw measurements are ALWAYS stored in millimeters:
    float widthMM = 100;
    float heightMM = 100;
    float depthMM = 100;

    boolean usingInches = false;  // false = mm, true = inches

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Fat Albert Box Generator");

        widthInput = new TextField("100");
        heightInput = new TextField("100");
        depthInput = new TextField("100");
        
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
        //Set unit variable
        unitChoice.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newvalue) -> {
            switch(newvalue){
                default:
                case "Millimeters":
                    conversion = 2.83465f;
                    widthInput.setText(String.valueOf(Float.parseFloat(widthInput.getText())/0.03937008f));
                    heightInput.setText(String.valueOf(Float.parseFloat(heightInput.getText())/0.03937008f));
                    depthInput.setText(String.valueOf(Float.parseFloat(depthInput.getText())/0.03937008f));
                    break;
                case "Inches":
                    conversion = 72f;
                    widthInput.setText(String.valueOf(Float.parseFloat(widthInput.getText())*0.03937008f));
                    heightInput.setText(String.valueOf(Float.parseFloat(heightInput.getText())*0.03937008f));
                    depthInput.setText(String.valueOf(Float.parseFloat(depthInput.getText())*0.03937008f));
                    break;
            }
        });


        //Width input
        Label widthLabel = new Label("Width:");
        GridPane.setConstraints(widthLabel, 0, 1);
        widthInput = new TextField("100");
        GridPane.setConstraints(widthInput, 1, 1);
        widthInput.setId("Width Input");
        final String[] lastInput = {widthInput.getText()};
        widthInput.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if(newFocus){
                widthInput.setStyle("-fx-control-inner-background: #ffffffff;");
                return;
            }
            if(!newFocus){  //When user clicks off box
                String current = widthInput.getText();
                
                if(!current.equals(lastInput[0])){  //If value has changed
                    System.out.println("New width");
                    isFloat(widthInput, current);      //Check if it is a valid float
                    //check if it is in min/max range
                }   
            }
        });
        

        //Height input
        Label heightLabel = new Label("Height:");
        GridPane.setConstraints(heightLabel, 0, 2);
        heightInput = new TextField("100");
        GridPane.setConstraints(heightInput, 1, 2);
        heightInput.setId("Height Input");
        
        heightInput.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if(newFocus){
                heightInput.setStyle("-fx-control-inner-background: #ffffffff;");
                return;
            }
            if(!newFocus){  //When user clicks off box
                String current = heightInput.getText();
                
                if(!current.equals(lastInput[0])){  //If value has changed
                    System.out.println("New height");
                    isFloat(heightInput, current);      //Check if it is a valid float
                    //check if it is in min/max range
                }   
            }
        });

        //Depth input
        Label depthLabel = new Label("Depth:");
        GridPane.setConstraints(depthLabel, 0, 3);
        depthInput = new TextField("100");
        GridPane.setConstraints(depthInput, 1, 3);
        depthInput.setId("Depth Input");
        depthInput.focusedProperty().addListener((obs, oldFocus, newFocus) -> {
            if(newFocus){
                depthInput.setStyle("-fx-control-inner-background: #ffffffff;");
                return;
            }
            if(!newFocus){  //When user clicks off box
                String current = depthInput.getText();
                
                if(!current.equals(lastInput[0])){  //If value has changed
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
                
                if(!current.equals(lastInput[0])){  //If value has changed
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
            //Get input values
            width = Float.parseFloat(widthInput.getText());
            height = Float.parseFloat(heightInput.getText());
            depth = Float.parseFloat(depthInput.getText());
            engraving = engraveInput.getText();
            fileName = fileInput.getText();

            convertMeasurments(conversion);
            calculateNumTab();
            System.err.println("Generate " + fileInput.getText());
            generateSVG(width, height, depth, engraving, fileName);
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

            if((num < 30 || num > 508) && conversion == 2.83465f){
                input.setStyle("-fx-control-inner-background: #ff9999;");
                errorMessage("ERROR", "'" + input.getId() + "'' can not be lower than 30 and can not be higher than 508.");
                return false;
            }
            if((num < 1.5 || num > 20) && conversion == 72f){
                input.setStyle("-fx-control-inner-background: #ff9999;");
                errorMessage("ERROR", "'" + input.getId() + "'' can not be lower than 1.5 and can not be higher than 20.");
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

    private void convertMeasurments(float conv)
    {
        conversion = conv;
        System.out.print("Converting " + width + "x" + height + "x" + depth);

        width = width * conv;
        height = height * conv;
        depth = depth * conv;

        System.out.println(" to " + width + "x" + height + "x" + depth);
    }

    private void calculateNumTab()
    {
        float shortSide = Math.min(width, Math.min(height, depth));

    }

    private void generateSVG(float width, float height, float depth, String engraving, String fileName){
        System.out.println("Passing " + width + "x" + height + "x" + depth);

        Box guiBox = new Box(boxType,width,height,depth,tabCount,engraving,"Arial",engravingSide,fileName,conversion);
        guiBox.print();
    }
}