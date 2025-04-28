package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.util.List;

public class HelloController {

    @FXML
    private Label hourLabel, depletedLabel, gridLabel;

    @FXML
    private TextField startField, endField;

    @FXML
    private TableView<Usage> usageTable;
    @FXML
    private TableColumn<Usage, String> hourColumn;
    @FXML
    private TableColumn<Usage, Double> producedColumn, usedColumn, gridUsedColumn;

    @FXML
    private void initialize() {

        hourColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHour().toString())
        );
        producedColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCommunityProduced())
        );
        usedColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCommunityUsed())
        );
        gridUsedColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getGridUsed())
        );
    }

    @FXML
    protected void onFetchCurrentButtonClick() {
        try {
            CurrentPercentage cp = RestClient.fetchCurrentPercentage();
            hourLabel.setText(cp.getHour().toString());
            depletedLabel.setText(cp.getCommunityDepleted() + " %");
            gridLabel.setText(cp.getGridPortion() + " %");
        } catch (IOException e) {
            hourLabel.setText("Error!");
            depletedLabel.setText("");
            gridLabel.setText("");
        }
    }

    @FXML
    protected void onFetchHistoricalButtonClick() {
        try {
            List<Usage> usageList = RestClient.fetchHistoricalUsage(startField.getText(), endField.getText());
            ObservableList<Usage> observableList = FXCollections.observableArrayList(usageList);
            usageTable.setItems(observableList);
        } catch (IOException e) {

            usageTable.setItems(FXCollections.observableArrayList());
        }
    }
}
