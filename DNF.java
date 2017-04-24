

import javax.swing.*;

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


public class DNF extends JFrame implements KeyListener{ // implements Runnable{	
	private JPanel titlePanel, textPanel, checkPanel;
	private JLabel titleLabel, textLabel, checkLabel;
	private JTextField inputText,checkText;
	private TemplateConverter t;
	public static ArrayList<String> wordsList= new ArrayList<String>();
	int position;
	int i=0;
	public DNF(TemplateConverter a){
		t = a;
		createPanel();
		setTitle("Diction N' Fiction");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		addKeyListener(this);
		setSize(600,600);	
		setVisible(true);
		setFocusable(true);
		position = 0;
		
	}
	
	private void createPanel() {
		titlePanel = new JPanel();
		titlePanel.setBounds(0, 0, 600, 120);
		titlePanel.setBackground(new Color(0xC07F7F));
		getContentPane().add(titlePanel);
		
		titleLabel = new JLabel("DICTION N' FICTION");
		titleLabel.setPreferredSize(new Dimension(500, 100));
		titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
		titlePanel.add(titleLabel);
			
		textPanel = new JPanel();
		textPanel.setBounds(0, 120, 600, 480);
		textPanel.setBackground(new Color(0xe9c97e));
		getContentPane().add(textPanel);
		textLabel = new JLabel(t.getField().get(position));
//			TODO: make "a noun" be replaceable with "an adj" or "a verb" or "an animal" etc.
		textLabel.setPreferredSize(new Dimension(300, 450));
		textLabel.setFont(new Font("Arial", Font.BOLD, 24));
		textPanel.add(textLabel);
		
/* 		checkPanel= new JPanel();
		checkPanel.setBounds(0,601,600,480);
		checkPanel.setBackground(new Color(0xffffff));
		getContentPane().add(checkPanel);
		checkLabel = new JLabel("Did you mean "+ t.getField().get(position) +" ?");
		checkLabel.setPreferredSize(new Dimension(300, 450));
		checkLabel.setFont(new Font("Arial", Font.BOLD, 24));
		checkPanel.add(checkLabel); */
		
		
		inputText = new JTextField();
//			TODO: make word spoken appear in the box
		inputText.setPreferredSize(new Dimension(250, 50));
		inputText.setText("Batman"); //insert input word...
		inputText.setFont(new Font("Arial", Font.BOLD, 24));
		textPanel.add(inputText);		
	
//		SwingUtilities.updateComponentTreeUI(this);
//		this.invalidate();
//		this.validate();
//		this.repaint();
		
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TemplateConverter t = new TemplateConverter("/Users/****/Documents/workspace/hello/src/main/java/test.txt");
	
					//speechRun(args);
	
				
				new DNF(t);
				
				
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
	    CommandLineParser parser = new DefaultParser();
	    String host = null;
	    Integer port = null;
	    Integer sampling = null;
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
          speech_to_text client = new speech_to_text(channel, sampling);
          try {
          System.out.println("reach");
            client.recognize();
            System.out.println("reach12");
          
         return client.wordsList.get(i);
       
          } finally {
        	 
          //	TemplateConverter t = new TemplateConverter("/Users/*/Documents/workspace/hello/src/main/java/"+file);
          //	wordsList.remove(wordsList.size()-1);
    //      	System.out.println(wordsList);
    //      	if(wordsList.size()==10){
          	client.shutdown();
          	 i++;
    //      	run=false;	
          		
          	}
//          	t.InsertField(wordsList);
//          	System.out.println(t.everything);
             //	System.out.println(wordsList);
             	//System.out.println("Hi, my name is " + wordsList.get(0) + " and I like to eat " + wordsList.get(1) + ". I go to UNC and I have"+ wordsList.get(2) + " dogs." );
        //    client.shutdown();
          }
    	
    	


	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		String args[]= null;
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            System.out.println(position);
            if (position != t.getField().size() - 1) {
            	position += 1;
            	textLabel.setText(t.getField().get(position));
            	
            	validate();
            	repaint();
            	try {
					inputText.setText(speechRun(args));
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
             	validate();
            	repaint();
            }
            else {
            	textLabel.setText(t.everything);
            	validate();
            	repaint();
            }
        }
		System.out.println(e.getID());
	//	System.out.println("hi");
		if (e.getKeyChar() == KeyEvent.VK_A) {
           // System.out.println(position);
     //       if (position != t.getField().size() - 1) {
      //      	position += 1;
                System.out.println("hi");
/*            	try {
				
	            	validate();
	            	repaint();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
       //     	validate();
       //     	repaint();
        //    }
        //    else {
         //   	textLabel.setText(t.everything);
          //  	validate();
          //  	repaint();
            }
        }
	



	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
