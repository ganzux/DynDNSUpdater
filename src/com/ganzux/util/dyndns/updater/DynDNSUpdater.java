package com.ganzux.util.dyndns.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class DynDNSUpdater {

	///////////////////////////////////////////////////////////////
	//                         SingleTon                         //
	///////////////////////////////////////////////////////////////

	private DynDNSUpdater(){
		PropertyConfigurator.configure("log4j.properties");
		logger.info("New instance for DynDNSUpdater");

		prop = new Properties();
		uris = new ArrayList<String>();
		InputStream input = null;
		 
		try {
			logger.info("Trying to read properties...");
			input = this.getClass().getClassLoader().getResourceAsStream("resources/config.properties");

			prop.load( input );
			logger.info("properties readed");

			String urls = prop.getProperty("url.apis");
			if ( urls != null ){
				logger.info("Reading URL");
				for ( String u:urls.split(",") ){
					uris.add( u );
					logger.info( u );
				}
			}

		} catch (IOException ex) {
			logger.error( ex );
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static DynDNSUpdater instance;

	public static synchronized DynDNSUpdater getInstance(){
		if ( instance == null )
			instance = new DynDNSUpdater();
		return instance;
	}

	///////////////////////////////////////////////////////////////
	//                      End of SingleTon                     //
	///////////////////////////////////////////////////////////////



	///////////////////////////////////////////////////////////////
	//                         Atributes                         //
	///////////////////////////////////////////////////////////////
	private Properties prop;
	private List<String> uris;
	private static Logger logger = Logger.getLogger( DynDNSUpdater.class.getName() );
	///////////////////////////////////////////////////////////////
	//                     End Of Atributes                      //
	///////////////////////////////////////////////////////////////


	///////////////////////////////////////////////////////////////
	//                       Public Methods                      //
	///////////////////////////////////////////////////////////////

	public void resetDynDNS( String nowPublicIP, String user, String pass, String path ){
		try{
			String userpassword = user + ":" + pass;
			String encodedAuthorization = DatatypeConverter.printBase64Binary(userpassword.getBytes());
			 
			// Connect to DynDNS
			URL url = new URL("http://members.dyndns.org/nic/update?hostname=" + path + "&myip=" + nowPublicIP);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent", "Demo DynDNS Updater");
			connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
			 
			// Execute GET
			int responseCode = connection.getResponseCode();

			// Print feedback
			String line;
			InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
			BufferedReader buff = new BufferedReader(in);
			do {
				line = buff.readLine();
			} while (line != null);

			connection.disconnect();
		 
		} catch (Exception ex) {
			
		}
	}
	
	public void resetDynDNS( String user, String pass, String path ){
		resetDynDNS( getPublicIP(), user, pass, path);
	}

	///////////////////////////////////////////////////////////////
	//                    End of Public Methods                  //
	///////////////////////////////////////////////////////////////



	///////////////////////////////////////////////////////////////
	//                      Private Methods                      //
	///////////////////////////////////////////////////////////////

	private String getPublicIP(){
		
		try{
			for ( String apiUrl:uris ){
				URL myIP;
				myIP = new URL( apiUrl );
	            BufferedReader in = new BufferedReader(  new InputStreamReader(myIP.openStream()) );
	            return in.readLine();
			}
		} catch ( Exception e ){
			
		}
		return null;

	}

	///////////////////////////////////////////////////////////////
	//                   End of Private Methods                  //
	///////////////////////////////////////////////////////////////
}
