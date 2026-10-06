package de.ganzer.core.validation;

import de.ganzer.core.internals.CoreMessages;
import de.ganzer.core.util.Strings;

import java.text.*;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * A validator for validating dates.
 *
 * @since 6.0.0
 */
public class TimeValidator extends PxPicValidator{
    /**
     * The default error message for input that is not in the allowed range.
     * @see #setRangeErrorMessage(String)
     */
    public final static String DEFAULT_RANGE_ERROR_MESSAGE = CoreMessages.get("inputOutOfRange");
    /**
     * The default error message for input that is not a number.
     * @see #setTimeErrorMessage(String)
     */
    public final static String DEFAULT_TIME_ERROR_MESSAGE = CoreMessages.get("inputIsNotDaytime");

    private String rangeErrorMessage = DEFAULT_RANGE_ERROR_MESSAGE;
    private String timeErrorMessage = DEFAULT_TIME_ERROR_MESSAGE;
    private LocalTime minTime = LocalTime.MIN;
    private LocalTime maxTime = LocalTime.MAX;
    private FormatStyle formatStyle = FormatStyle.SHORT;
    private String patternString;
    private Locale locale;

    /**
     * Creates a new instance of the validator.
     * <p>
     * {@code ValidatorOptions#NEEDS_INPUT | ValidatorOptions#AUTO_FILL}.
     * The smallest allowed value to {@code LocalTime#MIN} and the greatest
     * allowed value to {@code LocalTime#MAX}.
     */
    public TimeValidator() {
    }

    /**
     * Creates a new instance of the validator.
     * <p>
     * This sets the smallest allowed value to {@code LocalTime#MIN} and
     * the greatest allowed value to {@code LocalTime#MAX}.
     *
     * @param options The options to set. This may be any combination of the
     *                {@link ValidatorOptions} constants.
     */
    public TimeValidator(int options) {
        super(options);
    }

    /**
     * Creates a new date validator.
     * <p>
     * {@code ValidatorOptions#NEEDS_INPUT | ValidatorOptions#AUTO_FILL}.
     *
     * @param minTime The minimum allowed date. If this is {@code null},
     *        {@link LocalTime#MIN} is used.
     * @param maxTime The maximum allowed date. If this is {@code null},
     *        {@link LocalTime#MAX} is used.
     *
     * @throws IllegalArgumentException If {@code minDate} is greater than
     *         {@code maxDate}.
     */
    public TimeValidator(LocalTime minTime, LocalTime maxTime) {
        this(ValidatorOptions.NEEDS_INPUT | ValidatorOptions.AUTO_FILL, minTime, maxTime);
    }

    /**
     * Creates a new date validator.
     *
     * @param minTime The minimum allowed date. If this is {@code null},
     *        {@link LocalTime#MIN} is used.
     * @param maxTime The maximum allowed date. If this is {@code null},
     *        {@link LocalTime#MAX} is used.
     *
     * @param options The options to set. This may be any combination of the
     *                {@link ValidatorOptions} constants.
     *
     * @throws IllegalArgumentException If {@code minDate} is greater than
     *         {@code maxDate}.
     */
    public TimeValidator(int options, LocalTime minTime, LocalTime maxTime) {
        super(options);

        this.minTime = minTime != null ? minTime : LocalTime.MIN;
        this.maxTime = maxTime != null ? maxTime : LocalTime.MAX;

        if (this.minTime.isAfter(this.maxTime))
            throw new IllegalArgumentException("minTime/maxTime");

        updatePicture();
    }

    /**
     * Gets the minimum allowed date.
     *
     * @return The minimum allowed date. The default is {@link LocalTime#MIN}.
     */
    public LocalTime getMinTime() {
        return minTime;
    }

    /**
     * Sets the minimum allowed date.
     *
     * @param minTime The minimum allowed date. If this is {@code null},
     *        {@link LocalTime#MIN} is used. If this is greater than
     *        {@link #getMaxTime()}, the greatest allowed date ist set
     *        to this value too.
     */
    public void setMinTime(LocalTime minTime) {
        this.minTime = minTime != null ? minTime : LocalTime.MIN;

        if (this.maxTime.isBefore(this.minTime))
            this.maxTime = this.minTime;
    }

    /**
     * Gets the maximum allowed date.
     *
     * @return The maximum allowed date. The default is {@link LocalTime#MAX}.
     */
    public LocalTime getMaxTime() {
        return maxTime;
    }

    /**
     * Sets the maximum allowed date.
     *
     * @param maxTime The maximum allowed date. If this is {@code null},
     *        {@link LocalTime#MAX} is used. If this is less than
     *        {@link #getMinTime()}, the minimum allowed date ist set
     *        to this value too.
     */
    public void setMaxTime(LocalTime maxTime) {
        this.maxTime = maxTime != null ? maxTime : LocalTime.MAX;

        if (this.minTime.isAfter(this.maxTime))
            this.maxTime = this.minTime;
    }

