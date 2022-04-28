public class Main {

    public static void main(String[] args){

        NoteParser noteParser = new NoteParser();

        noteParser.parseNote("glob is I");
        noteParser.parseNote("prok is V");
        noteParser.parseNote("pish is X");
        noteParser.parseNote("tegj is L");

        noteParser.parseNote("glob glob Silver is 34 Credits");
        noteParser.parseNote("glob prok Gold is 57800 Credits");
        noteParser.parseNote("pish pish Iron is 3910 Credits");

        noteParser.parseNote("how much is pish tegj glob glob ?");

        noteParser.parseNote("how many Credits is glob prok Silver ?");
        noteParser.parseNote("how many Credits is glob prok Gold ?");
        noteParser.parseNote("how many Credits is glob prok Iron ?");

        noteParser.parseNote("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?");
    }
}