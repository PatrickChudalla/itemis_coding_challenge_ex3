import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RomanNumerGeneratorTest {

    @Test
    /**
     * Tests valid inputs for integer to roman numerals conversion from 0 to 3999.
     * To also test the roman numeral to integer conversion the results are converted back and forth
     */
    public void testRomanNumberGenerator (){

        for (int i = 0; i < 3999; i++){
            String roman = null;
            roman = RomanNumberGenerator.convertIntegerToRoman(i);
            int integer = RomanNumberGenerator.convertRomanToInteger(roman);

            Assertions.assertEquals(i, integer);
        }
    }

    @Test
    /**
     * Tests invalid inputs for integer to roman numeral conversion.
     * Expected is a IllegalArgumentException
     */
    public void testRomanNumberGeneratorOutOfBoundary (){

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertIntegerToRoman(-1);
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertIntegerToRoman(4000);
        });

    }

    @Test
    /**
     * Tests invalid Inputs for roman numeral to integer conversion according to the mentioned rules
     * of how roman numeral must be formatted and ordered
     */
    public void testInvalidInputs (){

        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("BLABLA");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("I I");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("IIII");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("XXXX");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("CCCC");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("MMMM");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("DD");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("LL");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("VV");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("IL");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("IC");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("ID");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("IM");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("XD");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("XM");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("VX");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("VL");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("VC");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("VD");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("VM");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("LC");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("LD");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("LM");
        });

        exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            RomanNumberGenerator.convertRomanToInteger("DM");
        });
    }
}
