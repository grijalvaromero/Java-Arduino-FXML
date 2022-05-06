module com.example.arduino {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;


    opens com.example.arduino to javafx.fxml;
    exports com.example.arduino;
}