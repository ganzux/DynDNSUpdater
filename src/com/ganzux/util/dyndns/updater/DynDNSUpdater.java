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

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class DynDNSUpdater {

	///////////////////////////////////////////////////////////////
	//                         SingleTon                         //
	///////////////////////////////////////////////////////////////

	private DynDNSUpdater(){
//		logger.info("New instance for DynDNSUpdater");

		prop = new Properties();
		uris = new ArrayList<String>();
		InputStream input = null;
		 
		try {
//			logger.info("Trying to read properties...");
			input = this.getClass().getClassLoader().getResourceAsStream("resources/config.properties");

			prop.load( input );
//			logger.info("properties readed");

			String urls = prop.getProperty("url.apis");
			if ( urls != null ){
//				logger.info("Reading URL");
				for ( String u:urls.split(",") ){
					uris.add( u );
//					logger.info( u );
				}
			}

		} catch (IOException ex) {
//			logger.error( ex.getMessage() );
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
//	private static Logger logger = LoggerFactory.getLogger( DynDNSUpdater.class );
	///////////////////////////////////////////////////////////////
	//                     End Of Atributes                      //
	///////////////////////////////////////////////////////////////


	///////////////////////////////////////////////////////////////
	//                       Public Methods                      //
	///////////////////////////////////////////////////////////////

	public String[] resetDynDNS( String nowPublicIP, String user, String pass, String path ){
		String[] reset = new String[3];
		
		try{
			reset[0] = nowPublicIP;
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
			reset[1] = String.valueOf( responseCode );

			// Print feedback
			String line;
			reset[2] = "";
			InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
			BufferedReader buff = new BufferedReader(in);
			do {
				line = buff.readLine();
				reset[2] += line;
			} while (line != null);

			connection.disconnect();
		 
		} catch (Exception ex) {
			
		}
		
		return reset;
	}
	
	public String[] resetDynDNS( String user, String pass, String path ){
		return resetDynDNS( getPublicIP(), user, pass, path);
	}

	public String getPublicIP(){
		
		try{
			for ( String apiUrl:uris ){
				URL myIP;
				try{
					myIP = new URL( apiUrl );
					BufferedReader in = new BufferedReader(  new InputStreamReader(myIP.openStream()) );
					return in.readLine();
				} catch ( Exception e ){
					
				}
			}
		} catch ( Exception e ){
			
		}
		return null;

	}
	///////////////////////////////////////////////////////////////
	//                    End of Public Methods                  //
	///////////////////////////////////////////////////////////////



	///////////////////////////////////////////////////////////////
	//                      Private Methods                      //
	///////////////////////////////////////////////////////////////

	

	///////////////////////////////////////////////////////////////
	//                   End of Private Methods                  //
	///////////////////////////////////////////////////////////////
}
