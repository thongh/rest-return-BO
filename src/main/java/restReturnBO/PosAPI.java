package restReturnBO;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.security.cert.X509Certificate;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class PosAPI {

	public static void main(String[] args) {
		try {
			callREST("https://192.168.100.64:8082/pos-api/", "inventories/warehouses", "GET", "bpmapiuser", "APiU53rBpM");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String callREST(String restEndpoint, String restResource, String method,
			String username, String password) throws IOException {
	    disableSslVerification();
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
			while ((output = br.readLine()) != null) {
				stringBuffer.append(output);
			}						
			conn.disconnect();			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}				
//		System.out.println(stringBuffer.toString());
//		System.out.println(response);
		return stringBuffer.toString();
	}
	
	private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
	
}
