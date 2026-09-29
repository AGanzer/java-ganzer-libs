package de.ganzer.core.util;

import de.ganzer.core.validation.PxPicValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * Converts {@link DateTimeFormatter} patterns into Borland Paradox picture
 * masks that can be uses with {@link PxPicValidator}.
 * <p>
 * The conversion is intended primarily for localized date/time
 * patterns returned by
 * {@link DateTimeFormatterBuilder#getLocalizedDateTimePattern}.
 * <p>
 * Examples:
 * <pre>
 * dd.MM.y      -> ##.##.####
 * d.M.y        -> [#]#.[#]#.####
 * yyyy-MM-dd   -> ####-##-##
 * HH:mm:ss     -> ##:##:##
 * H:mm         -> [#]#:##
 * </pre>
 * Localized textual fields such as month names are converted into
 * Paradox alternatives. Common prefixes are factored automatically.
 * <pre>
 * Jan.
 * Feb.
 * März
 * Apr.
 * Aug.
 * </pre>
 * <p>
 * may become:
 * <pre>
 * {Jan.,Feb.,März,A{pr.,ug.}}
 * </pre>
 * If one alternative is a prefix of another, the remaining part
 * is represented as optional:
 * <pre>
 * Mai
 * Main
 * -> Mai[n]
 * </pre>
 * <b>NOTE:</b> Time zones and padding modifiers cannot be converted into a
 * Paradox picture and will always lead into an {@code IllegalArgumentException}.
 *
 * @since 6.0.0
 */
public final class PxPicConverter {
    /**
     * Converts a DateTimeFormatter pattern into a Paradox picture.
     *
     * @param pattern DateTimeFormatter pattern
     * @param locale locale used for localized text fields
     *
     * @return Paradox picture
     *
     * @throws NullPointerException if {@code pattern} or {@code locale} is
     *          {@code null}.
     */
    public static String toParadoxPicture(String pattern, Locale locale) {
        Objects.requireNonNull(pattern, "pattern must not be null.");
        Objects.requireNonNull(locale, "locale must not be null.");

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < pattern.length(); ) {
            char c = pattern.charAt(i);

            // Quoted literal:
            //
            if (c == '\'') {
                i = appendQuotedLiteral(pattern, i, result);
                continue;
            }

            //DateTimeFormatter pattern field:
            //
            if (isPatternLetter(c)) {
                int start = i;

                while (i < pattern.length() && pattern.charAt(i) == c)
                    i++;

                int count = i - start;

                appendField(c, count, locale, result);
                continue;
            }

            // Ordinary literal:
            //
            appendLiteralCharacter(c, result);
            i++;
        }

        return result.toString();
    }

    /**
     * Converts a DateTimeFormatter pattern using the default locale.
     *
     * @param pattern DateTimeFormatter pattern
     *
     * @return Paradox picture
     *
     * @throws NullPointerException if {@code pattern} is {@code null}.
     */
    public static String toParadoxPicture(String pattern) {
        return toParadoxPicture(pattern, Locale.getDefault());
    }

    /**
     * Converts a localized date pattern into a Paradox picture.
     *
     * @param style Format style
     * @param locale locale used for localized text fields
     *
     * @return Paradox picture
     *
     * @throws NullPointerException if {@code style} or {@code locale} is
     *          {@code null}.
     * @throws IllegalArgumentException if {@code style} is not
     *          {@link FormatStyle#SHORT} or {@link FormatStyle#MEDIUM}
     */
    public static String toLocalizedDatePicture(FormatStyle style, Locale locale) {
        Objects.requireNonNull(style, "style must not be null");
        Objects.requireNonNull(locale, "locale must not be null");

        if (style != FormatStyle.SHORT && style != FormatStyle.MEDIUM)
            throw new IllegalArgumentException("style must be FormatStyle.SHORT or FormatStyle.MEDIUM");

        String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                style,
                null,
                IsoChronology.INSTANCE,
                locale);

        return toParadoxPicture(pattern, locale);
    }

    /**
     * Converts a localized date pattern using Locale.getDefault().
     *
     * @param style Format style
     *
     * @return Paradox picture
     *
     * @throws NullPointerException if {@code style} is {@code null}.
     * @throws IllegalArgumentException if {@code style} is not
     *          {@link FormatStyle#SHORT} or {@link FormatStyle#MEDIUM}
     */
    public static String toLocalizedDatePicture(FormatStyle style) {
        return toLocalizedDatePicture(style, Locale.getDefault());
    }

    /**
     * Converts a localized time pattern into a Paradox picture.
     *
     * @param style Format style
     * @param locale locale used for localized text fields
     *
     * @return Paradox picture
     *
     * @throws NullPointerException if {@code style} or {@code locale} is
     *          {@code null}.
     * @throws IllegalArgumentException if {@code style} is not
     *          {@link FormatStyle#SHORT} or {@link FormatStyle#MEDIUM}
     */
    public static String toLocalizedTimePicture(FormatStyle style, Locale locale) {
        Objects.requireNonNull(style, "style must not be null");
        Objects.requireNonNull(locale, "locale must not be null");

        if (style != FormatStyle.SHORT && style != FormatStyle.MEDIUM)
            throw new IllegalArgumentException("style must be FormatStyle.SHORT or FormatStyle.MEDIUM");

        String pattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
                null,
                style,
                IsoChronology.INSTANCE,
                locale);

        return toParadoxPicture(pattern, locale);
    }

    /**
     * Converts a localized time pattern using Locale.getDefault().
     *
     * @param style Format style
     *
     * @return Paradox picture
     *
     * @throws NullPointerException if {@code style} is {@code null}.
     * @throws IllegalArgumentException if {@code style} is not
     *          {@link FormatStyle#SHORT} or {@link FormatStyle#MEDIUM}
     */
    public static String toLocalizedTimePicture(FormatStyle style) {
        return toLocalizedTimePicture(style, Locale.getDefault());
    }

    private PxPicConverter() {
    }

    private static boolean isPatternLetter(char c) {
        return switch (c) {
            case 'G',
                 'u',
                 'y',
                 'D',
                 'M',
                 'L',
                 'd',
                 'Q',
                 'q',
                 'Y',
                 'w',
                 'W',
                 'E',
                 'e',
                 'c',
                 'F',
                 'a',
                 'B',
                 'h',
                 'K',
                 'k',
                 'H',
                 'm',
                 's',
                 'S',
                 'A',
                 'n',
                 'N',
                 'V',
                 'z',
                 'O',
                 'X',
                 'x',
                 'Z',
                 'p' -> true;

            default -> false;
        };
    }

    private static void appendField(char field, int count, Locale locale, StringBuilder result) {
        switch (field) {
            // Day of month:
            case 'd':
                appendNumericField(count, result);
                break;

            // Month:
            case 'M':
            case 'L':
                appendMonth(count, locale, result);
                break;

            // Year:
            case 'y':
            case 'u':
                appendYear(count, result);
                break;

            // Hour:
            case 'H':
            case 'k':
            case 'h':
            case 'K':
                appendNumericField(count, result);
                break;

            // Minute:
            case 'm':
                appendNumericField(count, result);
                break;

            // Second:
            case 's':
                appendNumericField(count, result);
                break;

            // Fraction of second:
            case 'S':
                appendExactDigits(count, result);
                break;

            // AM / PM / flexible day period:
            case 'a':
            case 'B':
                appendAmPm(locale, result);
                break;

            // Day of week:
            case 'E':
            case 'e':
            case 'c':
                appendDayOfWeek(field, count, locale, result);
                break;

            // Quarter:
            case 'Q':
            case 'q':
                appendQuarter(count, locale, result);
                break;

            // Week-related fields:
            case 'w':
            case 'W':
            case 'D':
            case 'F':
                appendNumericField(count, result);
                break;

            // Week-based year:
            case 'Y':
                appendNumericField(count, result);
                break;

            // Era:
            case 'G':
                appendEra(count, locale, result);
                break;

            // Time zones cannot be represented
            // meaningfully by a Paradox picture:
            case 'V':
            case 'z':
            case 'O':
            case 'X':
            case 'x':
            case 'Z':
                throw new IllegalArgumentException("Cannot convert timezone field '"
                                                           + field
                                                           + "' to a Paradox picture");

            // Numeric nano/second fields:
            case 'A':
            case 'n':
            case 'N':
                appendNumericField(count, result);
                break;

            // DateTimeFormatter padding modifier cannot be
            // represented meaningfully by a Paradox picture:
            case 'p':
                throw new IllegalArgumentException("DateTimeFormatter padding modifier 'p' is not supported");

            default:
                throw new IllegalArgumentException("Unsupported DateTimeFormatter field: " + field);
        }
    }

    private static void appendNumericField(int count, StringBuilder result) {
        if (count <= 1) {
            // One or two digits;
            // [#]# means: one optional digit followed by one mandatory digit:
            //
            result.append("#[#]");
        } else {
            appendExactDigits(count, result);
        }
    }

    private static void appendExactDigits(int count, StringBuilder result) {
        if (count > 0)
            result.append("#".repeat(count));
    }

    private static void appendYear(int count, StringBuilder result) {
        switch (count) {
            case 1:
                // DateTimeFormatter's 'y' is variable-width;
                // for ordinary localized calendar dates, four digits
                // are the useful representation:
                //
                result.append("####");
                break;

            case 2:
                result.append("##");
                break;

            default:
                appendExactDigits(count, result);
                break;
        }
    }

    private static void appendMonth(int count, Locale locale, StringBuilder result) {
        switch (count) {
            case 1:
                result.append("#[#]");
                break;

            case 2:
                result.append("##");
                break;

            // MMM = short localized month name:
            case 3:
                appendMonthNames(locale, "MMM", result);
                break;

            // MMMM = full localized month name:
            case 4:
                appendMonthNames(locale, "MMMM", result);
                break;

            // MMMMM = narrow localized month name:
            case 5:
                appendMonthNames(locale, "MMMMM", result);
                break;

            default:
                throw new IllegalArgumentException("Invalid month field width: " + count);
        }
    }

    private static void appendMonthNames(Locale locale, String formatterPattern, StringBuilder result) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterPattern, locale);
        List<String> names = new ArrayList<>(12);

        for (Month month : Month.values()) {
            // Using DateTimeFormatter here rather than
            // Month.getDisplayName() is intentional; it gives us the
            // exact formatting-context form used by the pattern:
            //
            String name = formatter.format(LocalDate.of(2000, month, 1));
            names.add(name);
        }

        appendAlternatives(names, result);
    }

    private static void appendDayOfWeek(char field, int count, Locale locale, StringBuilder result) {
        // e / c with one or two letters can be numeric:
        //
        if ((field == 'e' || field == 'c') && count <= 2) {
            appendNumericField(count, result);
            return;
        }

        String pattern;

        if (field == 'E') {
            pattern = switch (count) {
                case 3 -> "EEE";
                case 4 -> "EEEE";
                default -> "EEEEE";
            };
        } else {
            pattern = switch (count) {
                case 3 -> "eee";
                case 4 -> "eeee";
                default -> "eeeee";
            };
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
        List<String> names = new ArrayList<>(7);

        // 2020-01-06 was a Monday:
        //
        LocalDate monday = LocalDate.of(2020, 1, 6);

        for (int i = 0; i < 7; i++)
            names.add(formatter.format(monday.plusDays(i)));

        appendAlternatives(names, result);
    }

    private static void appendAmPm(Locale locale, StringBuilder result) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a", locale);
        List<String> values = List.of(
                formatter.format(LocalTime.of(9, 0)),
                formatter.format(LocalTime.of(21, 0)));

        appendAlternatives(values, result);
    }

    private static void appendQuarter(int count, Locale locale, StringBuilder result) {
        if (count <= 2) {
            appendNumericField(count, result);
            return;
        }

        String pattern = switch (count) {
            case 3 -> "QQQ";
            case 4 -> "QQQQ";
            default -> "QQQQQ";
        };

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
        List<String> names = new ArrayList<>(4);

        for (int quarter = 0; quarter < 4; quarter++) {
            LocalDate date = LocalDate.of(2000, 1 + quarter * 3, 1);
            names.add(formatter.format(date));
        }

        appendAlternatives(names, result);
    }

    private static void appendEra(int count, Locale locale, StringBuilder result) {
        String pattern = switch (count) {
            case 1, 2, 3 -> "G";
            case 4 -> "GGGG";
            default -> "GGGGG";
        };

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);

        // Use dates from the two ISO eras:
        // Year 1 = CE
        // Year 0 = BCE
        //
        List<String> values = List.of(
                formatter.format(LocalDate.of(2000, 1, 1)),
                formatter.format(LocalDate.of(0, 1, 1)));

        appendAlternatives(values, result);
    }

    /**
     * Appends a list of alternative strings as a compact Paradox
     * alternative expression.
     * <p>
     * Common prefixes are factored.
     * <pre>
     * April
     * August
     * -> A{pril,ugust}
     * </pre>
     * <pre>
     * Juni
     * Juli
     * -> Ju{ni,li}
     * </pre>
     * If one string is a prefix of another, the remainder becomes
     * optional:
     * <pre>
     * Mai
     * Main
     * -> Mai[n]
     * </pre>
     */
    private static void appendAlternatives(List<String> values, StringBuilder result) {
        List<String> unique = distinct(values);

        if (unique.isEmpty())
            return;

        if (unique.size() == 1) {
            appendLiteral(unique.get(0), result);
            return;
        }

        TrieNode root = new TrieNode();

        for (String value : unique)
            root.insert(value);

        appendTrie(root, result);
    }

    /**
     * Serializes a TrieNode.
     * <p>
     * The method returns a suffix expression for the supplied node.
     * <p>
     * Examples:
     * <pre>
     * A
     *   p r i l .
     *   u g .
     * -> A{pril.,ug.}
     * </pre>
     */
    private static void appendTrie(TrieNode node, StringBuilder result) {
        // No children means, the current prefix is a complete value:
        //
        if (node.children.isEmpty())
            return;

        // One child:
        //
        if (node.children.size() == 1) {
            Map.Entry<Character, TrieNode> entry =
                    node.children.entrySet()
                            .iterator()
                            .next();

            appendLiteralCharacter(
                    entry.getKey(),
                    result);

            TrieNode child = entry.getValue();

            // If the child itself is terminal and has no other
            // alternatives, the recursion simply continues:
            //
            if (child.isTerminal && child.children.isEmpty())
                return;

            appendTrieWithOptionalHandling(child, result);
            return;
        }

        // Multiple children:
        //
        appendAlternativeChildren(node, result);
    }

    /**
     * Handles a node after its first character has already been emitted.
     * <p>
     * Node itself is a valid completion and has exactly one or
     * more continuations.
     * <p>
     * Example:
     * <pre>
     *   Mai
     *   Main
     * </pre>
     * At this point the result already contains "Mai".
     * <p>
     * The continuation "n" is therefore optional:
     * <pre>
     *   Mai[n]
     * </pre>
     */
    private static void appendTrieWithOptionalHandling(TrieNode node, StringBuilder result) {
        if (node.isTerminal) {
            if (node.children.isEmpty())
                return;

            result.append('[');
            appendContinuation(node, result);
            result.append(']');

            return;
        }

        appendTrie(node, result);
    }

    /**
     * Appends the continuation of a node.
     */
    private static void appendContinuation(TrieNode node, StringBuilder result) {
        if (node.children.size() == 1) {
            Map.Entry<Character, TrieNode> entry =
                    node.children.entrySet()
                            .iterator()
                            .next();

            appendLiteralCharacter(entry.getKey(), result);
            appendContinuation(entry.getValue(), result);

            return;
        }

        appendAlternativeChildren(node, result);
    }

    /**
     * Serializes multiple children as a Paradox {...} alternative.
     */
    private static void appendAlternativeChildren(TrieNode node, StringBuilder result) {
        result.append('{');

        boolean first = true;

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
            if (!first)
                result.append(',');

            first = false;

            appendLiteralCharacter(entry.getKey(), result);

            TrieNode child = entry.getValue();

            // Continue down this branch:
            //
            if (!child.isTerminal || !child.children.isEmpty())
                appendTrieWithOptionalHandling(child, result);
        }

        result.append('}');
    }

    /**
     * Appends a literal character.
     * <p>
     * Paradox uses ';' to quote the following character.
     */
    private static void appendLiteralCharacter(char c, StringBuilder result) {
        if (isParadoxSpecialCharacter(c))
            result.append(';');

        result.append(c);
    }

    private static void appendLiteral(String value, StringBuilder result) {
        for (int i = 0; i < value.length(); i++)
            appendLiteralCharacter(value.charAt(i), result);
    }

    private static boolean isParadoxSpecialCharacter(char c) {
        return switch (c) {
            case '#',
                 '?',
                 '@',
                 '&',
                 '!',
                 ';',
                 '*',
                 '[',
                 ']',
                 '{',
                 '}',
                 ',' -> true;

            default -> false;
        };
    }

    private static int appendQuotedLiteral(String pattern, int index, StringBuilder result) {
        int i = index + 1;

        while (i < pattern.length()) {
            char c = pattern.charAt(i);

            if (c == '\'') {
                // '' represents a literal apostrophe:
                //
                if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '\'') {
                    appendLiteralCharacter('\'', result);
                    i += 2;

                    continue;
                }

                return i + 1;
            }

            appendLiteralCharacter(c, result);
            i++;
        }

        throw new IllegalArgumentException("Unterminated quoted literal in pattern: " + pattern);
    }

    private static List<String> distinct(List<String> values) {
        Set<String> set = new LinkedHashSet<>(values);
        return new ArrayList<>(set);
    }

    private static final class TrieNode {
        private final Map<Character, TrieNode> children = new LinkedHashMap<>();

        private boolean isTerminal;

        void insert(String value) {
            TrieNode node = this;

            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                node = node.children.computeIfAbsent(c, ignored -> new TrieNode());
            }

            node.isTerminal = true;
        }
    }
}
