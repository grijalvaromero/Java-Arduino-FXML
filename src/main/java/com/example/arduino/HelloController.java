package com.example.arduino;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HelloController {
   @FXML ComboBox comboPorts;
   @FXML Button btnConnect;
   @FXML Label lblInfo;
   @FXML TextArea txtConsola;
   @FXML LineChart<Double, Double> lineChart;
   @FXML NumberAxis lineXAxis;
   @FXML NumberAxis lineYAxis;
   ObservableList<XYChart.Series<Double, Double>> lineChartData= FXCollections.observableArrayList();
   XYChart.Series<Double, Double> series1= new XYChart.Series<Double,Double>();

   boolean isConnected=false;
    Thread hilo;
    Arduino arduino;
    @FXML protected void initialize(){
        arduino=new Arduino(txtConsola,series1);
        hilo=new Thread(arduino);

        ArrayList<String> ports=arduino.getPorts();
        for(String p: ports){
            comboPorts.getItems().add(p);
        }
        lineXAxis.setLabel("HORA");
        lineYAxis.setLabel("Temperatura");
        series1.setName("Temperatura");
        //series1.getData().add(new XYChart.Data<Double, Double>(0.0,1.0));
       // series1.getData().add(new XYChart.Data<Double, Double>(0.2,2.0));
       // series1.getData().add(new XYChart.Data<Double, Double>(0.3,10.0));
        lineChart.setData(lineChartData);
        lineChart.getData().add(series1);
        //TEMP,33.20


    }

    public void connectar(ActionEvent event){
        if(isConnected){
            btnConnect.setText("Connectar");
            btnConnect.setStyle("-fx-background-color: #1d1d1d");
            arduino.desconectar();
            hilo.stop();
        }else{
            if(comboPorts.getSelectionModel().getSelectedIndex() < 0 ){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Arduino");
                alert.setContentText("Favor de seleccionar un puerto");
                alert.show();
            }else{
                arduino.connect(comboPorts.getSelectionModel().getSelectedIndex());
                btnConnect.setText("STOP");
                btnConnect.setStyle("-fx-background-color: green");
                lblInfo.setText(arduino.getInfo());
                hilo.start();
            }

        }//llave del else
        isConnected= !isConnected;
    }
    public void encenderLed(ActionEvent event){
        String c="1";
        arduino.write( c.getBytes(StandardCharsets.UTF_8)[0] );
    }
    public void apagarLed(ActionEvent event){
        String c="2";
        arduino.write( c.getBytes(StandardCharsets.UTF_8)[0] );
    }
}