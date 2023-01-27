package be.ehb.mycar.models;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Map;

public class Note implements Serializable {
    String docId;
    String title;
    String content;
    String FuelEconomy;
    Timestamp timestamp;



    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getFuelEconomy() {
        return FuelEconomy;
    }

    public void setFuelEconomy(String fuelEconomy) {
        FuelEconomy = fuelEconomy;
    }
}


