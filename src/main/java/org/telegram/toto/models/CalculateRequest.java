package org.telegram.toto.models;

import java.io.Serializable;

public class CalculateRequest implements Serializable {

    private int drawNumber;
    private boolean isHalfBet;
    private String numbers;
    private int partsPurchased;
    private int totalNumberOfParts;

    public CalculateRequest(int drawNumber, boolean isHalfBet, String numbers, int partsPurchased,
            int totalNumberOfParts) {
        this.drawNumber = drawNumber;
        this.isHalfBet = isHalfBet;
        this.numbers = numbers;
        this.partsPurchased = partsPurchased;
        this.totalNumberOfParts = totalNumberOfParts;
    }

    @Override
    public String toString() {
        return "{\n" + //
                "  \"drawNumber\":" + drawNumber + ",\n" + //
                "  \"isHalfBet\":" + isHalfBet + ",\n" + //
                "  \"numbers\": \"" + numbers + "\",\n" + //
                "  \"partsPurchased\":" + partsPurchased + ",\n" + //
                "  \"totalNumberOfParts\":" + totalNumberOfParts + "\n" + //
                "}";
    }

    public int getDrawNumber() {
        return drawNumber;
    }

    public void setDrawNumber(int drawNumber) {
        this.drawNumber = drawNumber;
    }

    public boolean isHalfBet() {
        return isHalfBet;
    }

    public void setHalfBet(boolean isHalfBet) {
        this.isHalfBet = isHalfBet;
    }

    public String getNumbers() {
        return numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }

    public int getPartsPurchased() {
        return partsPurchased;
    }

    public void setPartsPurchased(int partsPurchased) {
        this.partsPurchased = partsPurchased;
    }

    public int getTotalNumberOfParts() {
        return totalNumberOfParts;
    }

    public void setTotalNumberOfParts(int totalNumberOfParts) {
        this.totalNumberOfParts = totalNumberOfParts;
    }

}
