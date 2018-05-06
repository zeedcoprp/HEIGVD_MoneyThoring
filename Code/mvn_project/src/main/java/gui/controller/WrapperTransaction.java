package gui.controller;

import bll.logic.IOTransactionLogic;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WrapperTransaction extends RecursiveTreeObject<WrapperTransaction> {

    StringProperty amount;
    StringProperty name;
    StringProperty description;
    StringProperty date;
    StringProperty currency;

    public WrapperTransaction(IOTransactionLogic transaction){
        this.amount = new SimpleStringProperty(String.valueOf(transaction.getAmount()));
        this.name = new SimpleStringProperty(transaction.getName());
        this.description = new SimpleStringProperty(transaction.getDescription());
        this.date = new SimpleStringProperty(transaction.getDate().toString());
        this.currency = new SimpleStringProperty(transaction.getCurrency());
    }
}
