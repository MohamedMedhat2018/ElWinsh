package com.beyond_tech.elwensh.messages;

public class MsgEventChangeMarker {

    boolean changeMarker = false;

    public boolean isChangeMarker() {
        return changeMarker;
    }

    public void setChangeMarker(boolean changeMarker) {
        this.changeMarker = changeMarker;
    }

    public MsgEventChangeMarker(boolean changeMarker) {
        this.changeMarker = changeMarker;
    }
}
