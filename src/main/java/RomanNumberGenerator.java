import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A static class for translasting roman numerals to integer and vice versa.
 * The rules of formatting and calculating roman numerals are embedded in various hash maps.
 */
public class RomanNumberGenerator {

    private static Logger LOG = LogManager.getLogger("RomanNumberGenerator");

    private final static HashMap <Character, Integer> VALUE_MAP = new HashMap <Character, Integer> (){
        {
            put('I',1);
            put ('V', 5);
            put ('X', 10);
            put ('L', 50);
            put ('C', 100);
            put ('D', 500);
            put ('M', 1000);
        }
    };
    private final static HashMap <Character, ArrayList<Character>> SUBSTITUTABLE_MAP = new HashMap <Character, ArrayList<Character>> (){
        {
            put('I', new ArrayList<Character>(){
                {
                    add('V');
                    add('X');
                }
            });

            put('V', new ArrayList<Character>());

            put('X', new ArrayList<Character>(){
                {
                    add('L');
                    add('C');
                }
            });

            put('L', new ArrayList<Character>());

            put('C', new ArrayList<Character>(){
                {
                    add('D');
                    add('M');
                }
            });

            put('D', new ArrayList<Character>());

            put('M', new ArrayList<Character>());
        }
    };
    private final static HashMap <Character, Integer> MAX_REPETITIONS = new HashMap<Character, Integer>(){
        {
            put('I', 3);
            put('X', 3);
            put('C', 3);
            put('M', 3);
            put('D', 1);
            put('L', 1);
            put('V', 1);
        }
    };

    /**
     * Static method for converting integer to roman numerals.
     * Input is expected to be between 0 and 3999.
     *
     * @param input
     * @return
     */
    public static String convertIntegerToRoman (int input){

        if (input < 0 || input > 3999){
            throw new IllegalArgumentException("Invalid input '" + input + "' for roman numeral");
        }

        String romanString = "";

        int thousands = input / 1000;

        switch (thousands){
            case 1:
                romanString = romanString.concat("M");
                break;
            case 2:
                romanString = romanString.concat("MM");
                break;
            case 3:
                romanString = romanString.concat("MMM");
                break;
        }

        int hundreds = (input % 1000) / 100 ;

        switch (hundreds){
            case 1:
                romanString = romanString.concat("C");
                break;
            case 2:
                romanString = romanString.concat("CC");
                break;
            case 3:
                romanString = romanString.concat("CCC");
                break;
            case 4:
                romanString = romanString.concat("CD");
                break;
            case 5:
                romanString = romanString.concat("D");
                break;
            case 6:
                romanString = romanString.concat("DC");
                break;
            case 7:
                romanString = romanString.concat("DCC");
                break;
            case 8:
                romanString = romanString.concat("DCCC");
                break;
            case 9:
                romanString = romanString.concat("CM");
                break;
        }

        int tens = ((input % 1000) % 100) / 10;

        switch (tens){
            case 1:
                romanString = romanString.concat("X");
                break;
            case 2:
                romanString = romanString.concat("XX");
                break;
            case 3:
                romanString = romanString.concat("XXX");
                break;
            case 4:
                romanString = romanString.concat("XL");
                break;
            case 5:
                romanString = romanString.concat("L");
                break;
            case 6:
                romanString = romanString.concat("LX");
                break;
            case 7:
                romanString = romanString.concat("LXX");
                break;
            case 8:
                romanString = romanString.concat("LXXX");
                break;
            case 9:
                romanString = romanString.concat("XC");
                break;
        }

        int single = ((input % 1000) % 100) % 10;

        switch (single){
            case 1:
                romanString = romanString.concat("I");
                break;
            case 2:
                romanString = romanString.concat("II");
                break;
            case 3:
                romanString = romanString.concat("III");
                break;
            case 4:
                romanString = romanString.concat("IV");
                break;
            case 5:
                romanString = romanString.concat("V");
                break;
            case 6:
                romanString = romanString.concat("VI");
                break;
            case 7:
                romanString = romanString.concat("VII");
                break;
            case 8:
                romanString = romanString.concat("VIII");
                break;
            case 9:
                romanString = romanString.concat("IX");
                break;
        }

        return romanString;
    }

