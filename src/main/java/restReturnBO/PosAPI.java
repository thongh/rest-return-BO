package restReturnBO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PosAPI {

	public static void main(String[] args) {
		try {
			callREST("", "", "", "", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String callREST(String restEndpoint, String restResource, String method,
			String username, String password) throws IOException {
		String response = "";
		StringBuffer stringBuffer = new StringBuffer();
		URL url = null;
		try {
			String restUrl = "";
			if (restEndpoint == "") {
				restEndpoint = "http://192.168.101.225:8082/pos-api/";
			}
			
			if (restResource == "") {
				restResource = "inventories/warehouses";
			}

			if (method == "") {
				method = "GET";
			}
			
			restUrl = restEndpoint + restResource;
				
			url = new URL(restUrl);
			
			// Open connection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// Set method		
			conn.setDoOutput(true);
			conn.setRequestMethod(method);
			// Request header
			conn.setRequestProperty("Source-Type", "WEB");
			conn.setRequestProperty("Source-Name", "BPM");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Accept-Language", "en");
			
			// Auth
			if (username == "") {
				username = "bpmapiuser";
			}
			if (password == "") {
				password = "APiU53rBpM";
			}
			String encoded = Base64.getEncoder().encodeToString((username+":"+password).getBytes(StandardCharsets.UTF_8));  //Java 8
			conn.setRequestProperty("Authorization", "Basic "+encoded);
					
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			
			String output;
//			output = br.readLine();
			
//			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
//				System.out.println(output);
				response = output;
				stringBuffer.append(output);
			}
			
			
			
			conn.disconnect();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();

		}
				
		System.out.println(stringBuffer.toString());
		System.out.println(response);
		return stringBuffer.toString();
	}
	
}
