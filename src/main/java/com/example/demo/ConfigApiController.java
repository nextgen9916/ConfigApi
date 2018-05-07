package com.example.demo;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConfigApiController {
	
	private final String USER_AGENT = "Mozilla/5.0";
	private final String USER_AGENTS = "Mozilla/5.0";

	@RequestMapping("/{ipaddress}")
	public void fetchSnmpApi(HttpServletResponse res,@PathVariable("ipaddress") String ipaddress) throws IOException {
		
		PrintWriter out = res.getWriter();
		
		String SnmpNetconf="";
		String url="";

		  url = "http://localhost:8091/all/";
		 URL obj = new URL(url+ipaddress);
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

	        // optional default is GET
	        con.setRequestMethod("GET");

	        //add request header
	        con.setRequestProperty("User-Agent", USER_AGENT);

	        int responseCode = con.getResponseCode();

	        BufferedReader in = new BufferedReader(
	                new InputStreamReader(con.getInputStream()));
	        String inputLine;
	        StringBuffer str = new StringBuffer();

	        while ((inputLine = in.readLine()) != null) {
	        	str.append(inputLine);
	        }
	        in.close();
	        
//	        out.println(str.toString());
	        System.out.println(str.toString());
	        
	        SnmpNetconf = after((str.toString()),"= ");
			
			if(SnmpNetconf.contains("Snmp")) {
				
				url = "http://localhost:7076/";
				 URL obj1 = new URL(url+ipaddress);
			        HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();

			        // optional default is GET
			        con1.setRequestMethod("GET");

			        //add request header
			        con1.setRequestProperty("User-Agent", USER_AGENTS);

			        int responseCodes = con1.getResponseCode();

			        BufferedReader br = new BufferedReader(
			                new InputStreamReader(con1.getInputStream()));
			        String inputLines;
			        StringBuffer strs = new StringBuffer();

			        while ((inputLines = br.readLine()) != null) {
			        	strs.append(inputLines);
			        }
			        br.close();
			        
			        out.println(strs.toString());
			        System.out.println(strs.toString());
			}else if(SnmpNetconf.contains("Netconf")) {

				
				url = "http://localhost:8080/SysName/";
				
				 Map hamp = new HashMap<>();
			        hamp.put("ipaddress", ipaddress);
			       
			        RestTemplate restTemplate = new RestTemplate();
			        
			        String result = restTemplate.postForObject(url, hamp,String.class);
			        
				 /*URL obj1 = new URL(url+ipaddress);
			        HttpURLConnection con1 = (HttpURLConnection) obj1.openConnection();

			        // optional default is GET
			        con1.setRequestMethod("POST");
			        
			        con1.setDoOutput(true);

			        //add request header
			        con1.setRequestProperty("User-Agent", USER_AGENTS);
			        
			     // For POST only - START
					OutputStream os = con1.getOutputStream();
					os.write(ipaddress.getBytes());
					os.flush();
					os.close();
					// For POST only - END

			        int responseCode1 = con1.getResponseCode();
					System.out.println("POST Response Code :: " + responseCode1);

						BufferedReader br = new BufferedReader(new InputStreamReader(
								con1.getInputStream()));
						String inputLines;
						StringBuffer response = new StringBuffer();

						while ((inputLines = br.readLine()) != null) {
							response.append(inputLines);
						}
						in.close();*/
			        
			        out.println(result);
			        System.out.println(result);
			
			}
						
	        
	}
	
	static String after(String value, String a) {
        // Returns a substring containing all characters after a string.
        int posA = value.lastIndexOf(a);
        if (posA == -1) {
            return "";
        }
        int adjustedPosA = posA + a.length();
        if (adjustedPosA >= value.length()) {
            return "";
        }
        return value.substring(adjustedPosA);
    }
}
