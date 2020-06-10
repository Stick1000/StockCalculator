import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import java.util.Calendar;

/**
 * A simple buy/sell stock calculator.
 * 
 * @author John Markton Olarte
 * @author Janley Molina
 * @version 1.8
 */

public final class StockCalculatorGUI extends JFrame implements ItemListener {
    private static final long serialVersionUID = 1L;

    private JButton CALCULATE_BTN, CLR_BTN, DETAILS_BTN, ABOUT_BTN;
    private RestrictedJTextField STK_CODE_FIELD, STK_NSHARE_FIELD, STK_PSHARE_FIELD, STK_MDR_FIELD, RES_TBP_FIELD,
            RES_CL_FIELD, RES_SELL_FIELD, RES_GROSS_FIELD, RES_PROFIT_FIELD;
    private JCheckBox SAVE_TO_FILE_CHK;
    private OperationHandler OP_HANDLER;

    private boolean saveToFile;
    private final StockCalculator backEnd;
    private final DetailsPane detailsModal;

    // TODO: refactor to focus on constructing
    public StockCalculatorGUI() {
        super("Basic Stock Calculator");
        this.OP_HANDLER = new OperationHandler();

        this.STK_CODE_FIELD = new RestrictedJTextField(5);
        this.STK_CODE_FIELD.setToolTipText("ex. MEG");
        this.STK_NSHARE_FIELD = new RestrictedJTextField(10, RestrictedJTextField.UNSIGNED_INT);
        this.STK_NSHARE_FIELD.setToolTipText("ex. 1000");
        this.STK_PSHARE_FIELD = new RestrictedJTextField(10, RestrictedJTextField.SIGNED_FLOAT);
        this.STK_PSHARE_FIELD.setToolTipText("ex. 2.96");
        this.STK_MDR_FIELD = new RestrictedJTextField(10, RestrictedJTextField.SIGNED_FLOAT);
        this.STK_MDR_FIELD.setToolTipText("ex. 200");

        this.SAVE_TO_FILE_CHK = new JCheckBox("Save results to file");
        this.SAVE_TO_FILE_CHK.addItemListener(this);
        this.CALCULATE_BTN = new JButton("Confirm");
        this.CALCULATE_BTN.addActionListener(this.OP_HANDLER);
        this.CLR_BTN = new JButton("Clear");
        this.CLR_BTN.addActionListener(this.OP_HANDLER);

        // Calculation Panel
        Container calcPanel = new Container();
        calcPanel.setLayout(new GridLayout(6, 2));

        calcPanel.add(new JLabel("Stock Code (optional): ", SwingConstants.RIGHT));
        calcPanel.add(this.STK_CODE_FIELD);
        calcPanel.add(new JLabel("Number of shares: ", SwingConstants.RIGHT));
        calcPanel.add(this.STK_NSHARE_FIELD);
        calcPanel.add(new JLabel("Price per share: ", SwingConstants.RIGHT));
        calcPanel.add(this.STK_PSHARE_FIELD);
        calcPanel.add(new JLabel("Minimum desired return (MDR): ", SwingConstants.RIGHT));
        calcPanel.add(this.STK_MDR_FIELD);

        calcPanel.add(new JLabel("", SwingConstants.RIGHT));
        calcPanel.add(this.SAVE_TO_FILE_CHK);
        calcPanel.add(this.CLR_BTN);
        calcPanel.add(this.CALCULATE_BTN);

        this.RES_TBP_FIELD = new RestrictedJTextField(10, RestrictedJTextField.SIGNED_FLOAT);
        this.RES_TBP_FIELD.setEditable(false);
        this.RES_CL_FIELD = new RestrictedJTextField(10, RestrictedJTextField.SIGNED_FLOAT);
        this.RES_CL_FIELD.setEditable(false);
        this.RES_SELL_FIELD = new RestrictedJTextField(10, RestrictedJTextField.SIGNED_FLOAT);
        this.RES_SELL_FIELD.setEditable(false);
        this.RES_GROSS_FIELD = new RestrictedJTextField(10, RestrictedJTextField.SIGNED_FLOAT);
        this.RES_GROSS_FIELD.setEditable(false);
        this.RES_PROFIT_FIELD = new RestrictedJTextField(10, RestrictedJTextField.SIGNED_FLOAT);
        this.RES_PROFIT_FIELD.setEditable(false);

        this.DETAILS_BTN = new JButton("Detailed Results");
        this.DETAILS_BTN.addActionListener(this.OP_HANDLER);
        this.DETAILS_BTN.setEnabled(false);
        this.ABOUT_BTN = new JButton("About");
        this.ABOUT_BTN.addActionListener(this.OP_HANDLER);

        // Results Panel
        Container resultsPanel = new Container();
        resultsPanel.setLayout(new GridLayout(6, 2));

        resultsPanel.add(new JLabel("Total Buying Price: ", SwingConstants.RIGHT));
        resultsPanel.add(this.RES_TBP_FIELD);
        resultsPanel.add(new JLabel("Sell @ (Max 5% Loss): ", SwingConstants.RIGHT));
        resultsPanel.add(this.RES_CL_FIELD);
        resultsPanel.add(new JLabel("Sell @ (to achieve MDR): ", SwingConstants.RIGHT));
        resultsPanel.add(this.RES_SELL_FIELD);
        resultsPanel.add(new JLabel("Gross: ", SwingConstants.RIGHT));
        resultsPanel.add(this.RES_GROSS_FIELD);
        resultsPanel.add(new JLabel("Profit: ", SwingConstants.RIGHT));
        resultsPanel.add(this.RES_PROFIT_FIELD);
        resultsPanel.add(this.ABOUT_BTN);
        resultsPanel.add(this.DETAILS_BTN);

        // Main Panel, aka the main window
        Container mainPanel = this.getContentPane();
        mainPanel.setLayout(new GridLayout(2, 1));
        mainPanel.add(calcPanel);
        mainPanel.add(resultsPanel);

        this.backEnd = new StockCalculator();
        this.detailsModal = new DetailsPane();

        this.setSize(400, 400);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    public void updateResultFields(double totalBuyPrice, double cutLoss, double minSellPrice, double gross,
            double profit) {
        this.RES_TBP_FIELD.setText(String.format("%.2f", totalBuyPrice));
        this.RES_CL_FIELD.setText(String.format("%.2f", cutLoss));
        this.RES_SELL_FIELD.setText(String.format("%.2f", minSellPrice));
        this.RES_GROSS_FIELD.setText(String.format("%.2f", gross));
        this.RES_PROFIT_FIELD.setText(String.format("%.2f", profit));
    }

    public void clearAllFields() {
        this.STK_CODE_FIELD.setText(null);
        this.STK_NSHARE_FIELD.setText(null);
        this.STK_PSHARE_FIELD.setText(null);
        this.STK_MDR_FIELD.setText(null);

        this.RES_TBP_FIELD.setText(null);
        this.RES_CL_FIELD.setText(null);
        this.RES_SELL_FIELD.setText(null);
        this.RES_GROSS_FIELD.setText(null);
        this.RES_PROFIT_FIELD.setText(null);
    }

    public StockCalculatorGUI getParentFrame() {
        return this;
    }

    private class StockCalculator {
        private static final double COMMISSION = 0.0025, VAT = 0.12, PSE = 0.00005, SCCP = 0.0001, STT = 0.006;

        private Stock stk;
        private double minDesiredReturn;

        private double totalBuyPrice, cutLoss, minSellPrice, gross, profit;

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

        private void saveResults() {
            try {
                BufferedWriter fw = new BufferedWriter(new FileWriter("saveResults.txt", true));

                Calendar timestamp = Calendar.getInstance();
                int Y = timestamp.get(Calendar.YEAR);
                int M = timestamp.get(Calendar.MONTH) + 1;
                int D = timestamp.get(Calendar.DAY_OF_MONTH);

                int HH = timestamp.get(Calendar.HOUR_OF_DAY);
                int MM = timestamp.get(Calendar.MINUTE);
                int SS = timestamp.get(Calendar.SECOND);

                fw.write(String.format("LOG: %d-%d-%d %d:%d:%d\n", Y, M, D, HH, MM, SS));
                fw.write("\nStock Code: " + this.stk.getStockCode());
                fw.write(String.format("\nTotal Buying Price: %.2f", this.totalBuyPrice));
                fw.write(String.format("\nMinimum Desired Return (MDR): %.2f", this.minDesiredReturn));
                fw.write(String.format("\nMinimum Sell Price to Achieve MDR: %.2f\n\n", this.minSellPrice));

                fw.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error saving the results to file.", "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        public double[][] calculate(Stock stk, double minDesiredReturn) {
            this.stk = stk;
            this.minDesiredReturn = minDesiredReturn;

            this.totalBuyPrice = this.grossBuyPrice();
            this.sellTarget();
            this.cutLoss();
            updateResultFields(this.totalBuyPrice, this.cutLoss, this.minSellPrice, this.gross, this.profit);

            if (saveToFile)
                this.saveResults();

            return this.detailedResults;
        }
    }

    private class OperationHandler implements ActionListener {
        private Stock stk;
        private double[][] detailedResults;

        public void actionPerformed(ActionEvent e) {
            try {
                if (e.getActionCommand().equals(CALCULATE_BTN.getText())) {
                    String stockCode;
                    int numOfShares = Integer.parseInt(STK_NSHARE_FIELD.getText());
                    double pricePerShare = Double.parseDouble(STK_PSHARE_FIELD.getText());

                    double minDesiredReturn = Double.parseDouble(STK_MDR_FIELD.getText());

                    if (STK_CODE_FIELD.getText().isEmpty())
                        this.stk = new Stock(numOfShares, pricePerShare);
                    else {
                        stockCode = STK_CODE_FIELD.getText();
                        this.stk = new Stock(stockCode, numOfShares, pricePerShare);
                    }
                    this.detailedResults = backEnd.calculate(this.stk, minDesiredReturn);

                    DETAILS_BTN.setEnabled(true);
                } else if (e.getActionCommand().equals(CLR_BTN.getText())) {
                    clearAllFields();
                    DETAILS_BTN.setEnabled(false);
                } else if (e.getActionCommand().equals(ABOUT_BTN.getText()))
                    JOptionPane.showMessageDialog(null, "v.1.4\n\nCreated by:\nJohn Markton Olarte\nJanley Molina",
                            "About this program", JOptionPane.INFORMATION_MESSAGE);
                else if (e.getActionCommand().equals(DETAILS_BTN.getText()))
                    detailsModal.displayDetailedResults(this.stk, this.detailedResults);
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(null, "Please check your input for invalid or incomplete fields.",
                        "INVALID OR INCOMPLETE INPUT", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent f) {
        this.saveToFile = (f.getStateChange() == ItemEvent.SELECTED);
    }

    private class DetailsPane extends JDialog {
        private JTable DETAILS_TABLE;
        private TableModel DATA_MODEL;
        private JScrollPane DT_SCROLLPANE;

        public DetailsPane() {
            super(getParentFrame(), true);

            this.DATA_MODEL = null;
            this.DETAILS_TABLE = new JTable(this.DATA_MODEL);
            this.DETAILS_TABLE.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            this.DT_SCROLLPANE = new JScrollPane(this.DETAILS_TABLE);

            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            this.setSize(475, 175);
            this.setResizable(false);
        }

        public void displayDetailedResults(Stock stk, double[][] detailedResults) {
            String[] columnNames = { "SELL @", "GROSS", "PROFIT" };
            this.DATA_MODEL = new DoubleTypeTableModel(5, 3, columnNames, detailedResults);

            Container detailsPanel = this.getContentPane();
            detailsPanel.setLayout(new FlowLayout());

            detailsPanel.add(new JLabel("Possible selling targets (0.5 intervals)"));
            detailsPanel.add(DT_SCROLLPANE);

            this.setLocationRelativeTo(getParentFrame());
            this.setTitle("Detailed results for " + stk.getStockCode());
            this.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new StockCalculatorGUI();
    }
}