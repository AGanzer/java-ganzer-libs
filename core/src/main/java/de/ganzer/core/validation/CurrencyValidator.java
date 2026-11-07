package de.ganzer.core.validation;

import de.ganzer.core.internals.CoreMessages;
import de.ganzer.core.util.Strings;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.time.format.DecimalStyle;
import java.util.Locale;

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
    private double minValue = Long.MIN_VALUE;
    private double maxValue = Long.MAX_VALUE;
    private Locale locale;
    private boolean hideCurrencySymbol;
    private String currencySymbol;

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
        this.hideCurrencySymbol = hideCurrencySymbol;
    }

    /**
     * Gets the currency symbol to use if the locale's default symbol should not
     * be used.
     *
     * @return The currency symbol to use or {@code null} to use the default
     *         symbol.
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
        this.currencySymbol = currencySymbol;
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
        this.locale = locale;
    }

    /**
     * Converts the given text into a {@code double} number.
     *
     * @param text The text to convert. This should be the same text that is
     *         successfully passed to {@link #validate(String)}.
     *
     * @return The converted value.
     *
     * @throws ParseException If the text is not a valid number.
     */
    public double asDouble(String text) throws ParseException {
        Locale loc = locale != null ? locale : Locale.getDefault();
        return parseNumber(text, loc);
    }

    /**
     * Converts the given text into a {@link BigDecimal}.
     *
     * @param text The text to convert. This should be the same text that is
     *         successfully passed to {@link #validate(String)}.
     *
     * @return The converted value.
     *
     * @throws ParseException If the text is not a valid number.
     */
    public BigDecimal asBigDecimal(String text) throws ParseException {
        Locale loc = locale != null ? locale : Locale.getDefault();
        return new BigDecimal(parseNumber(text, loc));
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

        var loc = locale != null ? locale : Locale.getDefault();
        var d = DecimalStyle.of(loc);
        var t = text.toString();

        if (t.length() == 1 && t.charAt(0) == d.getNegativeSign())
            return minValue < 0;

        var pos = new ParsePosition(0);
        var res = NumberFormat.getInstance(loc).parse(t, pos);

        return res != null && pos.getErrorIndex() < 0 && pos.getIndex() == t.length();
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

        Locale loc = locale != null ? locale : Locale.getDefault();
        String symbol = resolveCurrencySymbol(loc);
        String numberPart = text;

        if (hasCurrency(text, loc)) {
            boolean isPrefix = isCurrencyPrefix(loc);

            if (isPrefix) {
                if (numberPart.startsWith(symbol + " ") || numberPart.startsWith(symbol + "\u00A0") || numberPart.startsWith(symbol)) {
                    numberPart = numberPart.substring(symbol.length()).trim();
                } else {
                    er.setException(new ValidatorException(getErrorMessage() != null
                                                                   ? getErrorMessage()
                                                                   : getNumberErrorMessage(),
                                                           CurrencyValidator.class,
                                                           this));
                    return false;
                }
            } else {
                if (numberPart.endsWith(" " + symbol) || numberPart.endsWith("\u00A0" + symbol)) {
                    numberPart = numberPart.substring(0, numberPart.length() - symbol.length()).trim();
                } else {
                    er.setException(new ValidatorException(getErrorMessage() != null
                                                                   ? getErrorMessage()
                                                                   : getNumberErrorMessage(),
                                                           CurrencyValidator.class,
                                                           this));
                    return false;
                }
            }
        }

        var pos = new ParsePosition(0);
        var res = NumberFormat.getInstance(loc).parse(numberPart, pos);

        if (res == null || pos.getErrorIndex() >= 0 || pos.getIndex() < numberPart.length()) {
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
                                           formatDisplay(minValue, loc),
                                           formatDisplay(maxValue, loc));
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
            Locale loc = locale != null ? locale : Locale.getDefault();
            double value = parseNumber(text, loc);

            return how == TextFormat.DISPLAY
                    ? formatDisplay(value, loc)
                    : formatEdit(value, loc);
        } catch (ParseException e) {
            return text;
        }
    }

    private double parseNumber(String text, Locale loc) throws ParseException {
        if (text == null || text.isEmpty())
            throw new ParseException("empty text", 0);

        String symbol = resolveCurrencySymbol(loc);
        String numberPart = text;

        if (hasCurrency(text, loc)) {
            boolean isPrefix = isCurrencyPrefix(loc);

            if (isPrefix) {
                if (numberPart.startsWith(symbol))
                    numberPart = numberPart.substring(symbol.length()).trim();
            } else {
                if (numberPart.endsWith(symbol))
                    numberPart = numberPart.substring(0, numberPart.length() - symbol.length()).trim();
            }
        }

        var pos = new ParsePosition(0);
        var res = NumberFormat.getInstance(loc).parse(numberPart, pos);

        if (res == null || pos.getErrorIndex() >= 0 || pos.getIndex() < numberPart.length())
            throw new ParseException(text, pos.getErrorIndex() >= 0 ? pos.getErrorIndex() : pos.getIndex());

        return res.doubleValue();
    }

    private String formatDisplay(double value, Locale loc) {
        String numStr = String.format(loc, "%,.2f", value);

        if (hideCurrencySymbol)
            return numStr;

        String symbol = resolveCurrencySymbol(loc);

        return isCurrencyPrefix(loc)
                ? symbol + numStr
                : numStr + " " + symbol;
    }

    private String formatEdit(double value, Locale loc) {
        return String.format(loc, "%.2f", value);
    }

    private boolean isCurrencyPrefix(Locale loc) {
        Locale effectiveLocale = getLocaleWithCountry(loc);

        if (effectiveLocale == null)
            effectiveLocale = loc;

        var df = (DecimalFormat) NumberFormat.getCurrencyInstance(effectiveLocale);

        return df.toPattern().startsWith("\u00A4");
    }

    private boolean hasCurrency(String text, Locale loc) {
        if (text == null || text.isEmpty())
            return false;

        if (!Strings.isNullOrEmpty(currencySymbol) && text.contains(currencySymbol))
            return true;

        String symbol = resolveCurrencySymbol(loc);

        return !Strings.isNullOrEmpty(symbol) && !symbol.equals("\u00A4") && text.contains(symbol);
    }

    private String resolveCurrencySymbol(Locale loc) {
        if (!Strings.isNullOrEmpty(currencySymbol))
            return currencySymbol;

        var symbols = DecimalFormatSymbols.getInstance(loc);
        var symbol = symbols.getCurrencySymbol();

        if (Strings.isNullOrEmpty(symbol) || symbol.equals("\u00A4")) {
            Locale countryLocale = getLocaleWithCountry(loc);

            if (countryLocale != null) {
                var cs = DecimalFormatSymbols.getInstance(countryLocale);

                if (!Strings.isNullOrEmpty(cs.getCurrencySymbol()) && !cs.getCurrencySymbol().equals("\u00A4"))
                    return cs.getCurrencySymbol();
            }
        }

        return symbol;
    }

    private static Locale getLocaleWithCountry(Locale loc) {
        if (!loc.getCountry().isEmpty())
            return loc;

        String lang = loc.getLanguage();

        switch (lang) {
            case "de":
                return Locale.GERMANY;

            case "fr":
                return Locale.FRANCE;

            case "it":
                return Locale.ITALY;

            case "en":
                return Locale.US;

            case "es":
                return Locale.forLanguageTag("es-ES");

            case "ja":
                return Locale.JAPAN;

            case "zh":
                return Locale.CHINA;
        }

        for (Locale available : Locale.getAvailableLocales()) {
            if (available.getLanguage().equals(lang) && !available.getCountry().isEmpty())
                return available;
        }

        return null;
    }
}
