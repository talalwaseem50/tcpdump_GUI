package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML private Button startButton_ID;
    @FXML private Label label;
    @FXML private VBox protocolBox;


    //private Protocol p1 = new Protocol("HTTP");
    ArrayList<Protocol> protocolList;

    public void initialize() {
        label.setText("Initialized");

        ArrayList<String> pNameList = new ArrayList<String>();
        pNameList.add("HTTP");
        pNameList.add("SMTP");
        pNameList.add("VOIP");
        pNameList.add("FTP");

        protocolList = new ArrayList<Protocol>();

        for (String s : pNameList) {
            Protocol pTemp = new Protocol(s);
            protocolList.add(pTemp);

            Label lTemp = new Label();
            lTemp.textProperty().bind(pTemp.valueProperty());
            protocolBox.getChildren().add(lTemp);
        }

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        //label.setText("Running!");
        protocolList.get(0).increment();



        /*String s;
        Process p;
        try {
            p = Runtime.getRuntime().exec("sudo /usr/sbin/tcpdump -c 5");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((s = br.readLine()) != null)
                System.out.println(s);
            //System.out.println("line: " + s);

            p.waitFor();
            System.out.println ("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
            //e.printStackTrace();
        }

         */

    }


}
