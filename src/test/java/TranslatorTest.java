import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TranslatorTest {

    @Test
    /**
     * Tests the translation of intergalactic numerals to roman numerals.
     * Also the handling of badly formatted inputs is tested.
     */
    public void putInterToRomanTranslationTest(){

        NoteParser noteParser = new NoteParser();
        LogCaptor logCaptor = LogCaptor.forClass(NoteParser.class);

        // Correct Inputs

        String expectedMessage = "";

        noteParser.parseNote("glob is I");
        noteParser.parseNote("prok is V");
        noteParser.parseNote("pish is X");
        noteParser.parseNote("tegj is L");
        noteParser.parseNote("blob is C");
        noteParser.parseNote("druff is D");
        noteParser.parseNote("lul is M");

        Assertions.assertEquals(0, logCaptor.getWarnLogs().size());
        Assertions.assertEquals(0, logCaptor.getErrorLogs().size());

        // Doubled Inputs

        expectedMessage = "There is already a translation of glob";

        noteParser.parseNote("glob is I");

        Assertions.assertEquals(expectedMessage, logCaptor.getWarnLogs().get(0));

        expectedMessage = "There is already a translation of I";
        Assertions.assertEquals(expectedMessage, logCaptor.getWarnLogs().get(1));

        // Incorrect format

        expectedMessage = "I have no Idea what you are talking about";

        noteParser.parseNote("glob  is I");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(0));

        noteParser.parseNote("glob is  I");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(1));

        noteParser.parseNote("glob is");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(2));

        noteParser.parseNote("glob is ");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(3));

        noteParser.parseNote(" is ");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(4));
    }

    @Test
    /**
     * Tests the input of the price of a resource.
     * Also the handling of badly formatted inputs is tested.
     * Also the input of a resource price is tested when no translation for the intergalactic numerals is present
     */
    public void putResourcePriceTest (){

        NoteParser noteParser = new NoteParser();
        LogCaptor logCaptor = LogCaptor.forClass(NoteParser.class);

        // Input before translation

        String expectedMessage = "There is no translation for glob";
        noteParser.parseNote("glob glob Silver is 34 Credits");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(0));

        // Correct Input

        noteParser.parseNote("glob is I");
        noteParser.parseNote("prok is V");
        noteParser.parseNote("pish is X");
        noteParser.parseNote("tegj is L");
        noteParser.parseNote("blob is C");
        noteParser.parseNote("druff is D");
        noteParser.parseNote("lul is M");

        noteParser.parseNote("glob glob Silver is 34 Credits");
        noteParser.parseNote("glob prok Gold is 57800 Credits");
        noteParser.parseNote("pish pish Iron is 3910 Credits");
        noteParser.parseNote("blob tegj glob Sulfer is 690 Credits");
        noteParser.parseNote("lul druff prok Aluminum is 690 Credits");

        Assertions.assertEquals(0, logCaptor.getWarnLogs().size());
        Assertions.assertEquals(1, logCaptor.getErrorLogs().size());

        // Incorrect Inputs

        expectedMessage = "I have no Idea what you are talking about";

        noteParser.parseNote("glob glob Copper 34 Credits");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(1));

        noteParser.parseNote("glob glob is 34 Credits");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(2));

        noteParser.parseNote("glob glob is Credits");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(3));

        noteParser.parseNote("glob glob is");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(4));

        noteParser.parseNote("is Copper 34 Credits");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(5));

        // Doubled Inputs

        expectedMessage = "There is already a price for Silver";

        noteParser.parseNote ("prok Silver is 36 Credits");
        Assertions.assertEquals(expectedMessage, logCaptor.getWarnLogs().get(0));
    }

    @Test
    /**
     * Tests the query for the price of a resource.
     * Also the handling of badly formatted inputs is tested.
     * Also the behaviour is tested for when no price exists for the given resource or
     * no translation for the intergalactic numerals exists.
     */
    public void howManyCreditsTest (){

        NoteParser noteParser = new NoteParser();
        LogCaptor logCaptor = LogCaptor.forClass(NoteParser.class);

        // Input before translation and price

        String expectedMessage = "There is no translation for glob";
        noteParser.parseNote("how many Credits is glob prok Silver ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(0));

        expectedMessage = "The resource Silver has not been registered yet";
        noteParser.parseNote("glob is I");
        noteParser.parseNote("prok is V");

        noteParser.parseNote ("how many Credits is glob prok Silver ?");

        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(1));

        // Correct Input

        noteParser = new NoteParser ();

        noteParser.parseNote("glob is I");
        noteParser.parseNote("prok is V");
        noteParser.parseNote("pish is X");
        noteParser.parseNote("tegj is L");
        noteParser.parseNote("blob is C");
        noteParser.parseNote("druff is D");
        noteParser.parseNote("lul is M");

        noteParser.parseNote("glob glob Silver is 20 Credits");
        noteParser.parseNote("pish Gold is 10000 Credits");
        noteParser.parseNote("prok Iron is 10 Credits");
        noteParser.parseNote("blob tegj glob Sulfer is 690 Credits");
        noteParser.parseNote("lul druff prok Aluminum is 690 Credits");

        expectedMessage = "glob Silver is 10.0 Credits";
        noteParser.parseNote("how many Credits is glob Silver ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getInfoLogs().get(0));

        expectedMessage = "glob Gold is 1000.0 Credits";
        noteParser.parseNote("how many Credits is glob Gold ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getInfoLogs().get(1));

        expectedMessage = "glob Iron is 2.0 Credits";
        noteParser.parseNote("how many Credits is glob Iron ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getInfoLogs().get(2));

        expectedMessage = "glob Sulfer is 4.6 Credits";
        noteParser.parseNote("how many Credits is glob Sulfer ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getInfoLogs().get(3));

        expectedMessage = "glob Aluminum is 0.46 Credits";
        noteParser.parseNote("how many Credits is glob Aluminum ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getInfoLogs().get(4));

        // Incorrect Input

        noteParser = new NoteParser();

        noteParser.parseNote("glob is I");
        noteParser.parseNote("prok is V");
        noteParser.parseNote("pish is X");
        noteParser.parseNote("tegj is L");
        noteParser.parseNote("blob is C");
        noteParser.parseNote("druff is D");
        noteParser.parseNote("lul is M");

        noteParser.parseNote("glob glob Silver is 20 Credits");
        noteParser.parseNote("pish Gold is 10000 Credits");
        noteParser.parseNote("prok Iron is 10 Credits");
        noteParser.parseNote("blob tegj glob Sulfer is 690 Credits");
        noteParser.parseNote("lul druff prok Aluminum is 690 Credits");

        expectedMessage = "I have no Idea what you are talking about";
        noteParser.parseNote("how many Credits glob Silver ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(2));

        noteParser.parseNote("how many is glob Silver ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(3));

        noteParser.parseNote("how Credits is glob Silver ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(4));

        noteParser.parseNote("many Credits is glob Silver ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(5));

        noteParser.parseNote("how many Credits is Silver ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(6));

        noteParser.parseNote("how many Credits is glob ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(7));
    }

    @Test
    /**
     * Tests the query for the translation of intergalactic numerals to integer.
     * Also tests badly formatted inputs.
     * Also the behaviour is tested when no translation of the intergalactic numerals exists
     */
    public void howMuchInIntergalactic(){

        NoteParser noteParser = new NoteParser();
        LogCaptor logCaptor = LogCaptor.forClass(NoteParser.class);

        // Input before translation

        String expectedMessage = "There is no translation for pish";
        noteParser.parseNote("how much is pish tegj glob glob ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(0));

        // Correct Input

        noteParser.parseNote("glob is I");
        noteParser.parseNote("prok is V");
        noteParser.parseNote("pish is X");
        noteParser.parseNote("tegj is L");

        expectedMessage = "pish tegj glob glob is 42";
        noteParser.parseNote("how much is pish tegj glob glob ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getInfoLogs().get(0));

        // Incorrect Input

        expectedMessage = "I have no Idea what you are talking about";
        noteParser.parseNote("how much pish tegj glob glob ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(1));

        noteParser.parseNote("how much is?");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(2));

        noteParser.parseNote("is pish tegj glob glob ?");
        Assertions.assertEquals(expectedMessage, logCaptor.getErrorLogs().get(3));
    }
}
