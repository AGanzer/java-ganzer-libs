package de.ganzer.core.validation;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PxPicValidatorTest {
    private record DataPair(String input, String expected) {
    }

    @Test
    void constructEmpty() {
        var val = new PxPicValidator();

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals("", val.getPicture());
    }

    @Test
    void constructWithOption() {
        var val = new PxPicValidator(ValidatorOptions.AUTO_FILL);

        assertEquals(ValidatorOptions.AUTO_FILL, val.getOptions());
        assertEquals("", val.getPicture());
    }

    @Test
    void constructWithPic() {
        var pic = "*#";
        var val = new PxPicValidator(pic);

        assertEquals(ValidatorOptions.AUTO_FILL | ValidatorOptions.NEEDS_INPUT, val.getOptions());
        assertEquals(pic, val.getPicture());
    }

    @Test
    void constructWithPicInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new PxPicValidator("[*#"));
    }

    @Test
    void constructWithPicAndOption() {
        var pic = "*#";
        var val = new PxPicValidator(ValidatorOptions.AUTO_FILL, pic);

        assertEquals(ValidatorOptions.AUTO_FILL, val.getOptions());
        assertEquals(pic, val.getPicture());
    }

    @Test
    void constructWithPicInvalidAndOptions() {
        assertThrows(IllegalArgumentException.class, () -> new PxPicValidator(ValidatorOptions.AUTO_FILL, "[*#"));
    }

    @Test
    void setPicture() {
        var pic = "*#";
        var val = new PxPicValidator();

        val.setPicture(pic);
        assertEquals(pic, val.getPicture());
    }

    @Test
    void setPictureInvalid() {
        var val = new PxPicValidator();
        assertThrows(IllegalArgumentException.class, () -> val.setPicture("[*#"));
    }

    @Test
    void checkSyntax() {
        var val = new PxPicValidator();

        assertTrue(val.checkSyntax(""));
        assertTrue(val.checkSyntax("{White,Gr{ay,een},B{l{ack,ue},rown},Red}"));
        assertTrue(val.checkSyntax("##/##/##[##]"));
        assertTrue(val.checkSyntax("&*?"));
        assertTrue(val.checkSyntax("[(*3#*2[#]) ]*3#-*4#"));
        assertTrue(val.checkSyntax("&*?;*"));
        assertTrue(val.checkSyntax("&*?;;"));
        assertFalse(val.checkSyntax("&*?;"));
        assertFalse(val.checkSyntax("&*?*"));
        assertFalse(val.checkSyntax("*"));
        assertFalse(val.checkSyntax(";"));
        assertFalse(val.checkSyntax("[*#"));
        assertFalse(val.checkSyntax("{*#"));
    }

    @Test
    void doInputValidationColorsAutoFill() {
        var val = new PxPicValidator("{White,Gr{ay,een},B{l{ack,ue},rown},Red}");

        List<DataPair> validData = Arrays.asList(
                new DataPair("W", "White"),
                new DataPair("w", "White"),
                new DataPair("Gr", "Gr"),
                new DataPair("gr", "Gr"),
                new DataPair("b", "B"),
                new DataPair("Bl", "Bl"),
                new DataPair("Blu", "Blue"),
                new DataPair("Br", "Brown"),
                new DataPair("r", "Red"));
        List<DataPair> invalidData = Arrays.asList(
                new DataPair("Grr", "Grr"),
                new DataPair("l", "l"),
                new DataPair("Bli", "Bli"),
                new DataPair("Gray1", "Gray1"));

        for (DataPair d: validData) {
            var input = new StringBuilder(d.input());
            var result = val.isValidInput(input, true);

            assertEquals(d.expected(), input.toString());
            assertTrue(result);
        }

        for (DataPair d: invalidData) {
            var input = new StringBuilder(d.input());
            var result = val.isValidInput(input, true);

            assertEquals(d.expected(), input.toString());
            assertFalse(result);
        }
    }

    @Test
    void doInputValidationColorsNoAutoFill() {
        var val = new PxPicValidator(ValidatorOptions.NONE, "{White,Gr{ay,een},B{l{ack,ue},rown},Red}");

        List<DataPair> validData = Arrays.asList(
                new DataPair("W", "W"),
                new DataPair("w", "W"),
                new DataPair("Gr", "Gr"),
                new DataPair("gr", "Gr"),
                new DataPair("b", "B"),
                new DataPair("Bl", "Bl"),
                new DataPair("Blu", "Blu"),
                new DataPair("Br", "Br"),
                new DataPair("r", "R"));

        for (DataPair d: validData) {
            var input = new StringBuilder(d.input());
            var result = val.isValidInput(input, true);

            assertEquals(d.expected(), input.toString());
            assertTrue(result);
        }
    }

    @Test
    void doInputValidationDateAutoFill() {
        var val = new PxPicValidator("##/##/##[##]");

        List<DataPair> validData = Arrays.asList(
                new DataPair("1", "1"),
                new DataPair("12", "12/"),
                new DataPair("12/12", "12/12/"),
                new DataPair("12/12/12", "12/12/12"),
                new DataPair("12/12/121", "12/12/121"),
                new DataPair("12/12/1212", "12/12/1212"));
        List<DataPair> invalidData = Arrays.asList(
                new DataPair("A", "A"),
                new DataPair("1a", "1a"),
                new DataPair("12/12/12121", "12/12/12121"));


        for (DataPair d: validData) {
            var input = new StringBuilder(d.input());
            var result = val.isValidInput(input, true);

            assertEquals(d.expected(), input.toString());
            assertTrue(result);
        }

        for (DataPair d: invalidData) {
            var input = new StringBuilder(d.input());
            var result = val.isValidInput(input, true);

            assertEquals(d.expected(), input.toString());
            assertFalse(result);
        }
    }

    @Test
    void doInputValidationDateNoAutoFill() {
        var val = new PxPicValidator(ValidatorOptions.NONE, "##/##/##[##]");

        List<DataPair> validData = Arrays.asList(
                new DataPair("1", "1"),
                new DataPair("12", "12"),
                new DataPair("12/", "12/"));

        for (DataPair d: validData) {
            var input = new StringBuilder(d.input());
            var result = val.isValidInput(input, true);

            assertEquals(d.expected(), input.toString());
            assertTrue(result);
        }
    }

    @Test
    void doValidateColors() {
        var val = new PxPicValidator("{White,Gr{ay,een},B{l{ack,ue},rown},Red}");

        List<String> validInput = Arrays.asList(
                "White",
                "Gray",
                "Green",
                "Blue",
                "Black",
                "Brown",
                "Red");
        List<String> invalidInput = Arrays.asList(
                "Bl",
                "Gr",
                "B");

        for (String input: validInput) {
            assertDoesNotThrow(() -> val.validate(input));
        }

        for (String input: invalidInput) {
            assertThrows(ValidatorException.class, () -> val.validate(input));
        }
    }

    @Test
    void doValidateDate() {
        var val = new PxPicValidator("##/##/##[##]");

        List<String> validInput = Arrays.asList(
                "12/12/12",
                "12/12/1212");
        List<String> invalidInput = Arrays.asList(
                "12",
                "12/12/123");

        for (String input: validInput) {
            assertDoesNotThrow(() -> val.validate(input));
        }

        for (String input: invalidInput) {
            assertThrows(ValidatorException.class, () -> val.validate(input));
        }
    }
}