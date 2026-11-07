package de.ganzer.core.validation;

import de.ganzer.core.internals.CoreMessages;
import de.ganzer.core.util.Strings;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.format.DecimalStyle;
import java.util.Locale;
import java.util.Objects;

/**
 * The CurrencyValidator class defines a validator that validates the correct
 * format and range of strings that represents currency values.
 *
 * @since 6.4.0
 */
public class CurrencyValidator extends Validator {
    /**
     * The default error message for input that is not in the allowed range.
     *
     * @see #setRangeErrorMessage(String)
     */
    public final static String DEFAULT_RANGE_ERROR_MESSAGE = CoreMessages.get("inputOutOfRange");
    /**
     * The default error message for input that is not a number.
     *
     * @see #setNumberErrorMessage(String)
     */
    public final static String DEFAULT_NUMBER_ERROR_MESSAGE = CoreMessages.get("inputIsNotNumber");

    private String rangeErrorMessage = DEFAULT_RANGE_ERROR_MESSAGE;
    private String numberErrorMessage = DEFAULT_NUMBER_ERROR_MESSAGE;
    private double minValue = Double.MIN_VALUE;
    private double maxValue = Long.MAX_VALUE;
    private Locale locale;
    private boolean hideCurrencySymbol;
    private String currencySymbol;
    private DecimalFormat editFormat;
    private DecimalFormat displayFormat;

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the options to {@link ValidatorOptions#NEEDS_INPUT},
     * the smallest allowed value to {@link Long#MIN_VALUE} and the greatest
     * allowed value to {@link Long#MAX_VALUE}.
     */
    public CurrencyValidator() {
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the {@link #getMinValue smallest} allowed value to {@code Long.MIN_VALUE}
     * and the {@link #getMaxValue greatest} allowed value to {@code Long.MAX_VALUE}.
     *
     * @param options The options to set. This may be any combination of the
     *         {@link ValidatorOptions} constants.
     */
    public CurrencyValidator(int options) {
        super(options);
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the options to {@link ValidatorOptions#NEEDS_INPUT}.
     *
     * @param minValue The smallest allowed value.
     * @param maxValue The greatest allowed value.
     *
     * @throws IllegalArgumentException minValue is greater than maxValue.
     */
    public CurrencyValidator(double minValue, double maxValue) {
        this(ValidatorOptions.NEEDS_INPUT, minValue, maxValue, false);
    }

    /**
     * Creates a new instance of the validator.
     *
     * @param options The options to set. This may be any combination of the
     *         {@link ValidatorOptions} constants.
     * @param minValue The smallest allowed value.
     * @param maxValue The greatest allowed value.
     *
     * @throws IllegalArgumentException minValue is greater than maxValue.
     */
    public CurrencyValidator(int options, double minValue, double maxValue) {
        this(options, minValue, maxValue, false);
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the options to {@link ValidatorOptions#NEEDS_INPUT}.
     *
     * @param minValue The smallest allowed value.
     * @param maxValue The greatest allowed value.
     * @param hideCurrencySymbol {@code true} to hide the currency symbol in
     *         display mode.
     *
     * @throws IllegalArgumentException minValue is greater than maxValue.
     */
    public CurrencyValidator(double minValue, double maxValue, boolean hideCurrencySymbol) {
        this(ValidatorOptions.NEEDS_INPUT, minValue, maxValue, hideCurrencySymbol);
    }

    /**
     * Creates a new instance of the validator.
     *
     * @param options The options to set. This may be any combination of the
     *         {@link ValidatorOptions} constants.
     * @param minValue The smallest allowed value.
     * @param maxValue The greatest allowed value.
     * @param hideCurrencySymbol {@code true} to hide the currency symbol in
     *         display mode.
     *
     * @throws IllegalArgumentException minValue is greater than maxValue.
     */
    public CurrencyValidator(int options, double minValue, double maxValue, boolean hideCurrencySymbol) {
        super(options);

        if (minValue > maxValue)
            throw new IllegalArgumentException("minValue/maxValue");

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.hideCurrencySymbol = hideCurrencySymbol;
        this.editFormat = getEditFormat();
        this.displayFormat = getDisplayFormat();
    }

    /**
     * Gets the message shown if the input is out of the allowed range.
     *
     * @return The error message to use. The default is
     *         {@link #DEFAULT_RANGE_ERROR_MESSAGE}.
     *
     * @since 5.4.0
     */
    public String getRangeErrorMessage() {
        return rangeErrorMessage;
    }

    /**
     * Sets the message shown if the input is out of the allowed range.
     * <p>
     * <b>NOTE:</b> The message to set must contain 2 placeholders in the form
     * {@code %1$s} (for the minimum allowed number) and {@code %2$s} (for the
     * maximum allowed number).
     *
     * @param rangeErrorMessage The message to use. If this is
     *         {@code null}, empty or does contain white spaces only,
     *         {@link #DEFAULT_RANGE_ERROR_MESSAGE} is used.
     *
     * @since 5.4.0
     */
    public void setRangeErrorMessage(String rangeErrorMessage) {
        this.rangeErrorMessage = Strings.isNullOrBlank(rangeErrorMessage)
                ? DEFAULT_RANGE_ERROR_MESSAGE
                : rangeErrorMessage;
    }

    /**
     * Gets the message shown if the input is not a number.
     *
     * @return The error message to use. The default is
     *         {@link #DEFAULT_NUMBER_ERROR_MESSAGE}.
     *
     * @since 5.4.0
     */
    public String getNumberErrorMessage() {
        return numberErrorMessage;
    }

    /**
     * Sets the message shown if the input is not a number.
     *
     * @param numberErrorMessage The message to use. If this is
     *         {@code null}, empty or does contain white spaces only,
     *         {@link #DEFAULT_NUMBER_ERROR_MESSAGE} is used.
     *
     * @since 5.4.0
     */
    public void setNumberErrorMessage(String numberErrorMessage) {
        this.numberErrorMessage = Strings.isNullOrBlank(numberErrorMessage)
                ? DEFAULT_NUMBER_ERROR_MESSAGE
                : numberErrorMessage;
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
     *         than {@link #getMaxValue()}, the greatest allowed value
     *         ist set to minValue.
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
     *         than {@link #getMinValue()}, the smallest allowed
     *         value ist set to maxValue.
     */
    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;

        if (minValue > maxValue)
            minValue = maxValue;
    }

    /**
     * Indicates whether a currency symbol is shown in display mode.
     *
     * @return {@code true} if a currency symbol is hidden in display mode.
     *
     * @see #setCurrencySymbol(String)
     */
    public boolean isHideCurrencySymbol() {
        return hideCurrencySymbol;
    }

    /**
     * Sets whether a currency symbol is shown in display mode.
     *
     * @param hideCurrencySymbol {@code true} to hide the currency symbol in
     *         display mode.
     *
     * @see #setCurrencySymbol(String)
     */
    public void setHideCurrencySymbol(boolean hideCurrencySymbol) {
        if (this.hideCurrencySymbol == hideCurrencySymbol)
            return;

        this.hideCurrencySymbol = hideCurrencySymbol;
        this.displayFormat = getDisplayFormat();
    }

    /**
     * Gets the currency symbol to use if the locale's default symbol should not
     * be used.
     *
     * @return The currency symbol to use or {@code null} to use the default
     *          symbol.
     *
     * @see #setHideCurrencySymbol(boolean)
     */
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    /**
     * Sets the currency symbol to use if the locale's default symbol should not
     * be used.
     *
     * @param currencySymbol The currency symbol to use or {@code null} to use
     *         the default symbol.
     *
     * @see #setHideCurrencySymbol(boolean)
     */
    public void setCurrencySymbol(String currencySymbol) {
        if (Objects.equals(this.currencySymbol, currencySymbol))
            return;

        this.currencySymbol = currencySymbol;
        this.displayFormat = getDisplayFormat();
    }

    /**
     * Sets the range of valid values.
     *
     * @param minValue The smallest allowed value.
     * @param maxValue The greatest allowed value.
     *
     * @throws IllegalArgumentException If minValue is greater than maxValue.
     */
    public void setRange(double minValue, double maxValue) {
        if (minValue > maxValue)
            throw new IllegalArgumentException("minValue");

        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    /**
     * Gets the locale to use to format numbers.
     *
     * @return The locale to use to format numbers or {@code null} if the
     *         default locale is used.
     *
     * @see Locale#getDefault()
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets the locale to use to format numbers.
     *
     * @param locale The locale to use to format numbers or {@code null} to use
     *         the default locale.
     *
     * @see Locale#getDefault()
     */
    public void setLocale(Locale locale) {
        if (Objects.equals(this.locale, locale))
            return;

        this.locale = locale;
        this.editFormat = getEditFormat();
        this.displayFormat = getDisplayFormat();
    }

    /**
     * This implementation calls {@link Validator#doInputValidation} and checks
     * whether the input is a valid number in the range from {@link #getMinValue()}
     * to {@link #getMaxValue()}.
     *
     * @param text The text to validate. This is never {@code null}. The text
     *         must not be modified if autoFill is {@code false}.
     * @param autoFill Indicates whether the text is allowed to be modified.
     *
     * @return {@code true} if text is valid; otherwise, {@code false} is
     *         returned.
     */
    @Override
    protected boolean doInputValidation(StringBuilder text, boolean autoFill) {
        if (!super.doInputValidation(text, autoFill))
            return false;

        if (text.isEmpty())
            return true;

        var pos = new ParsePosition(0);
        var res = editFormat.parse(text.toString(), pos);

        return res != null && pos.getErrorIndex() < 0 && pos.getIndex() == text.length();
    }

    /**
     * This implementation calls the {@link Validator#doValidate} and checks
     * whether the input is a valid number in the range from {@link #getMinValue()}
     * to {@link #getMaxValue()}.
     *
     * @param text The text to validate. This is never {@code null}.
     * @param er A reference to a possible exception. If this method returns
     *         {@code false}, the encapsulated exception is set to an
     *         instance of {@link ValidatorException}. This must not be
     *         {@code null}.
     *
     * @return {@code true} if text is valid; otherwise, {@code false} is
     *         returned.
     */
    @Override
    protected boolean doValidate(String text, ValidatorExceptionRef er) {
        if (!super.doValidate(text, er))
            return false;

        if (text.isEmpty())
            return true;

        var pos = new ParsePosition(0);
        var res = displayFormat.parse(text, pos);

        if (res == null || pos.getErrorIndex() >= 0 || pos.getIndex() < text.length()) {
            er.setException(new ValidatorException(getErrorMessage() != null
                                                           ? getErrorMessage()
                                                           : getNumberErrorMessage(),
                                                   CurrencyValidator.class,
                                                   this));
            return false;
        }

        var v = res.doubleValue();

        if (minValue <= v && v <= maxValue)
            return true;

        if (getErrorMessage() != null)
            er.setException(new ValidatorException(getErrorMessage(),
                                                   CurrencyValidator.class,
                                                   this));
        else {
            String message = String.format(getRangeErrorMessage(),
                                           displayFormat.format(minValue),
                                           displayFormat.format(maxValue));
            er.setException(new ValidatorException(message,
                                                   CurrencyValidator.class,
                                                   this));
        }

        return false;
    }

    /**
     * Called to reformat the specified text.
     *
     * @param text The text to reformat. This is never {@code null}
     * @param how How to format the text. Must be one of the {@link TextFormat}
     *         values. If this is {@link TextFormat#DISPLAY}, the text is
     *         formatted to be displayed in a fine format. If this is
     *         {@link TextFormat#EDIT}, the text is formatted to be an easy
     *         editable text.
     *
     * @return The formatted string or input if the text cannot be formatted.
     */
    @Override
    protected String doFormatText(String text, TextFormat how) {
        try {
            double value = how == TextFormat.DISPLAY
                    ? displayFormat.parse(text).doubleValue()
                    : editFormat.parse(text).doubleValue();
            return how == TextFormat.DISPLAY
                    ? displayFormat.format(value)
                    : editFormat.format(value);
        } catch (ParseException e) {
            return text;
        }
    }

    private DecimalFormat getEditFormat() {
        return getFormat(locale != null ? locale : Locale.getDefault(), "");
    }

    private DecimalFormat getDisplayFormat() {
        return getFormat(
                locale != null ? locale : Locale.getDefault(),
                hideCurrencySymbol ? "" : currencySymbol);
    }

    private static DecimalFormat getFormat(Locale locale, String currencySymbol) {
        var f = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
        var s = f.getDecimalFormatSymbols();

        f.setRoundingMode(RoundingMode.HALF_UP);

        if (!Strings.isNullOrEmpty(currencySymbol)) {
            s.setCurrencySymbol(currencySymbol);
            f.setDecimalFormatSymbols(s);
        } else if (currencySymbol.isEmpty()) {
            f.applyPattern(removeCurrency(f.toPattern()));

            // It seems that the monetary separators are not used after the
            // currency symbol is removed, so we overwrite the defaults:
            //
            s.setDecimalSeparator(s.getMonetaryDecimalSeparator());
            s.setGroupingSeparator(s.getMonetaryGroupingSeparator());
            f.setDecimalFormatSymbols(s);
        }

        return f;
    }

    private static String removeCurrency(String pattern) {
        StringBuilder result = new StringBuilder(pattern.length());

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);

            if (c == '\u00A4') { // ¤
                // Remove whitespace before ¤:
                while (!result.isEmpty() && isSpace(result.charAt(result.length() - 1)))
                    result.deleteCharAt(result.length() - 1);

                // Skip whitespace behind ¤:
                while (i + 1 < pattern.length() && isSpace(pattern.charAt(i + 1)))
                    i++;
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private static boolean isSpace(char c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c);
    }
}
