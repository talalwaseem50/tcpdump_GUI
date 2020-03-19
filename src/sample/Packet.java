package sample;

public class Packet {

    private String data;

    Packet(String d) {
        data = d;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
