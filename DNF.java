import javax.swing.*;
import java.awt.*;

public class DNF extends JFrame { // implements Runnable{	
	private JPanel titlePanel, textPanel;
	private JLabel titleLabel, textLabel;
	private JTextField inputText;
	
	public DNF(){
		createPanel();
		
		setTitle("Diction N' Fiction");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setSize(600,600);	
		setVisible(true);
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
		
		textLabel = new JLabel("Please enter a noun:");
//			TODO: make "a noun" be replaceable with "an adj" or "a verb" or "an animal" etc.
		textLabel.setPreferredSize(new Dimension(300, 450));
		textLabel.setFont(new Font("Arial", Font.BOLD, 24));
		textPanel.add(textLabel);
		
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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new DNF();
			}
		});			
	}

}
