package com.ganzux.util.dyndns.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MainGui extends JFrame{
	private JLabel lblNewLabel;
	private JTextField user;
	private JLabel lblNewLabel_1;
	private JTextField path;
	private JTextField pass;
	JScrollPane scroll;
	private JTextField minutes;
	
	private JButton startButton = new JButton("Start !");
	private JButton stopButton = new JButton("Stop");
	
	private JTextArea out = new JTextArea();
	
	private StringBuilder sb = new StringBuilder();
	
	private Thread thread = null;
	private DynThread runnable = null;
	
	 protected MainGui()
	  {
	    setTitle("DynDNS Updater");
	    setSize(400, 350);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setLocationRelativeTo(null);
	    
	    JLabel lblDyndnsUpdater = new JLabel("DynDNS Updater");
	    lblDyndnsUpdater.setFont(new Font("Tahoma", Font.BOLD, 16));
	    lblDyndnsUpdater.setHorizontalAlignment(SwingConstants.CENTER);
	    lblDyndnsUpdater.setToolTipText("DynDNS Updater");
	    getContentPane().add(lblDyndnsUpdater, BorderLayout.NORTH);

	    out.setEditable(false);

	    out.setBackground(Color.BLACK);
	    out.setForeground(Color.GREEN);
	    out.setRows(8);
	    getContentPane().add(out, BorderLayout.SOUTH);
	    
	    JPanel panel = new JPanel();
	    getContentPane().add(panel, BorderLayout.CENTER);
	    panel.setLayout(new GridLayout(0,2));
	    
	    lblNewLabel = new JLabel("Path");
	    lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    panel.add(lblNewLabel);
	    
	    path = new JTextField();
	    path.setText("rpito.dyndns.org");
	    panel.add(path);
	    
	    JLabel lblNewLabel_2 = new JLabel("User");
	    lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    panel.add(lblNewLabel_2);
	    
	    user = new JTextField();
	    user.setText("aamsc");
	    panel.add(user);
	    
	    JLabel lblNewLabel_3 = new JLabel("Password");
	    lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    panel.add(lblNewLabel_3);
	    
	    pass = new JPasswordField();
	    panel.add(pass);
	    pass.setText("");
	    pass.setColumns(10);
	    
	    JLabel lblNewLabel_4 = new JLabel("Minutes");
	    lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
	    panel.add(lblNewLabel_4);
	    
	    minutes = new JTextField();
	    panel.add(minutes);
	    minutes.setColumns(10);
	    
	    startButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		startAction();
	    	}
	    });
	    stopButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		stopAction();
	    	}
	    });
	    getContentPane().add(startButton, BorderLayout.EAST);
	    
	    scroll = new JScrollPane (out, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    getContentPane().add( scroll, BorderLayout.SOUTH);
	 
	    prepareComponents(); // Throws the assertion error
	 
	    setVisible(true); // This will paint the entire frame
	  }
	 
	  /**
	   * Prepares the components for display. 
	   * 
	   * @throws AssertionError if not called on the EDT
	   */
	  private void prepareComponents()
	  {
	    assert SwingUtilities.isEventDispatchThread();
	  }
	 
	  /**
	   * @param args not used
	   */
	  public static void main(String[] args)
	  {
	    SwingUtilities.invokeLater(new Runnable() {
	      @Override
	      public void run()
	      {
	    	  MainGui example = new MainGui();
	      }
	    });
	  }
	  
	  
	  
	  private void startAction(){
		  OutUtil.getInstance().addText("Starting...", out);
		  if ( path.getText() != null && !path.getText().isEmpty() &&
	    			pass.getText() != null && !pass.getText().isEmpty() &&
	    			user.getText() != null && !user.getText().isEmpty() &&
	    			minutes.getText() != null && !minutes.getText().isEmpty()
	    		){
	    			try{
	    				int minutesInt = Integer.parseInt( minutes.getText() );
	    				
	    				getContentPane().remove( startButton );
	    				getContentPane().add(stopButton, BorderLayout.EAST);
	    				
	    				path.setEditable( false );
	    				  pass.setEditable( false );
	    				  user.setEditable( false );
	    				  minutes.setEditable( false );
	    				  
	    				  runnable = new DynThread( minutesInt, out, scroll,  user.getText(),pass.getText(),path.getText() );
	    			      thread = new Thread(runnable);
	    			      thread.start();
	    			      OutUtil.getInstance().addText("Started", out);
	    			} catch ( Exception e){
	    				OutUtil.getInstance().addText("Minutes must be a number", out);
	    			}
	    		} else {
	    			OutUtil.getInstance().addText("You must set all the paremeters", out);
	    		}
	  }
	  
	  private void stopAction(){
		  OutUtil.getInstance().addText("Stopping...", out);
		  OutUtil.getInstance().addText("Stopped", out);
		  getContentPane().remove( stopButton );
		  getContentPane().add(startButton, BorderLayout.EAST);
		  path.setEditable( true );
		  pass.setEditable( true );
		  user.setEditable( true );
		  minutes.setEditable( true );
		  if (thread != null) {
	            runnable.terminate();
	            try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	  }
}
