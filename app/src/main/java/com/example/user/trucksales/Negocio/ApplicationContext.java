package com.example.user.trucksales.Negocio;

import rego.printlib.export.regoPrinter;
import android.app.Application;

public class ApplicationContext extends Application {
    private regoPrinter printer;
    private int myState = 0;
    private String printName="RG-MTP58B";

    private preDefiniation.TransferMode printmode = preDefiniation.TransferMode.TM_NONE;
    private boolean labelmark = true;

    public regoPrinter getObject() {
        return printer;
    }

    public void setObject() {
        printer = new regoPrinter(this);
    }

    public String getName() {
        return printName;
    }

    public void setName(String name) {
        printName = name;
    }
    public void setState(int state) {
        myState = state;
    }

    public int getState() {
        return myState;
    }

    public void setPrintway(int printway) {

        switch (printway) {
            case 0:
                printmode = preDefiniation.TransferMode.TM_NONE;
                break;
            case 1:
                printmode = preDefiniation.TransferMode.TM_DT_V1;
                break;
            default:
                printmode = preDefiniation.TransferMode.TM_DT_V2;
                break;
        }

    }

    public int getPrintway() {
        return printmode.getValue();
    }

    public boolean getlabel() {
        return labelmark;
    }

    public void setlabel(boolean labelprint) {
        labelmark = labelprint;
    }

}
