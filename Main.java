import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import javax.swing.JOptionPane;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.JPasswordField;
import java.awt.Font;


public class Main {
	
	private long startTime = -1;
	private long pausedTime = -1;
	private boolean skip = false;
	private JFrame frame;
	private int interval = -1;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {	 
		frame = new JFrame();
		frame.setBounds(100, 100, 240, 210);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.setBounds(24, 64, 91, 42);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("00:00:00:00");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(28, 11, 188, 42);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnReset = new JButton("Reset");
		btnReset.setEnabled(false);
		btnReset.setBounds(125, 64, 91, 42);
		frame.getContentPane().add(btnReset);
		
		textField = new JTextField();
		textField.setText("120");
		textField.setBounds(59, 142, 131, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Interval (Seconds)");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(59, 117, 131, 14);
		frame.getContentPane().add(lblNewLabel_1);

		 
		javax.swing.Timer t = new javax.swing.Timer(30, new ActionListener() {
	         public void actionPerformed(ActionEvent e) {	        	        	 
	        	 long totalSecs = (System.currentTimeMillis() - startTime);
	        	 int hours = (int) (totalSecs / 3600000);
	        	 int minutes = (int)((totalSecs % 3600000) / 60000);
	        	 int seconds = (int)((totalSecs % 60000) / 1000);
	        	 int milsec = (int)((totalSecs % 1000) / 10);

	        	 String timeString = String.format("%02d:%02d:%02d:%02d", hours, minutes, seconds, milsec);
	        	 lblNewLabel.setText(timeString);
	         }
	    });
		
		javax.swing.Timer t1 = new javax.swing.Timer(interval, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (skip) {
						skip=!skip;
						return;
				}
				
	        	try {
	        		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("notification.wav"));
	        		Clip clip = AudioSystem.getClip();
	        		clip.open(audioInputStream);
	        		clip.start();
	        	} catch (Exception ex) {
	        		ex.printStackTrace();
	        	}
			}	
		});
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (btnNewButton.getText().equals("Start")) {
					if (!(textField.getText() != null && textField.getText().matches("[-+]?\\d*\\.?\\d+")))
						return;
					startTime = System.currentTimeMillis();
					interval = Integer.parseInt(textField.getText())*1000;
					t1.setDelay(interval);
					skip=true;
					t.start();
					t1.start();
					btnNewButton.setText("Pause");
					textField.setEnabled(false);
				}
				else if (btnNewButton.getText().equals("Resume")) {
					startTime += System.currentTimeMillis()-pausedTime;
					interval = Integer.parseInt(textField.getText())*1000;
					t1.setDelay(interval);
					skip=true;
					t.start();
					t1.start();
					btnNewButton.setText("Pause");
					btnReset.setEnabled(false);
				}
				else {
					t.stop();
					t1.stop();
					pausedTime = System.currentTimeMillis();
					btnNewButton.setText("Resume");
					btnReset.setEnabled(true);
				}
			}
		});
		
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblNewLabel.setText("00:00:00:00");
				btnNewButton.setText("Start");
				btnReset.setEnabled(false);
				textField.setEnabled(true);
			}
		});
	}
}
