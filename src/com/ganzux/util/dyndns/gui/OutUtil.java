package com.ganzux.util.dyndns.gui;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;

public class OutUtil {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
	private static OutUtil instance;
	private OutUtil(){
	}
	public static OutUtil getInstance(){
		if ( instance == null )
			instance = new OutUtil();
		return instance;
	}

	private static String getTimeFormatted(){
		  return sdf.format( new Date() ) + " - ";
	  }
	  
	  public void addText( String text,JTextArea out ){
		  StringBuilder sb = new StringBuilder();
		  sb.append( getTimeFormatted() ).append( text ).append("\r\n");
		  out.setText( out.getText() + sb.toString() );
	  }
}
