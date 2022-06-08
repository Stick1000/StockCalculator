/**
 * Handles logic for the Stock Calculator GUI.
 * 
 * @author Janley Molina
 */

public class StockCalculator {
    private static final double COMMISSION = 0.0025, VAT = 0.12, PSE = 0.00005, SCCP = 0.0001, STT = 0.006;

    private Stock stk;

    public double minDesiredReturn;
    public double totalBuyPrice, cutLoss, minSellPrice, gross, profit;

    private double[][] detailedResults;

    public StockCalculator() {
        this.stk = null;
        this.minDesiredReturn = 0.0;
        this.detailedResults = new double[5][3];
    }

    private double addChargesBuy(double commPrice, double IBP) {
        return (IBP + commPrice + (commPrice * VAT) + (IBP * PSE) + (IBP * SCCP));
    }

    private double addChargesSell(double commPrice, double sellPrice) {
        return sellPrice - ((20) + (VAT * 20) + (PSE * sellPrice) + (SCCP * sellPrice) + (STT * sellPrice));
    }

    private double grossBuyPrice() {
        double IBP = this.stk.getNumOfShares() * this.stk.getPricePerShare();
        double commPrice = IBP * COMMISSION;

        if (commPrice < 20)
            return this.addChargesBuy(20, IBP);
        else
            return this.addChargesBuy(commPrice, IBP);
    }

    private double grossSellPrice(double sellPrice) {
        double totalSellPrice = this.stk.getNumOfShares() * sellPrice;
        double commPrice = sellPrice * COMMISSION;

        if (commPrice < 20)
            return this.addChargesSell(20, totalSellPrice);
        else
            return this.addChargesSell(commPrice, totalSellPrice);
    }

    private void cutLoss() {
        double returnValue = 0, potentialLoss = this.totalBuyPrice - (0.05 * this.totalBuyPrice),
                sellPrice = this.minSellPrice;

        returnValue = this.grossSellPrice(sellPrice);

        while (returnValue > potentialLoss) {
            sellPrice -= 0.01;
            returnValue = this.grossSellPrice(sellPrice);
        }

        this.cutLoss = (sellPrice += 0.01); // compensation for the additional loop
    }

    private void sellTarget() {
        double returnValue = 0, sellPrice = this.stk.getPricePerShare();
        while (returnValue < this.minDesiredReturn) {
            double totalSellPrice = this.stk.getNumOfShares() * sellPrice;
            double commPrice = totalSellPrice * COMMISSION;

            if (commPrice < 20)
                returnValue = this.addChargesSell(20, totalSellPrice) - this.totalBuyPrice;
            else
                returnValue = this.addChargesSell(commPrice, totalSellPrice) - this.totalBuyPrice;

            sellPrice += 0.01;
        }

        this.minSellPrice = sellPrice;

        for (int i = 0; i < 5; i++) {
            double grossSellPrice = grossSellPrice(sellPrice);

            if (i == 0) {
                this.gross = grossSellPrice;
                this.profit = grossSellPrice - this.totalBuyPrice;
            }

            this.detailedResults[i][0] = sellPrice;
            this.detailedResults[i][1] = grossSellPrice;
            this.detailedResults[i][2] = grossSellPrice - this.totalBuyPrice;
            sellPrice += 0.5;
        }
    }

    public double[][] calculate(Stock stk, double minDesiredReturn) {
        this.stk = stk;
        this.minDesiredReturn = minDesiredReturn;

        this.totalBuyPrice = this.grossBuyPrice();
        this.sellTarget();
        this.cutLoss();

        return this.detailedResults;
    }
}
