import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that parses notes consisting of translations from intergalactic numerals to arabic numerals, prices of resources and various queries.
 * The information of translations or prices are embedded in HashMaps.
 */
public class NoteParser {

    private Logger LOG = LogManager.getLogger(this.getClass());

    private HashMap<String, String> interToRoman = new HashMap<String, String>();
    private HashMap<String, String> romanToInter = new HashMap<String, String>();
    private HashMap<String, Double> resourcePrice = new HashMap <String, Double> ();

    /**
     * Main function that can be called from outside the class. According to the pattern of the note the note will be
     * passed to other private methods where the actual parsing is happening.
     *
     * @param note
     */
    public void parseNote(String note) {

        String patternInterToRomanTranslation ="^[a-zA-Z]*[ ](is)[ ][IVXLCDM]$";
        String patternResourcePrice = "^[a-z ]*[A-Z][a-z]*( is )[0-9]*( Credits)$";
        String patternHowMuchInIntergalactic = "^(how much is )[a-z]+[a-z ]*[?]$";
        String patternHowManyCredits = "(how many Credits is )[a-z]+[a-z ]*[A-Z]+[a-z]*[ ?]*$";

        if (note.matches(patternInterToRomanTranslation)){
            putInterToRomanTranslation (note);
        } else if (note.matches(patternResourcePrice)){
            putResourcePrice(note);
        } else if (note.matches(patternHowMuchInIntergalactic)){
            howMuchInIntergalactic(note);
        } else if (note.matches(patternHowManyCredits)){
            howManyCredits(note);
        } else {
            LOG.error ("I have no Idea what you are talking about");
        }
    }

    /**
     * Parses a translation from intergalactic numerals to roman numerals
     *
     * @param translationNote
     */
    private void putInterToRomanTranslation(String translationNote){

        String intergalactic = translationNote.substring(0, translationNote.indexOf(' '));
        String roman = translationNote.substring(translationNote.lastIndexOf(' ') + 1);

        if (interToRoman.containsKey(intergalactic)){
            LOG.warn("There is already a translation of " + intergalactic);
        }
        else {
            this.interToRoman.put(intergalactic, roman);
        }

        if (this.romanToInter.containsKey(roman)){
            LOG.warn("There is already a translation of " + roman);
        }
        else {
            this.romanToInter.put(roman,intergalactic);
        }
    }

    /**
     * Parses a price for a resource
     *
     * @param priceNote
     */
    private void putResourcePrice (String priceNote){

        String price = "";
        Pattern patternPrice = Pattern.compile("[ ].[0-9]*[ ]");
        Matcher matcher = patternPrice.matcher(priceNote);

        if (matcher.find()){
            price = matcher.group(0).trim();
        }

        String intergalacticUnits = "";
        Pattern patternIntergalacticUnits = Pattern.compile("^[a-z]*[ ][a-z]*");
        matcher = patternIntergalacticUnits.matcher(priceNote);

        if (matcher.find()){
            intergalacticUnits = matcher.group(0).trim();
        }

        String resource = "";
        Pattern patternResource = Pattern.compile("[A-Z][a-z]*[ ]");
        matcher = patternResource.matcher(priceNote);

        if (matcher.find()){
            resource = matcher.group(0).trim();
        }

        String[] intergalacticDigits = intergalacticUnits.split("[ ]");

        String romanDigits = "";

        for (int i = 0; i < intergalacticDigits.length; i++){
            if (!this.interToRoman.containsKey(intergalacticDigits[i])){
                LOG.error ("There is no translation for " + intergalacticDigits[i]);
                return;
            }
            romanDigits = romanDigits.concat(this.interToRoman.get(intergalacticDigits[i]));
        }

        int numberOfResources = RomanNumberGenerator.convertRomanToInteger(romanDigits);

        if (this.resourcePrice.containsKey(resource)){
            LOG.warn("There is already a price for " + resource);
            return;
        }

        this.resourcePrice.put(resource, Double.parseDouble(price)/numberOfResources);
    }

    /**
     * Translates intergalactic numerals to a regular integer
     *
     * @param howMuchRequest
     */
    private void howMuchInIntergalactic(String howMuchRequest){

        Pattern patternIntergalacticDigits = Pattern.compile("is(.*?)[?]");
        Matcher matcher = patternIntergalacticDigits.matcher(howMuchRequest);

        String intergalacticDigits = "";

        if (matcher.find()){
            intergalacticDigits = matcher.group(1).trim();
        }

        String [] intergalacticSplitDigits = intergalacticDigits.split("[ ]");
        String romanString = "";

        for (int i = 0; i < intergalacticSplitDigits.length; i++){
            if (!this.interToRoman.containsKey(intergalacticSplitDigits[i])){
                LOG.error ("There is no translation for " + intergalacticSplitDigits[i]);
                return;
            }
            romanString = romanString.concat(this.interToRoman.get(intergalacticSplitDigits[i]));
        }

        LOG.info (intergalacticDigits + " is " + RomanNumberGenerator.convertRomanToInteger(romanString));
    }

    /**
     * Calculates the amount of Credits for specified intergalactic numerals of a given resource
     *
     * @param howManyRequest
     */
    private void howManyCredits(String howManyRequest){

        Pattern patternIntergalacticDigits = Pattern.compile("(is)(.*)[A-Z]");
        Matcher matcher = patternIntergalacticDigits.matcher(howManyRequest);

        String intergalacticDigits = "";

        if (matcher.find()){
            intergalacticDigits = matcher.group(2).trim();
        }

        String [] intergalacticSplitDigits = intergalacticDigits.split("[ ]");
        String romanDigits = "";

        for (int i = 0; i < intergalacticSplitDigits.length; i++){
            if (!this.interToRoman.containsKey(intergalacticSplitDigits[i])){
                LOG.error ("There is no translation for " + intergalacticSplitDigits[i]);
                return;
            }
            romanDigits = romanDigits.concat(this.interToRoman.get(intergalacticSplitDigits[i]));
        }

        Pattern patternResource = Pattern.compile("([A-Z][a-z ]*)[?]$");
        matcher = patternResource.matcher(howManyRequest);

        String resource = "";

        if (matcher.find()){
            resource = matcher.group(1).trim();
        }

        if (!this.resourcePrice.containsKey(resource)){
            LOG.error ("The resource " + resource + " has not been registered yet");
            return;
        }

        double value = RomanNumberGenerator.convertRomanToInteger(romanDigits) * this.resourcePrice.get(resource);

        LOG.info (intergalacticDigits + " " + resource + " is " + value + " Credits");
    }
}
