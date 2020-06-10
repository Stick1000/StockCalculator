/**
 * Stores basic stock info suck as a stock's code, number of shares, and price
 * per share. Fields are immutable once instantiated.
 * 
 * @author Janley Molina
 * @version 1.0
 */

public final class Stock {
    private final String stockCode;
    private final int numOfShares;
    private final double pricePerShare;

    public Stock() {
        this("UNKNOWN", 0, 0);
    }

    public Stock(int numOfShares, double pricePerShare) {
        this("UNKNOWN", numOfShares, pricePerShare);
    }

    public Stock(String stockCode, int numOfShares, double pricePerShare) {
        this.stockCode = stockCode;
        this.numOfShares = numOfShares;
        this.pricePerShare = pricePerShare;
    }

    // Accessors
    public String getStockCode() {
        return this.stockCode;
    }

    public int getNumOfShares() {
        return this.numOfShares;
    }

    public double getPricePerShare() {
        return this.pricePerShare;
    }
}