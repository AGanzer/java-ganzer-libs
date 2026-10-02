package de.ganzer.core.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.format.FormatStyle;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PxPicConverterTest {

    @Nested
    @DisplayName("Tests for toParadoxPicture(String, Locale)")
    class ToParadoxPictureWithLocaleTests {

        @Test
        void shouldThrowExceptionWhenPatternIsNull() {
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toParadoxPicture(null, Locale.GERMANY)
            );
            assertEquals("pattern must not be null.", ex.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenLocaleIsNull() {
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toParadoxPicture("yyyy-MM-dd", null)
            );
            assertEquals("locale must not be null.", ex.getMessage());
        }

        @Test
        void shouldConvertNumericDatePatterns() {
            assertEquals("##.##.####", PxPicConverter.toParadoxPicture("dd.MM.yyyy", Locale.GERMANY));
            assertEquals("#[#].#[#].####", PxPicConverter.toParadoxPicture("d.M.y", Locale.GERMANY));
            assertEquals("####-##-##", PxPicConverter.toParadoxPicture("yyyy-MM-dd", Locale.US));
            assertEquals("##-##-##", PxPicConverter.toParadoxPicture("yy-MM-dd", Locale.US));
            assertEquals("###-##-##", PxPicConverter.toParadoxPicture("yyy-MM-dd", Locale.US));
            assertEquals("####-##-##", PxPicConverter.toParadoxPicture("uuuu-MM-dd", Locale.US));
            assertEquals("##-##-##", PxPicConverter.toParadoxPicture("uu-MM-dd", Locale.US));
            assertEquals("####-##-##", PxPicConverter.toParadoxPicture("u-MM-dd", Locale.US));
        }

        @Test
        void shouldConvertTimePatterns() {
            assertEquals("##:##:##", PxPicConverter.toParadoxPicture("HH:mm:ss", Locale.GERMANY));
            assertEquals("#[#]:#[#]:#[#]", PxPicConverter.toParadoxPicture("H:m:s", Locale.GERMANY));
            assertEquals("#[#]:#[#]", PxPicConverter.toParadoxPicture("k:K", Locale.GERMANY));
            assertEquals("#[#]:##", PxPicConverter.toParadoxPicture("h:mm", Locale.US));
            assertEquals("#", PxPicConverter.toParadoxPicture("S", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toParadoxPicture("SS", Locale.GERMANY));
            assertEquals("###", PxPicConverter.toParadoxPicture("SSS", Locale.GERMANY));
            assertEquals("######", PxPicConverter.toParadoxPicture("SSSSSS", Locale.GERMANY));
        }

        @Test
        void shouldConvertWeekAndDayFields() {
            assertEquals("#[#]", PxPicConverter.toParadoxPicture("w", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toParadoxPicture("ww", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toParadoxPicture("W", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toParadoxPicture("D", Locale.GERMANY));
            assertEquals("###", PxPicConverter.toParadoxPicture("DDD", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toParadoxPicture("F", Locale.GERMANY));
            assertEquals("####", PxPicConverter.toParadoxPicture("YYYY", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toParadoxPicture("A", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toParadoxPicture("n", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toParadoxPicture("N", Locale.GERMANY));
        }

        @Test
        void shouldConvertAmPmAndFlexibleDayPeriod() {
            assertEquals("{AM,PM}", PxPicConverter.toParadoxPicture("a", Locale.US));
            assertEquals("{AM,PM}", PxPicConverter.toParadoxPicture("B", Locale.US));
        }

        @Test
        void shouldConvertDayOfWeekFields() {
            assertEquals("#[#]", PxPicConverter.toParadoxPicture("e", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toParadoxPicture("ee", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toParadoxPicture("c", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toParadoxPicture("cc", Locale.GERMANY));

            String eShort = PxPicConverter.toParadoxPicture("EEE", Locale.GERMANY);
            assertNotNull(eShort);
            assertTrue(eShort.startsWith("{") && eShort.endsWith("}"));

            String eFull = PxPicConverter.toParadoxPicture("EEEE", Locale.GERMANY);
            assertNotNull(eFull);
            assertTrue(eFull.startsWith("{") && eFull.endsWith("}"));

            String eNarrow = PxPicConverter.toParadoxPicture("EEEEE", Locale.GERMANY);
            assertNotNull(eNarrow);

            String eee = PxPicConverter.toParadoxPicture("eee", Locale.GERMANY);
            assertNotNull(eee);
            String eeee = PxPicConverter.toParadoxPicture("eeee", Locale.GERMANY);
            assertNotNull(eeee);
            String eeeee = PxPicConverter.toParadoxPicture("eeeee", Locale.GERMANY);
            assertNotNull(eeeee);
        }

        @Test
        void shouldConvertQuarters() {
            assertEquals("#[#]", PxPicConverter.toParadoxPicture("Q", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toParadoxPicture("QQ", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toParadoxPicture("q", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toParadoxPicture("qq", Locale.GERMANY));

            String q3 = PxPicConverter.toParadoxPicture("QQQ", Locale.GERMANY);
            assertNotNull(q3);
            String q4 = PxPicConverter.toParadoxPicture("QQQQ", Locale.GERMANY);
            assertNotNull(q4);
            String q5 = PxPicConverter.toParadoxPicture("QQQQQ", Locale.GERMANY);
            assertNotNull(q5);
            String qLower3 = PxPicConverter.toParadoxPicture("qqq", Locale.GERMANY);
            assertNotNull(qLower3);
            String qLower4 = PxPicConverter.toParadoxPicture("qqqq", Locale.GERMANY);
            assertNotNull(qLower4);
            String qLower5 = PxPicConverter.toParadoxPicture("qqqqq", Locale.GERMANY);
            assertNotNull(qLower5);
        }

        @Test
        void shouldConvertEra() {
            String g1 = PxPicConverter.toParadoxPicture("G", Locale.US);
            assertEquals("{AD,BC}", g1);

            String g4 = PxPicConverter.toParadoxPicture("GGGG", Locale.US);
            assertEquals("{Anno Domini,Before Christ}", g4);

            String g5 = PxPicConverter.toParadoxPicture("GGGGG", Locale.US);
            assertEquals("{A,B}", g5);
        }

        @Test
        void shouldConvertMonthNamesAndFactorPrefixes() {
            String mmm = PxPicConverter.toParadoxPicture("MMM", Locale.GERMANY);
            assertNotNull(mmm);
            assertTrue(mmm.startsWith("{") && mmm.endsWith("}"));

            String mmmm = PxPicConverter.toParadoxPicture("MMMM", Locale.GERMANY);
            assertNotNull(mmmm);
            assertTrue(mmmm.startsWith("{") && mmmm.endsWith("}"));

            String mmmmm = PxPicConverter.toParadoxPicture("MMMMM", Locale.GERMANY);
            assertNotNull(mmmmm);

            String lll = PxPicConverter.toParadoxPicture("LLL", Locale.GERMANY);
            assertEquals(mmm, lll);
            String llll = PxPicConverter.toParadoxPicture("LLLL", Locale.GERMANY);
            assertEquals(mmmm, llll);
            String lllll = PxPicConverter.toParadoxPicture("LLLLL", Locale.GERMANY);
            assertEquals(mmmmm, lllll);
        }

        @Test
        void shouldThrowExceptionOnInvalidMonthWidth() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toParadoxPicture("MMMMMM", Locale.GERMANY)
            );
            assertTrue(ex.getMessage().contains("Invalid month field width"));
        }

        @Test
        void shouldHandleQuotedLiteralsAndEscapeApostrophes() {
            assertEquals("T", PxPicConverter.toParadoxPicture("'T'", Locale.US));
            assertEquals("o'clock", PxPicConverter.toParadoxPicture("'o''clock'", Locale.US));
            assertEquals("yyyy", PxPicConverter.toParadoxPicture("'yyyy'", Locale.US));
            assertEquals(";#;?;@;&;!;;;*;[;];;;{;};,", PxPicConverter.toParadoxPicture("'#?@&!;*[];{},'", Locale.US));
        }

        @Test
        void shouldThrowExceptionOnUnterminatedQuotedLiteral() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toParadoxPicture("'unclosed", Locale.US)
            );
            assertTrue(ex.getMessage().contains("Unterminated quoted literal in pattern"));
        }

        @Test
        void shouldEscapeParadoxSpecialCharactersInOrdinaryLiterals() {
            assertEquals("####/##/##", PxPicConverter.toParadoxPicture("yyyy/MM/dd", Locale.US));
            assertEquals("####-##-##", PxPicConverter.toParadoxPicture("yyyy-MM-dd", Locale.US));
            assertEquals("####.##.##", PxPicConverter.toParadoxPicture("yyyy.MM.dd", Locale.US));
            assertEquals("####;###", PxPicConverter.toParadoxPicture("yyyy#dd", Locale.US));
            assertEquals("####;[##;]", PxPicConverter.toParadoxPicture("yyyy[dd]", Locale.US));
            assertEquals("####;{##;,##;}", PxPicConverter.toParadoxPicture("yyyy{MM,dd}", Locale.US));
            assertEquals("####;;##", PxPicConverter.toParadoxPicture("yyyy;dd", Locale.US));
            assertEquals("####;?##", PxPicConverter.toParadoxPicture("yyyy?dd", Locale.US));
            assertEquals("####;@##", PxPicConverter.toParadoxPicture("yyyy@dd", Locale.US));
            assertEquals("####;&##", PxPicConverter.toParadoxPicture("yyyy&dd", Locale.US));
            assertEquals("####;!##", PxPicConverter.toParadoxPicture("yyyy!dd", Locale.US));
            assertEquals("####;*##", PxPicConverter.toParadoxPicture("yyyy*dd", Locale.US));
        }

        @ParameterizedTest
        @ValueSource(chars = {'V', 'z', 'O', 'X', 'x', 'Z'})
        void shouldThrowExceptionOnTimezoneFields(char timezoneChar) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toParadoxPicture("HH:mm:ss " + timezoneChar, Locale.US)
            );
            assertTrue(ex.getMessage().contains("Cannot convert timezone field"));
        }

        @Test
        void shouldThrowExceptionOnPaddingModifier() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toParadoxPicture("ppHH", Locale.US)
            );
            assertTrue(ex.getMessage().contains("DateTimeFormatter padding modifier 'p' is not supported"));
        }
    }

    @Nested
    @DisplayName("Tests for toParadoxPicture(String)")
    class ToParadoxPictureDefaultLocaleTests {

        @Test
        void shouldThrowExceptionWhenPatternIsNull() {
            assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toParadoxPicture(null)
            );
        }

        @Test
        void shouldConvertUsingDefaultLocale() {
            String resultWithDefault = PxPicConverter.toParadoxPicture("yyyy-MM-dd");
            String resultExplicit = PxPicConverter.toParadoxPicture("yyyy-MM-dd", Locale.getDefault());
            assertEquals(resultExplicit, resultWithDefault);
            assertEquals("####-##-##", resultWithDefault);
        }
    }

    @Nested
    @DisplayName("Tests for toLocalizedDatePicture(FormatStyle, Locale)")
    class ToLocalizedDatePictureWithLocaleTests {

        @Test
        void shouldThrowExceptionWhenStyleIsNull() {
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toLocalizedDatePicture(null, Locale.GERMANY)
            );
            assertEquals("style must not be null", ex.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenLocaleIsNull() {
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toLocalizedDatePicture(FormatStyle.SHORT, null)
            );
            assertEquals("locale must not be null", ex.getMessage());
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"SHORT", "MEDIUM"})
        void shouldConvertValidStyles(FormatStyle style) {
            String germanDatePic = PxPicConverter.toLocalizedDatePicture(style, Locale.GERMANY);
            assertNotNull(germanDatePic);
            assertFalse(germanDatePic.isEmpty());
            assertTrue(new PxPicValidator().checkSyntax(germanDatePic));

            String usDatePic = PxPicConverter.toLocalizedDatePicture(style, Locale.US);
            assertNotNull(usDatePic);
            assertFalse(usDatePic.isEmpty());
            assertTrue(new PxPicValidator().checkSyntax(usDatePic));
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"LONG", "FULL"})
        void shouldThrowExceptionOnUnsupportedStyles(FormatStyle style) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toLocalizedDatePicture(style, Locale.GERMANY)
            );
            assertEquals("style must be FormatStyle.SHORT or FormatStyle.MEDIUM", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests for toLocalizedDatePicture(FormatStyle)")
    class ToLocalizedDatePictureDefaultLocaleTests {

        @Test
        void shouldThrowExceptionWhenStyleIsNull() {
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toLocalizedDatePicture(null)
            );
            assertEquals("style must not be null", ex.getMessage());
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"SHORT", "MEDIUM"})
        void shouldConvertUsingDefaultLocale(FormatStyle style) {
            String resultDefault = PxPicConverter.toLocalizedDatePicture(style);
            String resultExplicit = PxPicConverter.toLocalizedDatePicture(style, Locale.getDefault());
            assertEquals(resultExplicit, resultDefault);
            assertTrue(new PxPicValidator().checkSyntax(resultDefault));
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"LONG", "FULL"})
        void shouldThrowExceptionOnUnsupportedStyles(FormatStyle style) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toLocalizedDatePicture(style)
            );
            assertEquals("style must be FormatStyle.SHORT or FormatStyle.MEDIUM", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests for toLocalizedTimePicture(FormatStyle, Locale)")
    class ToLocalizedTimePictureWithLocaleTests {

        @Test
        void shouldThrowExceptionWhenStyleIsNull() {
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toLocalizedTimePicture(null, Locale.GERMANY)
            );
            assertEquals("style must not be null", ex.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenLocaleIsNull() {
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toLocalizedTimePicture(FormatStyle.SHORT, null)
            );
            assertEquals("locale must not be null", ex.getMessage());
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"SHORT", "MEDIUM"})
        void shouldConvertValidStyles(FormatStyle style) {
            String germanTimePic = PxPicConverter.toLocalizedTimePicture(style, Locale.GERMANY);
            assertNotNull(germanTimePic);
            assertFalse(germanTimePic.isEmpty());
            assertTrue(new PxPicValidator().checkSyntax(germanTimePic));

            String usTimePic = PxPicConverter.toLocalizedTimePicture(style, Locale.US);
            assertNotNull(usTimePic);
            assertFalse(usTimePic.isEmpty());
            assertTrue(new PxPicValidator().checkSyntax(usTimePic));
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"LONG", "FULL"})
        void shouldThrowExceptionOnUnsupportedStyles(FormatStyle style) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toLocalizedTimePicture(style, Locale.GERMANY)
            );
            assertEquals("style must be FormatStyle.SHORT or FormatStyle.MEDIUM", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Tests for toLocalizedTimePicture(FormatStyle)")
    class ToLocalizedTimePictureDefaultLocaleTests {

        @Test
        void shouldThrowExceptionWhenStyleIsNull() {
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toLocalizedTimePicture(null)
            );
            assertEquals("style must not be null", ex.getMessage());
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"SHORT", "MEDIUM"})
        void shouldConvertUsingDefaultLocale(FormatStyle style) {
            String resultDefault = PxPicConverter.toLocalizedTimePicture(style);
            String resultExplicit = PxPicConverter.toLocalizedTimePicture(style, Locale.getDefault());
            assertEquals(resultExplicit, resultDefault);
            assertTrue(new PxPicValidator().checkSyntax(resultDefault));
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"LONG", "FULL"})
        void shouldThrowExceptionOnUnsupportedStyles(FormatStyle style) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toLocalizedTimePicture(style)
            );
            assertEquals("style must be FormatStyle.SHORT or FormatStyle.MEDIUM", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Comprehensive Pattern and Validator integration tests")
    class ValidatorIntegrationTests {

        @Test
        void shouldValidateLocalizedGermanShortDate() {
            String pic = PxPicConverter.toLocalizedDatePicture(FormatStyle.SHORT, Locale.GERMANY);
            var validator = new PxPicValidator(pic);

            assertTrue(validator.isValidInput(new StringBuilder("01.01.24"), false));
            assertDoesNotThrow(() -> validator.validate("01.01.24"));
        }

        @Test
        void shouldValidateLocalizedGermanMediumDate() {
            String pic = PxPicConverter.toLocalizedDatePicture(FormatStyle.MEDIUM, Locale.GERMANY);
            var validator = new PxPicValidator(pic);

            assertTrue(validator.isValidInput(new StringBuilder("01.01.2024"), false));
            assertDoesNotThrow(() -> validator.validate("01.01.2024"));
        }

        @Test
        void shouldValidateLocalizedGermanShortTime() {
            String pic = PxPicConverter.toLocalizedTimePicture(FormatStyle.SHORT, Locale.GERMANY);
            var validator = new PxPicValidator(pic);

            assertTrue(validator.isValidInput(new StringBuilder("14:30"), false));
            assertDoesNotThrow(() -> validator.validate("14:30"));
        }

        @Test
        void shouldValidateLocalizedGermanMediumTime() {
            String pic = PxPicConverter.toLocalizedTimePicture(FormatStyle.MEDIUM, Locale.GERMANY);
            var validator = new PxPicValidator(pic);

            assertTrue(validator.isValidInput(new StringBuilder("14:30:45"), false));
            assertDoesNotThrow(() -> validator.validate("14:30:45"));
        }
    }
}
