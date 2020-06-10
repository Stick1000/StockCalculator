import javax.swing.table.AbstractTableModel;

/**
 * An extension of the abstract class AbstractTableModel that holds
 * <code>double</code> values only. The values are immutable once instantiated.
 * 
 * @author Janley Molina
 */
public final class DoubleTypeTableModel extends AbstractTableModel {
    public static final int DEFAULT_ROWCOUNT = 5;
    public static final int DEFAULT_COLUMNCOUNT = 5;

    private final int rowCount;
    private final int columnCount;
    private final String[] columnNames;
    private final double[][] data;

    /**
     * Constructs a 5x5, empty DoubleTypeTableModel.
     */
    public DoubleTypeTableModel() {
        this(5, 5, null, new double[DEFAULT_ROWCOUNT][DEFAULT_COLUMNCOUNT]);
    }

    /**
     * Constructs an empty DoubleTypeTableModel with a specified size.
     * 
     * @param rowCount    Number of rows
     * @param columnCount Number of columns
     */
    public DoubleTypeTableModel(int rowCount, int columnCount) {
        this(rowCount, columnCount, null, new double[rowCount][columnCount]);
    }

    /**
     * Constructs a DoubleTypeTableModel with a specified size and custom data.
     * 
     * @param rowCount    Number of rows
     * @param columnCount Number of columns
     * @param columnNames a <code>String</code> array that holds the column names
     * @param data        a 2D <code>double</code> array that holds the data to be
     *                    displayed
     */
    public DoubleTypeTableModel(int rowCount, int columnCount, String[] columnNames, double[][] data) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.columnNames = columnNames;
        this.data = data;
    }

    public int getColumnCount() {
        return this.columnCount;
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public Object getValueAt(int row, int column) {
        return String.format("%.2f", this.data[row][column]);
    } // returns as String for convenience

    @Override
    public String getColumnName(int column) {
        if (this.columnNames != null)
            return this.columnNames[column];
        else
            return super.getColumnName(column);
    }
}