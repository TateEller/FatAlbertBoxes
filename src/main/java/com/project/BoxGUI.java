package com.project;

import java.util.concurrent.BrokenBarrierException;

import javafx.application.*;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class BoxGUI extends Application
{
    Stage window;
    Button type1Button, type2Button, type3Button;
    Button genButton, downButton;

    int boxType = 1;
    float width, height, depth = 100;
    float numTabs;  //I'm not sure if we need this still or if the tab count is auto calculated
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

        //Width input
        Label widthLabel = new Label("Width:");
        GridPane.setConstraints(widthLabel, 0, 0);
        TextField widthInput = new TextField("100");
        GridPane.setConstraints(widthInput, 1, 0);

        //Height input
        Label heightLabel = new Label("Height:");
        GridPane.setConstraints(heightLabel, 0, 1);
        TextField heightInput = new TextField("100");
        GridPane.setConstraints(heightInput, 1, 1);

        //Depth input
        Label depthLabel = new Label("Depth:");
        GridPane.setConstraints(depthLabel, 0, 2);
        TextField depthInput = new TextField("100");
        GridPane.setConstraints(depthInput, 1, 2);

        //Engraving input
        Label engraveLabel = new Label("Engraving:");
        GridPane.setConstraints(engraveLabel, 0, 4);
        TextField engraveInput = new TextField("FA");
        GridPane.setConstraints(engraveInput, 1, 4);

        //Engraving Side input
        Label sideLabel = new Label("Engrave on side:");
        GridPane.setConstraints(sideLabel, 0, 5);
        ChoiceBox<String> sideChoice = new ChoiceBox<>();
        sideChoice.getItems().add("One side, front");   //add one item to dropdown menu
        sideChoice.getItems().addAll("All sides, no top", "All sides, with top");   //add multiple items to dropdown menu
        sideChoice.setValue("One side, front"); //set default value
        GridPane.setConstraints(sideChoice, 1, 5);

        //FileName input
        Label fileLabel = new Label("File name:");
        GridPane.setConstraints(fileLabel, 0, 6);
        TextField fileInput = new TextField("Untitled");
        GridPane.setConstraints(fileInput, 1, 6);

        grid.getChildren().addAll(widthLabel, widthInput, heightLabel, heightInput, depthLabel, 
            depthInput, engraveLabel, engraveInput, sideLabel, sideChoice, fileLabel, fileInput);

        //Buttons for generating and downloading svg
        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            
        });
        Button downloadButton = new Button("Download");
        downloadButton.setOnAction(e -> {
            System.err.println("Generate " + fileInput.getText());
            generateSVG(Float.parseFloat(widthInput.getText()), Float.parseFloat(heightInput.getText()), 
                Float.parseFloat(depthInput.getText()), engraveInput.getText(), fileInput.getText());
        });

        HBox bottomButtons = new HBox(10);
        bottomButtons.setPadding(new Insets(10,10,10,10));
        bottomButtons.getChildren().addAll(generateButton,downloadButton);

        //Set final layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets (10,10,10,10));
        layout.getChildren().addAll(topButtons, grid, bottomButtons);

        //Create scene
        Scene scene = new Scene(layout, 400, 400);
        window.setScene(scene);
        window.show();
    }

    private boolean isInt(TextField input, String value){
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
        try{
            Float.parseFloat(value);
            return true;
        }catch(NumberFormatException e){
            input.setStyle("-fx-control-inner-background: #ff9999;");
            errorMessage("ERROR", "'" + value + "' is not a float.");
            return false;
        }
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

        Box guiBox = new Box(boxType,width,height,depth,10,engraving,"Arial",fileName,1);
        guiBox.print();
    }



}
