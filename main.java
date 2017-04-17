import java.util.ArrayList;
import java.util.List;

import model.TextToSpeech;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import marytts.modules.synthesis.Voice;

public class main {

	public static void main(String[] args) {
		
		/*This function reads in a text file and turns it into a large string */
		String everything = "";
		List<String> lines = null; //holds all of the lines
		Path file = Paths.get("/Users/jledinh/Documents/workspace2/Diction_n_Fiction/src/file.txt");
		try { //reads in all of the lines
			lines = Files.readAllLines(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace(); //throws an exception if nothing there
		}		
		for (String s: lines) {
			everything = everything + s; //adds all the lines together
		}
		
		//This function retrieves the fields required for the story
		ArrayList<String> fieldlist = new ArrayList<String>(); //holds the fields in order
		int i = 0;
		while (i < everything.length()) { //goes through the story
			if (everything.substring(i, i+1).equals("(")) { //finds the beginning ( to indicate a field
				String fieldtype =""; //holds the field type
				int j = i+1; //moves string to after (
				while (!everything.substring(j,j+1).equals(")")) { //creates the field string up to the ending )
					fieldtype = fieldtype + everything.substring(j, j+1); //adds characters to fieldtype
					StringBuilder sb = new StringBuilder(everything);
					sb.deleteCharAt(j);
					everything = sb.toString();
				}
				fieldlist.add(fieldtype); //adds field to the arraylist
			}
			i += 1;
		}
		
		//This function replaces the empty fields with inputs given
		ArrayList<String> inputs = new ArrayList<String>();
		
		inputs.add("cool");
		inputs.add("Anna");
		inputs.add("ice cream");
		inputs.add("gerbil");
		inputs.add("5");
		inputs.add("tourists");
		/*inputs.add("7");
		inputs.add("Justin's Minivan");
		inputs.add("kettle");
		inputs.add("fupa");
		inputs.add("Cold water with honey and tumeric");*/
		
		if (inputs.size() != fieldlist.size()) {
			RuntimeException r = new RuntimeException();
			throw r;
		}
		for (String s: inputs) {
			int j = 0;
			StringBuilder sb = new StringBuilder(everything);
			while (j < everything.length()) {
				if (sb.substring(j,j+1).equals("(")) {
					sb.replace(j, j+2, s);
					break;
				}
				j = j + 1;
			}
			everything = sb.toString();
		}
		
		//Prints out everything
		/*for (String s: fieldlist) {
			System.out.println(s);
		}*/
		System.out.println(everything);
		
		TextToSpeech tts = new TextToSpeech();
		// Setting the Voice
		tts.setVoice("dfki-poppy-hsmm");

		// TTS say something that we actually are typing into the first variable
		tts.speak(everything, 2.0f, false, true);

	}
	
	
}
