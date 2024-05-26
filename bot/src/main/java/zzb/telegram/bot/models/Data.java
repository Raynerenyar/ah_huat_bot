package zzb.telegram.bot.models;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {

    private List<Prize> Prizes;
    private List<Integer> WinningNumbers;
    private String AdditionalNumber;

    public Data(List<Prize> prizes, List<Integer> winningNumbers, String additionalNumber) {
        Prizes = prizes;
        WinningNumbers = winningNumbers;
        AdditionalNumber = additionalNumber;
    }

    public Data() {
    }

    @Override
    public String toString() {
        return "Data [Prizes=" + Prizes + ", WinningNumbers=" + WinningNumbers + ", AdditionalNumber="
                + AdditionalNumber + "]";
    }

    public List<Prize> getPrizes() {
        return Prizes;
    }

    public void setPrizes(List<Prize> Prizes) {
        this.Prizes = Prizes;
    }

    public List<Integer> getWinningNumbers() {
        return WinningNumbers;
    }

    public void setWinningNumbers(List<Integer> WinningNumbers) {
        this.WinningNumbers = WinningNumbers;
    }

    public String getAdditionalNumber() {
        return AdditionalNumber;
    }

    public void setAdditionalNumber(String AdditionalNumber) {
        this.AdditionalNumber = AdditionalNumber;
    }

}
