package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {

    @FXML private Button startButton_ID;
    @FXML private Label status_ID;
    @FXML private VBox protocolBox;
    @FXML private TableView packetTable;

    private MultithreadedRun mRun;
    private volatile boolean flag = false;

    ArrayList<Protocol> protocolList;
    ArrayList<Packet> packetList = null;

    public void initialize() {
        ArrayList<String> pNameList = new ArrayList<String>();
        pNameList.add("HTTP");
        pNameList.add("SMTP");
        pNameList.add("VOIP");
        pNameList.add("FTP");
        pNameList.add("DNS");
        pNameList.add("POP3");
        pNameList.add("TELNET");
        pNameList.add("NTP");
        pNameList.add("ARP");
        pNameList.add("Others");
        pNameList.add("Total");

        protocolList = new ArrayList<Protocol>();
        for (String s : pNameList) {
            Protocol pTemp = new Protocol(s);
            protocolList.add(pTemp);

            Label lTemp = new Label();
            lTemp.textProperty().bind(pTemp.valueProperty());
            protocolBox.getChildren().add(lTemp);
        }

        String packetComponents[] = {"time","IP","length","srcAddress","destAddress","headerData"};
        for(int i = 0; i < packetTable.getColumns().size(); i++) {
            TableColumn dataColumn = (TableColumn) packetTable.getColumns().get(i);
            dataColumn.setCellValueFactory(new PropertyValueFactory<>(packetComponents[i]));
        }

        status_ID.setText("Ready!");
    }

    @FXML
    private void handleResetButtonAction(ActionEvent event) {
        packetTable.getItems().clear();
        status_ID.setText("Ready!");
        startButton_ID.setText("Start");
        flag = false;
        for(int i = 0; i < protocolList.size(); ++i)
            protocolList.get(i).clear();
    }

    @FXML
    private void handleStartButtonAction(ActionEvent event) {

        if (flag == false) {
            status_ID.setText("Running!");
            startButton_ID.setText("Stop");

            flag = true;
            mRun = new MultithreadedRun();
            mRun.start();
        }
        else {
            flag = false;
            status_ID.setText("Stopped!");
            startButton_ID.setText("Start");
        }
    }

    @FXML
    private void handleGraphButtonAction(ActionEvent event) {
        try {
            Parent root1 = FXMLLoader.load(getClass().getResource("graph.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Graph");
            stage.setScene(new Scene(root1));
            stage.show();
        }
        catch (Exception e) {
            System.out.println("Error Building Graph");
            e.printStackTrace();
        }
    }

    /*  --------------------------------------------------------------------- */
    private class MultithreadedRun extends Thread
    {
        public void run()
        {
            try
            {
                packetList = new ArrayList<Packet>();
                String s;
                Process p;
                p = Runtime.getRuntime().exec("sudo /usr/sbin/tcpdump -c 50 -vv");
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

                while ((s = br.readLine()) != null && flag == true) {
                    System.out.println(s);
                    StringTokenizer tokenizer = new StringTokenizer(s);
                    String time = null;
                    String IP = null;
                    String length = null;
                    String headerData = null;
                    String srcAddress = null;
                    String destAddress = null;
                    String packetData = null;
                    String appProtocol = null;
                    if(!s.contains("IP6") && !s.contains("ARP") && s.contains("IP"))
                    {
                        time = tokenizer.nextToken(" ");
                        time = time.substring(0,8);
                        IP = tokenizer.nextToken(" ");
                        headerData = tokenizer.nextToken(")").toString().concat("))");
                        length = tokenizer.nextToken(")");
                        length = length.substring(2);
                        s = br.readLine();
                        s = s.substring(4);
                        tokenizer = new StringTokenizer(s);
                        srcAddress = tokenizer.nextToken(">");
                        destAddress = tokenizer.nextToken(":");
                        destAddress = destAddress.substring(2);
                        packetData = tokenizer.nextToken("\n");
                        //System.out.println(s);
                    }
                    else if(s.contains("IP6")){
                        time = tokenizer.nextToken(" ");
                        time = time.substring(0,8);
                        IP = tokenizer.nextToken(" ");
                        headerData = tokenizer.nextToken(")").toString().concat("))");
                        length = tokenizer.nextToken(")");
                        length = length.substring(9);
                        srcAddress = tokenizer.nextToken(">");
                        destAddress = tokenizer.nextToken(":");
                        destAddress = destAddress.substring(2);

                    }else if(s.contains("ARP"))
                    {
                        time = " ";
                        IP = " ";
                        length = " ";
                        headerData = " ";
                        srcAddress = s;
                        destAddress = " ";
                        packetData = " ";
                    }

                    Packet packet = new Packet(time, length, IP, headerData, srcAddress, destAddress, packetData);
                    if (srcAddress != null) {
                        if (!srcAddress.contains("ARP")) {
                            packetTable.getItems().add(packet);
                        }
                    }
                    packetList.add(packet);

                    Platform.runLater(() -> {
                        if(packet.getSrcAddress().contains("ARP"))
                        {
                            protocolList.get(8).increment();
                        }else if (packet.getSrcAddress().contains("http") || packet.getDestAddress().contains("http")) {
                            protocolList.get(0).increment();
                        } else if (packet.getSrcAddress().contains("smtp") || packet.getDestAddress().contains("smtp")) {
                            protocolList.get(1).increment();
                        } else if (packet.getSrcAddress().contains("voip") || packet.getDestAddress().contains("voip")) {
                            protocolList.get(2).increment();
                        } else if (packet.getSrcAddress().contains("ftp") || packet.getDestAddress().contains("ftp")) {
                            protocolList.get(3).increment();
                        } else if (packet.getSrcAddress().contains("domain") || packet.getDestAddress().contains("domain") || packet.getSrcAddress().contains("dns") || packet.getDestAddress().contains("dns")) {
                            protocolList.get(4).increment();
                        } else if (packet.getSrcAddress().contains("25") || packet.getDestAddress().contains("25")) {
                            protocolList.get(5).increment();
                        } else if (packet.getSrcAddress().contains("telnet") || packet.getDestAddress().contains("telnet")) {
                            protocolList.get(6).increment();
                        } else if (packet.getSrcAddress().contains("ntp") || packet.getDestAddress().contains("ntp")) {
                            protocolList.get(7).increment();
                        } else {
                            protocolList.get(9).increment();
                        }
                        protocolList.get(10).increment();
                    });

                    try {
                        Thread.currentThread().sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                //p.waitFor();
                //System.out.println ("exit: " + p.exitValue());

                Platform.runLater(() -> {
                    if (flag == true)
                        status_ID.setText("Done!");
                    startButton_ID.setText("Start");
                });
                p.destroy();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Menu Actions
     */

    @FXML
    private void handleExitMenuAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you Sure?");
        if (packetTable.getItems().size() != 0)
            alert.setContentText("Any unsaved results will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            status_ID.setText("Yahoo!");
        }
    }

    @FXML
    private void handleAboutMenuAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("tcpdump_GUI");
        alert.setContentText("Description goes here");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            status_ID.setText("Yahoo!");
        }
    }

    @FXML
    private void handlePrefMenuAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("tcpdump_GUI");
        alert.setContentText("sahskkaskhk");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            status_ID.setText("Yahoo!");
        }
    }
}
