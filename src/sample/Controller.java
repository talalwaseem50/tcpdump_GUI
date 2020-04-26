package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class Controller {

    @FXML private Button startButton_ID;
    @FXML private Label label;
    @FXML private VBox protocolBox;
    @FXML private TableView packetTable;


    //private Protocol p1 = new Protocol("HTTP");
    ArrayList<Protocol> protocolList;
    ArrayList<Packet> packetList = null;
    public void initialize() {
        label.setText("Initialized");

        ArrayList<String> pNameList = new ArrayList<String>();
        pNameList.add("HTTP");
        pNameList.add("SMTP");
        pNameList.add("VOIP");
        pNameList.add("FTP");
        pNameList.add("DNS");
        pNameList.add("POP3");
        pNameList.add("TELNET");
        pNameList.add("NTP");


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
            TableColumn dataColumn = (TableColumn) packetTable.getColumns().get(i);//new TableColumn("Temp");
            dataColumn.setCellValueFactory(new PropertyValueFactory<>(packetComponents[i]));
        }

    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

        label.setText("Running!");

        MultithreadedRun mRun = new MultithreadedRun();
        mRun.start();

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
                p = Runtime.getRuntime().exec("sudo /usr/sbin/tcpdump -c 5 -vv");
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

                while ((s = br.readLine()) != null) {
                    System.out.println(s);
                    StringTokenizer tokenizer = new StringTokenizer(s);
                    String time = tokenizer.nextToken(" ");
                    String IP = tokenizer.nextToken(" ");
                    String length = null;
                    String headerData = null;
                    String srcAddress = null;
                    String destAddress = null;
                    String packetData = null;
                    String appProtocol = null;
                    if(!s.contains("IP6"))
                    {
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
                        System.out.println(s);
                    }else
                    {
                        headerData = tokenizer.nextToken(")").toString().concat("))");
                        length = tokenizer.nextToken(")");
                        srcAddress = tokenizer.nextToken(">");
                        destAddress = tokenizer.nextToken(":");
                        destAddress = destAddress.substring(2);

                    }

                    Packet pTemp = new Packet(time,length,IP,headerData,srcAddress,destAddress,packetData);
                    packetTable.getItems().add(pTemp);
                    packetList.add(pTemp);
                }

                p.waitFor();
                System.out.println ("exit: " + p.exitValue());
                Platform.runLater(() -> {
                            for(int i = 0; i < packetList.size(); i++) {
                                if (packetList.get(i).getSrcAddress().contains("http") || packetList.get(i).getDestAddress().contains("http")) {
                                    protocolList.get(0).increment();
                                } else if (packetList.get(i).getSrcAddress().contains("smtp") || packetList.get(i).getDestAddress().contains("smtp")) {
                                    protocolList.get(1).increment();
                                } else if (packetList.get(i).getSrcAddress().contains("voip") || packetList.get(i).getDestAddress().contains("voip")) {
                                    protocolList.get(2).increment();
                                } else if (packetList.get(i).getSrcAddress().contains("ftp") || packetList.get(i).getDestAddress().contains("ftp")) {
                                    protocolList.get(3).increment();
                                } else if (packetList.get(i).getSrcAddress().contains("domain") || packetList.get(i).getDestAddress().contains("domain") || packetList.get(i).getSrcAddress().contains("dns") || packetList.get(i).getDestAddress().contains("dns")) {
                                    protocolList.get(4).increment();
                                } else if (packetList.get(i).getSrcAddress().contains("25") || packetList.get(i).getDestAddress().contains("25")) {
                                    protocolList.get(5).increment();
                                } else if (packetList.get(i).getSrcAddress().contains("telnet") || packetList.get(i).getDestAddress().contains("telnet")) {
                                    protocolList.get(6).increment();
                                } else if (packetList.get(i).getSrcAddress().contains("ntp") || packetList.get(i).getDestAddress().contains("ntp")) {
                                    protocolList.get(6).increment();
                                }
                            }
                    label.setText("Done!");
                });
                p.destroy();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }


}
