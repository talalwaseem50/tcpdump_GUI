package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class graphController implements Initializable {

    @FXML private PieChart chart;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    private void loadData() {
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList();

        list.add(new PieChart.Data("HTML", 30000));
        list.add(new PieChart.Data("HT", 60000));
        list.add(new PieChart.Data("L", 90000));

        chart.setData(list);
        chart.setTitle("Packets Visualized");
    }
}
