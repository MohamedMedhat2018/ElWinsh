package com.beyond_tech.elwensh.messages;
//Hold the event message that u want to send
public class MessageEvent {

    boolean okayAccessNow = false;//offline

    public boolean isOkayAccessNow() {
        return okayAccessNow;
    }

    public void setOkayAccessNow(boolean okayAccessNow) {
        this.okayAccessNow = okayAccessNow;
    }

    public MessageEvent(boolean okayAccessNow) {
        this.okayAccessNow = okayAccessNow;
    }
}
