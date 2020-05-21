package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    @FXML private PieChart chart;
    ArrayList<Protocol> protocolList = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setData(ArrayList<Protocol> list) {
        protocolList = list;
        ObservableList<PieChart.Data> olist = FXCollections.observableArrayList();

        for(Protocol p : protocolList) {
            if (p.getpCount() != 0 && !p.getpName().matches("Total")) {
                olist.add(new PieChart.Data(p.getpName(), p.getpCount()));
            }
        }

        chart.setData(olist);
        chart.setTitle("Packets Visualized");
    }
}
