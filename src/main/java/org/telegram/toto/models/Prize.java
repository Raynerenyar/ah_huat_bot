package org.telegram.toto.models;

import java.io.Serializable;

public class Prize implements Serializable {
    private int GroupNumber;
    private int NumberOfSharesWon;
    private int ShareAmount;
    private int Total;
    private int TotalNumberOfParts;
    private int NumberOfPartsPurchased;

    public Prize(int groupNumber, int numberOfSharesWon, int shareAmount, int total, int totalNumberOfParts,
            int numberOfPartsPurchased) {
        GroupNumber = groupNumber;
        NumberOfSharesWon = numberOfSharesWon;
        ShareAmount = shareAmount;
        Total = total;
        TotalNumberOfParts = totalNumberOfParts;
        NumberOfPartsPurchased = numberOfPartsPurchased;
    }

    public Prize() {
    }

    public int getGroupNumber() {
        return GroupNumber;
    }

    public void setGroupNumber(int GroupNumber) {
        this.GroupNumber = GroupNumber;
    }

    public int getNumberOfSharesWon() {
        return NumberOfSharesWon;
    }

    public void setNumberOfSharesWon(int NumberOfSharesWon) {
        this.NumberOfSharesWon = NumberOfSharesWon;
    }

    public int getShareAmount() {
        return ShareAmount;
    }

    public void setShareAmount(int ShareAmount) {
        this.ShareAmount = ShareAmount;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public int getTotalNumberOfParts() {
        return TotalNumberOfParts;
    }

    public void setTotalNumberOfParts(int TotalNumberOfParts) {
        this.TotalNumberOfParts = TotalNumberOfParts;
    }

    public int getNumberOfPartsPurchased() {
        return NumberOfPartsPurchased;
    }

    public void setNumberOfPartsPurchased(int NumberOfPartsPurchased) {
        this.NumberOfPartsPurchased = NumberOfPartsPurchased;
    }

}
