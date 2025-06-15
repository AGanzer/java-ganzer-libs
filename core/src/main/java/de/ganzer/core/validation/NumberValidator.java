package de.ganzer.core.validation;

import de.ganzer.core.internals.CoreMessages;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.format.DecimalStyle;

/**
 * The NumberValidator class defines a validator that validates the correct
 * format and range of strings that represents numbers.
 */
public class NumberValidator extends Validator {
    private double minValue = Long.MIN_VALUE;
    private double maxValue = Long.MAX_VALUE;
    private int numDecimals = 0;
    private String displayFormat;
    private String editFormat;

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT},
     * the {@link #getMinValue smallest} allowed value to {@code Long.MIN_VALUE} and
     * the {@link #getMaxValue greatest} allowed value to {@code Long.MAX_VALUE}.
     * Input of {@link #getNumDecimals post decimal digits} is forbidden.
     */
    public NumberValidator() {
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the {@link #getMinValue smallest} allowed value to {@code Long.MIN_VALUE}
     * and the {@link #getMaxValue greatest} allowed value to {@code Long.MAX_VALUE}.
     * Input of {@link #getNumDecimals post decimal digits} is forbidden.
     *
     * @param options The options to set. This may be any combination of the
     *                {@link ValidatorOptions} constants.
     */
    public NumberValidator(int options) {
        super(options);
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the {@link #getOptions options} to {@link ValidatorOptions#NEEDS_INPUT}.
     * Input of {@link #getNumDecimals post decimal digits} is forbidden.
     *
     * @param minValue The smallest allowed value.
     * @param maxValue The greatest allowed value.
     * @throws IllegalArgumentException minValue is greater than maxValue.
     */
    public NumberValidator(double minValue, double maxValue) {
        this(ValidatorOptions.NEEDS_INPUT, minValue, maxValue);
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * Input of {@link #getNumDecimals post decimal digits} is forbidden.
     *
     * @param options  The options to set. This may be any combination of the
     *                 {@link ValidatorOptions} constants.
     * @param minValue The smallest allowed value.
     * @param maxValue The greatest allowed value.
     * @throws IllegalArgumentException minValue is greater than maxValue.
     */
    public NumberValidator(int options, double minValue, double maxValue) {
        super(options);

        if (minValue > maxValue)
            throw new IllegalArgumentException("minValue/maxValue");

        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Gets the smallest allowed value.
     *
     * @return The smallest allowed value.
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * Sets the smallest allowed value.
     *
     * @param minValue The smallest allowed value to set. If this is greater
     *                 than {@link #getMaxValue()}, the greatest allowed value
     *                 ist set to minValue.
     */
    public void setMinValue(double minValue) {
        this.minValue = minValue;

        if (minValue > maxValue)
            maxValue = minValue;
    }

    /**
     * Gets the greatest allowed value.
     *
     * @return The greatest allowed value.
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the greatest allowed value.
     *
     * @param maxValue The greatest allowed value to set. If this is less
     *                 than {@link #getMinValue()}, the smallest allowed
     *                 value ist set to maxValue.
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;

        if (minValue > maxValue)
            minValue = maxValue;
    }

    /**
     * Indicates whether post decimal digits are allowed.
     * <p>
     * A value of 0 does not mean, that the range of valid numbers is shrunk
     * to the range of {@code Long}. It simply means that a valid value does
     * not contain any post decimal digits.
     *
     * @return {@code true} if post decimal digits are allowed.
     */
    public int getNumDecimals() {
        return numDecimals;
    }

    /**
     * Sets the number of post decimal digits to allow.
     * <p>
     * A value of 0 does not mean, that the range of valid numbers is shrunk
     * to the range of {@code Long}. It simply means that a valid value does
     * not contain any post decimal digits.
     * <p>
     * If the range shall be shrunk to {@code Long}, the range must be
     * explicitly set by {@link #setMinValue} and {@link #setMaxValue} or by
     * {@link #setRange}.
     *
     * @param numDecimals The number of post decimal digits to allow.
     * @throws IllegalArgumentException numDecimals is less than zero.
     */
    public void setNumDecimals(int numDecimals) {
        if (numDecimals < 0)
            throw new IllegalArgumentException("numDecimals");

        this.numDecimals = numDecimals;
    }

    /**
     * Sets the range of valid values.
     *
     * @param minValue The smallest allowed value.
     * @param maxValue The greatest allowed value.
     * @throws IllegalArgumentException If minValue is greater than maxValue.
     */
    public void setRange(double minValue, double maxValue) {
        if (minValue > maxValue)
            throw new IllegalArgumentException("minValue");

        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Gets the format that is used for formatting the text in display mode.
     *
     * @return The format that is used for formatting the text in display mode
     * or {@code null} to use default formatting.
     *
     * @see #doFormatText(String, TextFormat)
     */
    public String getDisplayFormat() {
        return displayFormat;
    }

    /**
     * Sets the format to use for formatting the text in display mode.
     *
     * @param displayFormat The format to use or {@code null} to use default
     *                      formatting. If this is an empty String, {@code null}
     *                      is set.
     *
     * @see #doFormatText(String, TextFormat)
     */
    public void setDisplayFormat(String displayFormat) {
        if (displayFormat != null && displayFormat.isEmpty())
            this.displayFormat = null;
        else
            this.displayFormat = displayFormat;
    }

    /**
     * Gets the format that is used for formatting the text in edit mode.
     *
     * @return The format that is used for formatting the text in edit mode
     * or {@code null} to use default formatting.
     *
     * @see #doFormatText(String, TextFormat)
     */
    public String getEditFormat() {
        return editFormat;
    }

    /**
     * Sets the format to use for formatting the text in edit mode.
     *
     * @param editFormat The format to use or {@code null} to use default
     *                   formatting. If this is an empty String, {@code null}
     *                   is set.
     *
     * @see #doFormatText(String, TextFormat)
     */
    public void setEditFormat(String editFormat) {
        if (editFormat != null && editFormat.isEmpty())
            this.editFormat = null;
        else
            this.editFormat = editFormat;
    }

    /**
     * This implementation calls {@link Validator#doInputValidation} and checks
     * whether the input is a valid number in the range from {@link #getMinValue()}
     * to {@link #getMaxValue()}.
     *
     * @param text     The text to validate. This is never {@code null}. The text
     *                 must not be modified if autoFill is {@code false}.
     * @param autoFill Indicates whether the text is allowed to be modified.
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    @Override
    protected boolean doInputValidation(StringBuilder text, boolean autoFill) {
        if (!super.doInputValidation(text, autoFill))
            return false;

        if (text.isEmpty())
            return true;

        var t = text.toString();
        var d = DecimalStyle.ofDefaultLocale();

        if (numDecimals == 0 && t.indexOf(d.getDecimalSeparator()) != -1)
            return false;

        if (t.length() == 1) {
            if (t.charAt(0) == d.getNegativeSign())
                return minValue < 0;
        }

        try {
            NumberFormat.getInstance().parse(t).doubleValue();
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * This implementation calls the {@link Validator#doInputValidation} and checks
     * whether the input is a valid number in the range from {@link #getMinValue()}
     * to {@link #getMaxValue()}.
     *
     * @param text The text to validate. This is never {@code null}.
     * @param er   A reference to a possible exception. If this method returns
     *             {@code false}, the encapsulated exception is set to an
     *             instance of {@link ValidatorException}. This must not be
     *             {@code null}.
     * @return {@code true} if text is valid; otherwise, {@code false} is
     * returned.
     */
    @Override
    protected boolean doValidate(String text, ValidatorExceptionRef er) {
        if (!super.doValidate(text, er))
            return false;

        if (text.isEmpty())
            return true;

        var d = DecimalStyle.ofDefaultLocale();

        if (numDecimals == 0 && text.indexOf(d.getDecimalSeparator()) != -1) {
            er.setException(new ValidatorException(getErrorMessage() != null ? getErrorMessage() : CoreMessages.get("inputIsNotInteger")));
            return false;
        }

        try {
            double v = NumberFormat.getInstance().parse(text).doubleValue();

            if (minValue <= v && v <= maxValue)
                return true;

            if (getErrorMessage() != null)
                er.setException(new ValidatorException(getErrorMessage()));
            else {
                String mask1 = String.format("%%1$,.%df", numDecimals);
                String mask2 = String.format("%%2$,.%df", numDecimals);
                String format = String.format(CoreMessages.get("inputOutOfRange"), mask1, mask2);

                er.setException(new ValidatorException(String.format(format, minValue, maxValue)));
            }

            return false;
        } catch (ParseException e) {
            er.setException(new ValidatorException(getErrorMessage() != null ? getErrorMessage() : CoreMessages.get("inputIsNotNumber")));
            return false;
        }
    }

    /**
     * Called to reformats the specified text.
     *
     * @param text The text to reformat. This is never {@code null}
     * @param how  How to format the text. Must be one of the {@link TextFormat}
     *             values. If this is {@link TextFormat#DISPLAY}, the text is
     *             formatted to be displayed in a fine format. If this is
     *             {@link TextFormat#EDIT}, the text is formatted to be an easy
     *             editable text.
     * @return The formatted string or input if the text cannot be formatted.
     */
    @Override
    protected String doFormatText(String text, TextFormat how) {
        try {
            double value = NumberFormat.getInstance().parse(text).doubleValue();
            String eFormat = editFormat == null
                    ? numDecimals > 0 && value != (int)value ? String.format("%%.%df", numDecimals) : "%.0f"
                    : editFormat;
            String dFormat = displayFormat == null
                    ? numDecimals > 0 ? String.format("%%,.%df", numDecimals) : "%,.0f"
                    : displayFormat;

            return String.format(how == TextFormat.DISPLAY ? dFormat : eFormat, value);
        } catch (ParseException e) {
            return text;
        }
    }
}
