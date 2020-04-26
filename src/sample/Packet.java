package sample;

import java.util.StringTokenizer;

public class Packet {

    private String time;
    private String length;
    private String IP;
    private String headerData;
    private String srcAddress;
    private String destAddress;
    private String packetData;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getHeaderData() {
        return headerData;
    }

    public void setHeaderData(String headerData) {
        this.headerData = headerData;
    }

    public String getSrcAddress() {
        return srcAddress;
    }

    public void setSrcAddress(String srcAddress) {
        this.srcAddress = srcAddress;
    }

    public String getDestAddress() {
        return destAddress;
    }

    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    public String getPacketData() {
        return packetData;
    }

    public void setPacketData(String packetData) {
        this.packetData = packetData;
    }

    Packet(String t, String l, String i, String h, String s, String de, String p) {
        time = t;
        length = l;
        IP = i;
        headerData = h;
        srcAddress = s;
        destAddress = de;
        packetData = p;
    }

    /*public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }*/
}
