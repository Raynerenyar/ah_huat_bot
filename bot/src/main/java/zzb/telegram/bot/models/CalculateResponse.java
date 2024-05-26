package zzb.telegram.bot.models;

import java.io.Serializable;

public class CalculateResponse implements Serializable {

    private Data d;

    public CalculateResponse(Data d) {
        this.d = d;
    }

    public CalculateResponse() {
    }

    public Data getD() {
        return d;
    }

    public void setD(Data d) {
        this.d = d;
    }

}