    /**
     * Static method for converting roman numerals to integer.
     * The rules of formatting roman numerals are embedded in the static hash maps of this class.
     * If those rules are not followed a IllegalArgumentException is thrown.
     *
     * @param romanString
     * @return
     */
    public static int convertRomanToInteger(String romanString) {

        String pattern = "[IVXLCDM]*$";

        if (!romanString.matches(pattern)){
            throw new IllegalArgumentException("Invalid input '" + romanString + "' for roman numeral");
        }

        int romanInteger = 0;

        ArrayList <String> romanDissected = dissectRomanNumber(romanString);
        romanInteger = calculateRomanNumber(romanDissected);

        return romanInteger;
    }

    /**
     * The dissected roman numerals will be summed up in this method.
     *
     * @param dissectedRomanNumerals
     * @return
     */
    private static int calculateRomanNumber (ArrayList <String> dissectedRomanNumerals){

        int romanInteger = 0;

        for (String string : dissectedRomanNumerals){

            if (string.length() == 1){
                romanInteger += VALUE_MAP.get(string.charAt(0));
            }

            else {
                if (string.matches("^[" + string.charAt(0) + "]+$")){
                    romanInteger += VALUE_MAP.get(string.charAt(0)) * string.length();
                }
                else {
                    //Assuming only strings of length 2 can be left
                    romanInteger += VALUE_MAP.get(string.charAt(1)) - VALUE_MAP.get(string.charAt(0));
                }
            }
        }


        return romanInteger;
    }

    /**
     * For further conversion of a roman numeral to an integer the roman numerals needs to be first dissected into logically
     * correct sections. Also the rules of formatting a roman numeral are checked in this method.
     *
     * @param romanString
     * @return
     */
    private static ArrayList<String> dissectRomanNumber (String romanString) {

        ArrayList <String> romanDissected = new ArrayList <String> ();
        String romanSubString = "";

        for (int i = 0; i < romanString.length(); i++){

            // If romanSubString is empty every roman numeral can be put in
            if (romanSubString.isEmpty()){
                romanSubString = romanSubString.concat(String.valueOf(romanString.charAt(i)));
            }
            else {
                // If the next char of the original roman string is equal to the already existing roman numeral in the substring,
                // we need to check if this specific roman numeral is allowed to have repetitions or the number of repetitions has been surpassed
                if (romanSubString.charAt(0) == romanString.charAt(i)){
                    if (MAX_REPETITIONS.get(romanSubString.charAt(0)) <= romanSubString.length()){
                        throw new IllegalArgumentException ("Incorrect Syntax. " + romanSubString.charAt(0) + " can't be repeated more than " + MAX_REPETITIONS.get(romanSubString.charAt(0)) + " times");
                    }

                    romanSubString = romanSubString.concat(String.valueOf(romanString.charAt(i)));

                }

                // If the next char of the original roman string is not equal to the already existing roman numeral in the substring,
                // we need to check if the already existing roman numeral in the substring is allowed to be subtracted from the new char of the original substring
                else if (romanSubString.charAt(0) != romanString.charAt(i)){
                    if (VALUE_MAP.get(romanSubString.charAt(0)) < VALUE_MAP.get(romanString.charAt(i))){
                        if (!SUBSTITUTABLE_MAP.get(romanSubString.charAt(0)).contains(romanString.charAt(i))){
                            throw new IllegalArgumentException ("Incorrect Syntax. " + romanSubString.charAt(0) + " can't be subtracted from " + romanString.charAt(i));
                        }
                        romanSubString = romanSubString.concat(String.valueOf(romanString.charAt(i)));
                    }
                    else {
                        romanDissected.add(romanSubString);
                        romanSubString = "";
                        romanSubString = romanSubString.concat(String.valueOf(romanString.charAt(i)));
                    }
                }
            }
        }

        // Leftovers
        if (!romanSubString.isEmpty()){
            romanDissected.add(romanSubString);
        }

        return romanDissected;
    }
};