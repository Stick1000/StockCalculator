import javax.swing.JTextField;

import javax.swing.text.Document;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;
import javax.swing.text.BadLocationException;

/**
 * An extension of JTextField to include an input type and length restriction.
 * Whitespaces are automatically removed by design.
 * 
 * @author Janley Molina
 * @version 1.2
 */

public final class RestrictedJTextField extends JTextField {
    /**
     * No restriction on the input type.
     */
    public static final int LENIENT = 0;
    /**
     * Limited to alphabetical characters <code>a-z</code>, <code>A-Z</code>.
     */
    public static final int ALPHA = 1;
    /**
     * Limited to numerical characters <code>0-9</code>.
     */
    public static final int UNSIGNED_INT = 2;
    /**
     * Similar to <code>UNSIGNED_INT</code> but includes the characters
     * <code>-</code> and <code>+</code> to indicate positive or negative integers.
     */
    public static final int SIGNED_INT = 3;
    /**
     * Limited to numerical characters <code>0-9</code> and <code>.</code> to
     * indicate floating-point values.
     */
    public static final int UNSIGNED_FLOAT = 4;
    /**
     * Similar to <code>UNSIGNED_FLOAT</code> but includes the characters
     * <code>-</code> and <code>+</code> to indicate positive or negative
     * floating-point values.
     */
    public static final int SIGNED_FLOAT = 5;

    public static final int DEFAULT_MAXLENGTH = 10;

    private final int maxLength;
    private final int inputType;

    /**
     * Default RestrictedJTextField constructor with a maximum length of 10 and a
     * LENIENT input type.
     */
    public RestrictedJTextField() {
        this(DEFAULT_MAXLENGTH, LENIENT);
    }

    /**
     * Constructs a RestrictedJTextField instance with a specified maximum length
     * and a LENIENT input type.
     * 
     * @param maxLength The maximum number of characters allowed.
     */
    public RestrictedJTextField(int maxLength) {
        this(maxLength, LENIENT);
    }

    /**
     * Constructs a RestrictedJTextField instance with a specied maximum length and
     * input type.
     * 
     * @param maxLength The maximum number of characters allowed.
     * @param inputType The only input type allowed. One of the constants defined:
     *                  <code>LENIENT</code>, <code>ALPHA</code>,
     *                  <code>UNSIGNED_INT</code>, <code>SIGNED_INT</code>,
     *                  <code>UNSIGNED_FLOAT</code>, <code>SIGNED_FLOAT</code>.
     */
    public RestrictedJTextField(int maxLength, int inputType) {
        super();
        this.maxLength = maxLength;
        this.inputType = inputType;
    }

    @Override
    protected Document createDefaultModel() {
        return new RestrictedDocument();
    }

    private final class RestrictedDocument extends PlainDocument {
        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null || str.isBlank())
                return;

            switch (inputType) {
                case LENIENT:
                    break;
                case ALPHA:
                    if (!str.matches("[a-zA-Z]+"))
                        return;

                    break;
                case UNSIGNED_INT:
                    if (!str.matches("[0-9]+"))
                        return;

                    break;
                case SIGNED_INT:
                    if (!str.matches("[+-[0-9]]+"))
                        return;

                    break;
                case UNSIGNED_FLOAT:
                    if (!str.matches("[.[0-9]]+"))
                        return;

                    break;
                case SIGNED_FLOAT:
                    if (!str.matches("[.+-[0-9]]+"))
                        return;

                    break;
                default:
                    break;
            }

            if (this.getLength() + str.length() <= maxLength)
                super.insertString(offs, str, a);
        }
    }
}