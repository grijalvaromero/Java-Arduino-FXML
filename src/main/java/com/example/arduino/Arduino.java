package com.example.arduino;
import com.fazecast.jSerialComm.SerialPort;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Arduino implements Runnable{
    int baudRate =9600;
    int dataBits= 8;
    int stopBit = SerialPort.ONE_STOP_BIT;
    int parity = SerialPort.NO_PARITY;
    SerialPort[] serialPorts;
    SerialPort mySerialPort;
    TextArea txtConsola;
    XYChart.Series<Double, Double> series1;
    public Arduino(TextArea txtConsola,XYChart.Series<Double, Double> series1){
        this.txtConsola =txtConsola;
        this.series1= series1;
    }

    private String info;
    public ArrayList<String> getPorts(){
        serialPorts = SerialPort.getCommPorts();
        ArrayList<String> ports=new ArrayList<String>();
        for(SerialPort port: serialPorts){
            ports.add(port.toString());
            System.out.println(port.toString());
        }
        return ports;
    }
    public boolean isOpen(){
        return mySerialPort.isOpen();
    }

    public String getInfo() {
        return info;
    }
    public void connect(int num){
        mySerialPort = serialPorts[num];
        mySerialPort.setComPortParameters(baudRate, dataBits, stopBit,parity);
        mySerialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING,1000,0);
        mySerialPort.openPort();
        info = "Info \n"+
                "Port Name: "+mySerialPort.getSystemPortName()+" \n"+
                "BaudRate: "+mySerialPort.getBaudRate()+" \n"+
                "DataBit: "+mySerialPort.getNumDataBits()+" \n"+
                "StopBit: "+mySerialPort.getNumStopBits()+" \n"+
                "Parity: "+mySerialPort.getParity()+" \n"+
                "ReadTimeout: "+mySerialPort.getReadTimeout()+" \n";

    }

    public void desconectar(){
        if(isOpen()){
            mySerialPort.closePort();
        }
    }
    public void write(byte number){
        byte[] writeByte =new byte[1];
        writeByte[0] = number;
        int byteTx= mySerialPort.writeBytes(writeByte, 1);
    }

    @Override
    public void run() {
        mySerialPort.flushIOBuffers();
        try {
            while(true) {

                byte[] readBuffer = new byte[100];
                int numRead = mySerialPort.readBytes(readBuffer, readBuffer.length);
                String lectura = new String(readBuffer, "UTF-8");
                if(lectura.contains("TEMP")){
                    txtConsola.appendText("\n"+lectura);
                    //TEMP,100.00
                    String[] arS=lectura.split(",");
                    double temp=Double.parseDouble(arS[1]);
                    double segund= LocalDateTime.now().getSecond();
                    series1.getData().add(new XYChart.Data<Double, Double>(segund,temp));
                }

            }//llave while
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
