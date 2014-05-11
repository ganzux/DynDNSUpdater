package com.ganzux.util.dyndns.gui;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.ganzux.util.dyndns.updater.DynDNSUpdater;

public class DynThread implements Runnable {

	private volatile boolean running = true;
	private String text;
	JTextArea out;
	JScrollPane scroll;
	private int minuteFactor = 60000;
	
	private int minutes = 0;
	private String user;
	private String pass;
	private String path;

    public DynThread(int minutes, JTextArea out, JScrollPane scroll, String user, String pass, String path) {
		super();
		this.minutes = minutes;
		this.out = out;
		this.user = user;
		this.pass = pass;
		this.path = path;
		this.scroll = scroll;
	}

	public void terminate() {
        running = false;
    }
    
    @Override
    public void run() {
        while (running) {
            try {
            	OutUtil.getInstance().addText("Init Reset", out);
            	
            	String[] reset = DynDNSUpdater.getInstance().resetDynDNS(user, pass, path);
            	
            	OutUtil.getInstance().addText("IP: " + reset[0], out);
            	OutUtil.getInstance().addText("Code: " + reset[1], out);
            	OutUtil.getInstance().addText("Comments: " + reset[2], out);
            	
            	
            	OutUtil.getInstance().addText("End Reset", out);
            	
            	JScrollBar vertical = scroll.getVerticalScrollBar();
            	vertical.setValue( vertical.getMaximum() );
            	
                Thread.sleep((long) minutes * minuteFactor);
            } catch (InterruptedException e) {
                running = false;
            }
        }

    }

	public String getText() {
		return text;
	}
    
}
