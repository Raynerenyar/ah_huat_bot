package org.telegram.toto.models;

import java.time.LocalDateTime;

public class Draw {

    private long value;
    private LocalDateTime datetime;

    public Draw(long value, LocalDateTime datetime) {
        this.value = value;
        this.datetime = datetime;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

}
