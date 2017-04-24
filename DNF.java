package com.examples.cloud.speech;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class dnf extends JFrame implements KeyListener{ // implements Runnable{	
	private JPanel titlePanel, textPanel;
	private JLabel titleLabel, textLabel;
	private JTextField inputText;
	private TemplateConverter t;
	int position;
	public dnf(TemplateConverter a){
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
			public void run() {
				TemplateConverter t = new TemplateConverter("/Users/jledinh/Documents/hello/src/main/java/file.txt");
				new dnf(t);
				
			}
		});			
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            System.out.println(position);
            if (position != t.getField().size() - 1) {
            	position += 1;
            	textLabel.setText(t.getField().get(position));
            	validate();
            	repaint();
            }
            else {
            	textLabel.setText(t.everything);
            	validate();
            	repaint();
            }
        }
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
