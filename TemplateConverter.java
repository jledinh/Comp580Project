
import java.util.ArrayList;
import java.util.List;

//import model.TextToSpeech;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
//import marytts.modules.synthesis.Voice;

public class TemplateConverter {
	public String everything;
	private List<String> lines;
	private Path file;
	private ArrayList<String> fieldlist;
	private ArrayList<String> inputs;
	TemplateConverter(String path) {
		everything = "";
		lines = null;
		file = Paths.get(path);
		try { //reads in all of the lines
			lines = Files.readAllLines(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace(); //throws an exception if nothing there
		}		
		for (String s: lines) {
			everything = everything + s; //adds all the lines together
		}
		fieldlist = new ArrayList<String>();
		RetrieveField();
	}
	
	private void RetrieveField() {
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
	}
	
	public void InsertField(ArrayList<String> i) {
		this.inputs = i;
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
	}
	
	public void Print() {
		System.out.println(everything);
	}
	public int FieldSize() {
		return fieldlist.size();
	}
	
	public ArrayList<String> getField() {
		return fieldlist;
	}
}
