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
                    () -> PxPicConverter.toPicture(null, Locale.GERMANY)
            );
            assertEquals("pattern must not be null.", ex.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenLocaleIsNull() {
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toPicture("yyyy-MM-dd", null)
            );
            assertEquals("locale must not be null.", ex.getMessage());
        }

        @Test
        void shouldConvertNumericDatePatterns() {
            assertEquals("##.##.####", PxPicConverter.toPicture("dd.MM.yyyy", Locale.GERMANY));
            assertEquals("#[#].#[#].####", PxPicConverter.toPicture("d.M.y", Locale.GERMANY));
            assertEquals("####-##-##", PxPicConverter.toPicture("yyyy-MM-dd", Locale.US));
            assertEquals("##-##-##", PxPicConverter.toPicture("yy-MM-dd", Locale.US));
            assertEquals("###-##-##", PxPicConverter.toPicture("yyy-MM-dd", Locale.US));
            assertEquals("####-##-##", PxPicConverter.toPicture("uuuu-MM-dd", Locale.US));
            assertEquals("##-##-##", PxPicConverter.toPicture("uu-MM-dd", Locale.US));
            assertEquals("####-##-##", PxPicConverter.toPicture("u-MM-dd", Locale.US));
        }

        @Test
        void shouldConvertTimePatterns() {
            assertEquals("##:##:##", PxPicConverter.toPicture("HH:mm:ss", Locale.GERMANY));
            assertEquals("#[#]:#[#]:#[#]", PxPicConverter.toPicture("H:m:s", Locale.GERMANY));
            assertEquals("#[#]:#[#]", PxPicConverter.toPicture("k:K", Locale.GERMANY));
            assertEquals("#[#]:##", PxPicConverter.toPicture("h:mm", Locale.US));
            assertEquals("#", PxPicConverter.toPicture("S", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toPicture("SS", Locale.GERMANY));
            assertEquals("###", PxPicConverter.toPicture("SSS", Locale.GERMANY));
            assertEquals("######", PxPicConverter.toPicture("SSSSSS", Locale.GERMANY));
        }

        @Test
        void shouldConvertWeekAndDayFields() {
            assertEquals("#[#]", PxPicConverter.toPicture("w", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toPicture("ww", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toPicture("W", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toPicture("D", Locale.GERMANY));
            assertEquals("###", PxPicConverter.toPicture("DDD", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toPicture("F", Locale.GERMANY));
            assertEquals("####", PxPicConverter.toPicture("YYYY", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toPicture("A", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toPicture("n", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toPicture("N", Locale.GERMANY));
        }

        @Test
        void shouldConvertAmPmAndFlexibleDayPeriod() {
            assertEquals("{AM,PM}", PxPicConverter.toPicture("a", Locale.US));
            assertEquals("{AM,PM}", PxPicConverter.toPicture("B", Locale.US));
        }

        @Test
        void shouldConvertDayOfWeekFields() {
            assertEquals("#[#]", PxPicConverter.toPicture("e", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toPicture("ee", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toPicture("c", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toPicture("cc", Locale.GERMANY));

            String eShort = PxPicConverter.toPicture("EEE", Locale.GERMANY);
            assertNotNull(eShort);
            assertTrue(eShort.startsWith("{") && eShort.endsWith("}"));

            String eFull = PxPicConverter.toPicture("EEEE", Locale.GERMANY);
            assertNotNull(eFull);
            assertTrue(eFull.startsWith("{") && eFull.endsWith("}"));

            String eNarrow = PxPicConverter.toPicture("EEEEE", Locale.GERMANY);
            assertNotNull(eNarrow);

            String eee = PxPicConverter.toPicture("eee", Locale.GERMANY);
            assertNotNull(eee);
            String eeee = PxPicConverter.toPicture("eeee", Locale.GERMANY);
            assertNotNull(eeee);
            String eeeee = PxPicConverter.toPicture("eeeee", Locale.GERMANY);
            assertNotNull(eeeee);
        }

        @Test
        void shouldConvertQuarters() {
            assertEquals("#[#]", PxPicConverter.toPicture("Q", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toPicture("QQ", Locale.GERMANY));
            assertEquals("#[#]", PxPicConverter.toPicture("q", Locale.GERMANY));
            assertEquals("##", PxPicConverter.toPicture("qq", Locale.GERMANY));

            String q3 = PxPicConverter.toPicture("QQQ", Locale.GERMANY);
            assertNotNull(q3);
            String q4 = PxPicConverter.toPicture("QQQQ", Locale.GERMANY);
            assertNotNull(q4);
            String q5 = PxPicConverter.toPicture("QQQQQ", Locale.GERMANY);
            assertNotNull(q5);
            String qLower3 = PxPicConverter.toPicture("qqq", Locale.GERMANY);
            assertNotNull(qLower3);
            String qLower4 = PxPicConverter.toPicture("qqqq", Locale.GERMANY);
            assertNotNull(qLower4);
            String qLower5 = PxPicConverter.toPicture("qqqqq", Locale.GERMANY);
            assertNotNull(qLower5);
        }

        @Test
        void shouldConvertEra() {
            String g1 = PxPicConverter.toPicture("G", Locale.US);
            assertEquals("{AD,BC}", g1);

            String g4 = PxPicConverter.toPicture("GGGG", Locale.US);
            assertEquals("{Anno Domini,Before Christ}", g4);

            String g5 = PxPicConverter.toPicture("GGGGG", Locale.US);
            assertEquals("{A,B}", g5);
        }

        @Test
        void shouldConvertMonthNamesAndFactorPrefixes() {
            String mmm = PxPicConverter.toPicture("MMM", Locale.GERMANY);
            assertNotNull(mmm);
            assertTrue(mmm.startsWith("{") && mmm.endsWith("}"));

            String mmmm = PxPicConverter.toPicture("MMMM", Locale.GERMANY);
            assertNotNull(mmmm);
            assertTrue(mmmm.startsWith("{") && mmmm.endsWith("}"));

            String mmmmm = PxPicConverter.toPicture("MMMMM", Locale.GERMANY);
            assertNotNull(mmmmm);

            String lll = PxPicConverter.toPicture("LLL", Locale.GERMANY);
            assertEquals(mmm, lll);
            String llll = PxPicConverter.toPicture("LLLL", Locale.GERMANY);
            assertEquals(mmmm, llll);
            String lllll = PxPicConverter.toPicture("LLLLL", Locale.GERMANY);
            assertEquals(mmmmm, lllll);
        }

        @Test
        void shouldThrowExceptionOnInvalidMonthWidth() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toPicture("MMMMMM", Locale.GERMANY)
            );
            assertTrue(ex.getMessage().contains("Invalid month field width"));
        }

        @Test
        void shouldHandleQuotedLiteralsAndEscapeApostrophes() {
            assertEquals("T", PxPicConverter.toPicture("'T'", Locale.US));
            assertEquals("o'clock", PxPicConverter.toPicture("'o''clock'", Locale.US));
            assertEquals("yyyy", PxPicConverter.toPicture("'yyyy'", Locale.US));
            assertEquals(";#;?;@;&;!;;;*;[;];;;{;};,", PxPicConverter.toPicture("'#?@&!;*[];{},'", Locale.US));
        }

        @Test
        void shouldThrowExceptionOnUnterminatedQuotedLiteral() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toPicture("'unclosed", Locale.US)
            );
            assertTrue(ex.getMessage().contains("Unterminated quoted literal in pattern"));
        }

        @Test
        void shouldEscapeParadoxSpecialCharactersInOrdinaryLiterals() {
            assertEquals("####/##/##", PxPicConverter.toPicture("yyyy/MM/dd", Locale.US));
            assertEquals("####-##-##", PxPicConverter.toPicture("yyyy-MM-dd", Locale.US));
            assertEquals("####.##.##", PxPicConverter.toPicture("yyyy.MM.dd", Locale.US));
            assertEquals("####;###", PxPicConverter.toPicture("yyyy#dd", Locale.US));
            assertEquals("####;[##;]", PxPicConverter.toPicture("yyyy[dd]", Locale.US));
            assertEquals("####;{##;,##;}", PxPicConverter.toPicture("yyyy{MM,dd}", Locale.US));
            assertEquals("####;;##", PxPicConverter.toPicture("yyyy;dd", Locale.US));
            assertEquals("####;?##", PxPicConverter.toPicture("yyyy?dd", Locale.US));
            assertEquals("####;@##", PxPicConverter.toPicture("yyyy@dd", Locale.US));
            assertEquals("####;&##", PxPicConverter.toPicture("yyyy&dd", Locale.US));
            assertEquals("####;!##", PxPicConverter.toPicture("yyyy!dd", Locale.US));
            assertEquals("####;*##", PxPicConverter.toPicture("yyyy*dd", Locale.US));
        }

        @ParameterizedTest
        @ValueSource(chars = {'V', 'z', 'O', 'X', 'x', 'Z'})
        void shouldThrowExceptionOnTimezoneFields(char timezoneChar) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toPicture("HH:mm:ss " + timezoneChar, Locale.US)
            );
            assertTrue(ex.getMessage().contains("Cannot convert timezone field"));
        }

        @Test
        void shouldThrowExceptionOnPaddingModifier() {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toPicture("ppHH", Locale.US)
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
                    () -> PxPicConverter.toPicture(null)
            );
        }

        @Test
        void shouldConvertUsingDefaultLocale() {
            String resultWithDefault = PxPicConverter.toPicture("yyyy-MM-dd");
            String resultExplicit = PxPicConverter.toPicture("yyyy-MM-dd", Locale.getDefault());
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
                    () -> PxPicConverter.toDatePicture(null, Locale.GERMANY)
            );
            assertEquals("style must not be null", ex.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenLocaleIsNull() {
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toDatePicture(FormatStyle.SHORT, null)
            );
            assertEquals("locale must not be null", ex.getMessage());
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"SHORT", "MEDIUM"})
        void shouldConvertValidStyles(FormatStyle style) {
            String germanDatePic = PxPicConverter.toDatePicture(style, Locale.GERMANY);
            assertNotNull(germanDatePic);
            assertFalse(germanDatePic.isEmpty());
            assertTrue(new PxPicValidator().checkSyntax(germanDatePic));

            String usDatePic = PxPicConverter.toDatePicture(style, Locale.US);
            assertNotNull(usDatePic);
            assertFalse(usDatePic.isEmpty());
            assertTrue(new PxPicValidator().checkSyntax(usDatePic));
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"LONG", "FULL"})
        void shouldThrowExceptionOnUnsupportedStyles(FormatStyle style) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toDatePicture(style, Locale.GERMANY)
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
                    () -> PxPicConverter.toDatePicture(null)
            );
            assertEquals("style must not be null", ex.getMessage());
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"SHORT", "MEDIUM"})
        void shouldConvertUsingDefaultLocale(FormatStyle style) {
            String resultDefault = PxPicConverter.toDatePicture(style);
            String resultExplicit = PxPicConverter.toDatePicture(style, Locale.getDefault());
            assertEquals(resultExplicit, resultDefault);
            assertTrue(new PxPicValidator().checkSyntax(resultDefault));
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"LONG", "FULL"})
        void shouldThrowExceptionOnUnsupportedStyles(FormatStyle style) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toDatePicture(style)
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
                    () -> PxPicConverter.toTimePicture(null, Locale.GERMANY)
            );
            assertEquals("style must not be null", ex.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenLocaleIsNull() {
            NullPointerException ex = assertThrows(
                    NullPointerException.class,
                    () -> PxPicConverter.toTimePicture(FormatStyle.SHORT, null)
            );
            assertEquals("locale must not be null", ex.getMessage());
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"SHORT", "MEDIUM"})
        void shouldConvertValidStyles(FormatStyle style) {
            String germanTimePic = PxPicConverter.toTimePicture(style, Locale.GERMANY);
            assertNotNull(germanTimePic);
            assertFalse(germanTimePic.isEmpty());
            assertTrue(new PxPicValidator().checkSyntax(germanTimePic));

            String usTimePic = PxPicConverter.toTimePicture(style, Locale.US);
            assertNotNull(usTimePic);
            assertFalse(usTimePic.isEmpty());
            assertTrue(new PxPicValidator().checkSyntax(usTimePic));
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"LONG", "FULL"})
        void shouldThrowExceptionOnUnsupportedStyles(FormatStyle style) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toTimePicture(style, Locale.GERMANY)
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
                    () -> PxPicConverter.toTimePicture(null)
            );
            assertEquals("style must not be null", ex.getMessage());
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"SHORT", "MEDIUM"})
        void shouldConvertUsingDefaultLocale(FormatStyle style) {
            String resultDefault = PxPicConverter.toTimePicture(style);
            String resultExplicit = PxPicConverter.toTimePicture(style, Locale.getDefault());
            assertEquals(resultExplicit, resultDefault);
            assertTrue(new PxPicValidator().checkSyntax(resultDefault));
        }

        @ParameterizedTest
        @EnumSource(value = FormatStyle.class, names = {"LONG", "FULL"})
        void shouldThrowExceptionOnUnsupportedStyles(FormatStyle style) {
            IllegalArgumentException ex = assertThrows(
                    IllegalArgumentException.class,
                    () -> PxPicConverter.toTimePicture(style)
            );
            assertEquals("style must be FormatStyle.SHORT or FormatStyle.MEDIUM", ex.getMessage());
        }
    }

    @Nested
    @DisplayName("Comprehensive Pattern and Validator integration tests")
    class ValidatorIntegrationTests {

        @Test
        void shouldValidateLocalizedGermanShortDate() {
            String pic = PxPicConverter.toDatePicture(FormatStyle.SHORT, Locale.GERMANY);
            var validator = new PxPicValidator(pic);

            assertTrue(validator.isValidInput(new StringBuilder("01.01.24"), false));
            assertDoesNotThrow(() -> validator.validate("01.01.24"));
        }

        @Test
        void shouldValidateLocalizedGermanMediumDate() {
            String pic = PxPicConverter.toDatePicture(FormatStyle.MEDIUM, Locale.GERMANY);
            var validator = new PxPicValidator(pic);

            assertTrue(validator.isValidInput(new StringBuilder("01.01.2024"), false));
            assertDoesNotThrow(() -> validator.validate("01.01.2024"));
        }

        @Test
        void shouldValidateLocalizedGermanShortTime() {
            String pic = PxPicConverter.toTimePicture(FormatStyle.SHORT, Locale.GERMANY);
            var validator = new PxPicValidator(pic);

            assertTrue(validator.isValidInput(new StringBuilder("14:30"), false));
            assertDoesNotThrow(() -> validator.validate("14:30"));
        }

        @Test
        void shouldValidateLocalizedGermanMediumTime() {
            String pic = PxPicConverter.toTimePicture(FormatStyle.MEDIUM, Locale.GERMANY);
            var validator = new PxPicValidator(pic);

            assertTrue(validator.isValidInput(new StringBuilder("14:30:45"), false));
            assertDoesNotThrow(() -> validator.validate("14:30:45"));
        }
    }
}