    /**
     * Sets the allowed range.
     *
     * @param minDate The minimum allowed date. If this is {@code null},
     *        {@link LocalTime#MIN} is used.
     * @param maxDate The maximum allowed date. If this is {@code null},
     *        {@link LocalTime#MAX} is used.
     *
     * @throws IllegalArgumentException If {@code minDate} is greater than
     *         {@code maxDate}.
     */
    public void setRange(LocalTime minDate, LocalTime maxDate) {
        this.minTime = minDate != null ? minDate : LocalTime.MIN;
        this.maxTime = maxDate != null ? maxDate : LocalTime.MAX;

        if (this.minTime.isAfter(this.maxTime))
            throw new IllegalArgumentException("minTime/maxTime");
    }

    /**
     * Gets the format style used to formatting dates.
     * <p>
     * <b>NOTE:</b> This is ignored if {@link #getPatternString()} is not
     * {@code null}.
     *
     * @return The format style used to formatting dates. The default is
     *         {@link FormatStyle#SHORT}.
     */
    public FormatStyle getFormatStyle() {
        return formatStyle;
    }

    /**
     * Sets the format style used to formatting dates.
     * <p>
     * <b>NOTE:</b> This is ignored if {@link #getPatternString()} is not
     * {@code null}.
     *
     * @param formatStyle The format style used to formatting dates.
     */
    public void setFormatStyle(FormatStyle formatStyle) {
        this.formatStyle = formatStyle;
        updatePicture();
    }

    /**
     * Gets the format string used to formatting dates.
     *
     * @return The format string used to formatting dates or {@code null} if the
     *         default format of the locale gotten by {@link getLocale()} is used.
     */
    public String getPatternString() {
        return patternString;
    }

    /**
     * Sets the format string used to formatting dates.
     *
     * @param patternString The format string used to formatting dates or
     *        {@code null} to use the default format of the locale gotten by
     *        {@link getLocale()}.
     */
    public void setPatternString(String patternString) {
        this.patternString = patternString;
        updatePicture();
    }

    /**
     * Gets the locale to use to format dates.
     *
     * @return The locale to use to format dates or {@code null} if the
     *         default locale is used.
     *
     * @see Locale#getDefault()
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets the locale to use to format dates.
     *
     * @param locale The locale to use to format dates or {@code null} to use
     *               the default locale.
     *
     * @see Locale#getDefault()
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
        updatePicture();
    }

    /**
     * Gets the message shown if the input is out of the allowed range.
     *
     * @return The error message to use. The default is
     *         {@link #DEFAULT_RANGE_ERROR_MESSAGE}.
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
     *        {@code null}, empty or does contain white spaces only,
     *        {@link #DEFAULT_RANGE_ERROR_MESSAGE} is used.
     */
    public void setRangeErrorMessage(String rangeErrorMessage) {
        this.rangeErrorMessage = Strings.isNullOrBlank(rangeErrorMessage)
                ? DEFAULT_RANGE_ERROR_MESSAGE
                : rangeErrorMessage;
    }

    /**
     * Gets the message shown if the input is not a valid date.
     *
     * @return The error message to use. The default is
     *         {@link #DEFAULT_TIME_ERROR_MESSAGE}.
     */
    public String getTimeErrorMessage() {
        return timeErrorMessage;
    }

    /**
     * Sets the message shown if the input is not a valid date.
     *
     * @param numberErrorMessage The message to use. If this is
     *        {@code null}, empty or does contain white spaces only,
     *        {@link #DEFAULT_TIME_ERROR_MESSAGE} is used.
     */
    public void setTimeErrorMessage(String numberErrorMessage) {
        this.timeErrorMessage = Strings.isNullOrBlank(numberErrorMessage)
                ? DEFAULT_TIME_ERROR_MESSAGE
                : numberErrorMessage;
    }

    /**
     * This implementation calls the {@link Validator#doValidate} and checks
     * whether the input is a valid number in the range from {@link #getMinTime()}
     * to {@link #getMaxTime()}.
     *
     * @param text The text to validate. This is never {@code null}.
     * @param er   A reference to a possible exception. If this method returns
     *             {@code false}, the encapsulated exception is set to an
     *             instance of {@link ValidatorException}. This must not be
     *             {@code null}.
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

        var loc = locale != null ? locale : Locale.getDefault();
        var formatter = patternString != null
                ? DateTimeFormatter.ofPattern(patternString, loc)
                : DateTimeFormatter.ofLocalizedTime(formatStyle).withLocale(loc);
        LocalTime value;

        try {
            value = LocalTime.from(formatter.parse(text));
        } catch (DateTimeException e) {
            er.setException(new ValidatorException(getErrorMessage() != null
                                                           ? getErrorMessage()
                                                           : getTimeErrorMessage(),
                                                   TimeValidator.class,
                                                   this));
            return false;
        }

        if (value.isBefore(minTime) || value.isAfter(maxTime)) {
            er.setException(new ValidatorException(getErrorMessage() != null
                                                       ? getErrorMessage()
                                                       : String.format(loc, getRangeErrorMessage(),
                                                                       formatter.format(minTime),
                                                                       formatter.format(maxTime)),
                                               TimeValidator.class,
                                               this));
            return false;
        }

        return true;
    }

    private void updatePicture() {
        Locale loc = locale != null ? locale : Locale.getDefault();

        if (patternString != null)
            setPicture(PxPicConverter.toPicture(patternString, loc));
        else
            setPicture(PxPicConverter.toTimePicture(formatStyle, loc));
    }
}
