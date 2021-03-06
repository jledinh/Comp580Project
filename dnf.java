
import javax.swing.*;

import java.util.Scanner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.auth.oauth2.GoogleCredentials;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.auth.ClientAuthInterceptor;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class dnf extends JFrame implements KeyListener{ // implements Runnable{
	
	//must uncomment the appropriate mp3 array for the tts to read out the prompts
	
	//Sports competition file
	
/*	final String []mp3s= {"name.mp3", "sport.mp3", "adjective.mp3", "place.mp3", "number.mp3", "number.mp3", 
			"animal_s.mp3", "name.mp3", "adjective.mp3", "verb_ed.mp3", "adverb.mp3"};
	*/
	
	//School
	final String []mp3s = {"number.mp3","verb_ing.mp3", "place.mp3", "feeling.mp3", "noun.mp3", "noun.mp3", "clothing.mp3",
			"time_of_day.mp3", "verb_motion.mp3", "vehicle.mp3", "noun.mp3", "subject.mp3"};
	
	//Mythical Beast Journey
	
/*	final String []mp3s = {"temperature_adjective.mp3", "place.mp3", "name.mp3", "noun.mp3", "city.mp3", "adjective.mp3",
			"adjective.mp3", "time_of_day.mp3", "adjective.mp3", "mythical_beast.mp3", "number.mp3", "weapon.mp3", "verb_ed.mp3",
			 "verb_ed.mp3"};
*/
	
	//Dictionland VS Fictionland
	/*
	final String []mp3s = {"number.mp3" , "animal_s.mp3", "instrument.mp3", "adjective.mp3", "name.mp3", "animal_same.mp3", 
	"verb_ed.mp3" , "food.mp3"};
	*/ 
	private JPanel titlePanel, textPanel, checkPanel; // storyPanel;
	private JLabel titleLabel, textLabel, checkLabel, inputLabel, storyLabel;
	private JTextField inputText,checkText;
	private TemplateConverter t;
	private JTextArea storyArea;
	private JScrollPane scrollPane;
	public static ArrayList<String> wordsList= new ArrayList<String>();
	int position;
	int i=0;
	private CommandLineParser parser;
	private String host;
	private Integer port;
	private Integer sampling;
	private Options options;
	
	
	public dnf(TemplateConverter a){
		t = a;
		createPanel();
		setTitle("Diction N' Fiction");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(true);
		addKeyListener(this);
		setSize(1300,800);	
		setVisible(true);
		setFocusable(true);
		position = 0;
		
		/*Establishes the connection for the client*/
		parser = new DefaultParser();
		host = null;
		port = null;
		sampling = null;
		options = new Options();
		options.addOption(
				Option.builder()
				.longOpt("host")
				.desc("endpoint for api, e.g. speech.googleapis.com")
				.hasArg()
				.argName("ENDPOINT")
				.build());
		options.addOption(
				Option.builder()
				.longOpt("port")
				.desc("SSL port, usually 443")
				.hasArg()
				.argName("PORT")
				.build());
		options.addOption(
				Option.builder()
				.longOpt("sampling")
				.desc("Sampling Rate, i.e. 16000")
				.hasArg()
				.argName("RATE")
				.build());
	}

	private void createPanel() {
		
		getContentPane().setLayout(null);
		titlePanel = new JPanel();
		titlePanel.setBounds(0, 0, 1300, 200);
		titlePanel.setBackground(new Color(0xC07F7F));
		getContentPane().add(titlePanel);

		titleLabel = new JLabel("DICTION N' FICTION", SwingConstants.CENTER);
		titleLabel.setPreferredSize(new Dimension(800, 200));
		titleLabel.setFont(new Font("Arial", Font.BOLD, 64));
		titlePanel.add(titleLabel);


		textPanel = new JPanel();
		textPanel.setBounds(0, 200, 1300, 620);
		textPanel.setLayout(new GridLayout(3,1)); 
		textPanel.setBackground(new Color(0xe9c97e));


		textLabel = new JLabel(t.getField().get(position), SwingConstants.CENTER); //SwingConstants.CENTER
		textLabel.setPreferredSize(new Dimension(400, 800));
		textLabel.setFont(new Font("Arial", Font.BOLD, 48));
		textPanel.add(textLabel);

		inputLabel = new JLabel();
		inputLabel.setPreferredSize(new Dimension(400, 800));
//		inputLabel.setText("Batman");
		inputLabel.setFont(new Font("Arial", Font.BOLD, 48));
		inputLabel.setHorizontalAlignment(SwingConstants.CENTER);
		textPanel.add(inputLabel);


		getContentPane().add(textPanel);


		speak("/Users/----/Documents/dnf_mp3/start.mp3");
		speak("/Users/----/Documents/dnf_mp3/"+mp3s[position]);
	}
	
	/*Runs the main code to create the gui*/
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
// must run change the hardcoded file path in order to get the correct story
				TemplateConverter t = new TemplateConverter("/Users/-----/Documents/workspace/hello/src/main/java/school.txt");

				//speechRun(args);
				new dnf(t);


			}
		});			
	}
	public void clientSetup(){

		CommandLineParser parser = new DefaultParser();

		Options options = new Options();
		options.addOption(
				Option.builder()
				.longOpt("host")
				.desc("endpoint for api, e.g. speech.googleapis.com")
				.hasArg()
				.argName("ENDPOINT")
				.build());
		options.addOption(
				Option.builder()
				.longOpt("port")
				.desc("SSL port, usually 443")
				.hasArg()
				.argName("PORT")
				.build());
		options.addOption(
				Option.builder()
				.longOpt("sampling")
				.desc("Sampling Rate, i.e. 16000")
				.hasArg()
				.argName("RATE")
				.build());
		//    return options;
	}
	static ManagedChannel createChannel(String host, int port) throws IOException {
		final List<String> OAUTH2_SCOPES =
				Arrays.asList("https://www.googleapis.com/auth/cloud-platform");
		GoogleCredentials creds = GoogleCredentials.getApplicationDefault();
		creds = creds.createScoped(OAUTH2_SCOPES);
		ManagedChannel channel =
				ManagedChannelBuilder.forAddress(host, port)
				.intercept(new ClientAuthInterceptor(creds, Executors.newSingleThreadExecutor()))
				.build();

		return channel;
	}
	public String speechRun(String[] args) throws InterruptedException, IOException{

		try {
			CommandLine line = parser.parse(options, args);

			host = line.getOptionValue("host", "speech.googleapis.com");
			port = Integer.parseInt(line.getOptionValue("port", "443"));
			// System.out.println(port);
			/*if (line.hasOption("sampling")) {
              sampling = Integer.parseInt(line.getOptionValue("sampling"));
            } else {
              System.err.println("An Audio sampling rate (--sampling) must be specified. (e.g. 16000)");
              System.exit(1);
            }
			 */
			// Avoid the command line thing
			sampling = 16000;
		} catch (ParseException exp) {
			System.err.println("Unexpected exception:" + exp.getMessage());
			System.exit(1);
		}


		ManagedChannel channel = createChannel(host, port);
		StreamingRecognizeClient client = new StreamingRecognizeClient(channel, sampling);
		try {

			client.recognize();	
			client.shutdown();

			wordsList = client.wordsList;
			//System.out.println(client.wordsList.get());
		
			return client.wordsList.get(i);

		} finally {
		//	client.shutdown();
			i++;
			
		}
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		String args[]= null;
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			//System.out.println(position);
			System.out.println(position);
			if (position < t.getField().size()) {
				position += 1;
				if (position != t.getField().size()) {
					textLabel.setText(t.getField().get(position));
				}

				try {
					String me= speechRun(args);
				    inputLabel.setText(me);
					
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				validate();
				repaint();
				if (position != t.getField().size()) {
					speak("/Users/-----/Documents/dnf_mp3/"+mp3s[position]);
				}
			}
			else if (position==t.getField().size()) {
				
				for (int i=0;i<wordsList.size();i++) {
					wordsList.set(i, wordsList.get(i).replace("\n", ""));
					System.out.println(wordsList.get(i));
				}
				t.InsertField(wordsList);
		
				
				
				storyArea = new JTextArea(); //50,40
				storyArea.setText(t.everything);
				storyArea.setFont(new Font("Arial", Font.BOLD, 32));
				storyArea.setBounds(0, 200, 1300, 620);
				storyArea.setMargin(new Insets(50,50,50,50));
				storyArea.setBackground(new Color(0xe9c97e));
				storyArea.setLineWrap(true);
				storyArea.setWrapStyleWord(true);
								
				textPanel.remove(textLabel);
				textPanel.remove(inputLabel);
				
				textPanel.setLayout(new GridLayout(1,1));
				textPanel.add(storyArea);
				getContentPane().add(textPanel);

				revalidate();
				repaint();
				position++;
				
				try {
					TextToMp3 ttm = new TextToMp3(t.everything, "/Users/-----/Documents/dnf_mp3/everything.mp3");
					speak(ttm.pathname);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			else{
				
				
			}
		}

	}

	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	/*Converts a string to an mp3 and then plays the mp3*/
	public void speak(String path) {
		try {
			//TextToMp3 ttm = new TextToMp3z(say, path);
			JLayerPlayer j = new JLayerPlayer(path);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
