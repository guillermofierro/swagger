package swagger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONObject;

/** 
 * @author gfierro
 * @date 28-02-2020
 */
public class Main {
	public static void main(String[] args) throws Exception {
		Main this_=new Main(); 
        JSONObject obj = new JSONObject(this_.procesar(this_.getFile("in/CompanyRelationship.json")));
        Map<String, String> mapDescripciones= Main.readContent(this_.getFile("in/relationship_detail.csv"));
        this_.getObject(obj, mapDescripciones);
        Main.write(new StringBuffer(obj.toString(4)), "out/temp.json");
	}
	
	public JSONObject getObject(JSONObject obj, Map<String, String> map){
		for(String name: JSONObject.getNames(obj)){
			try{
				JSONObject obj_=obj.getJSONObject(name);
				if (obj_.has("description")){
					String description=obj_.getString("description");
				}
				else{
					if(!name.equals("properties") && !name.equals("definitions") && !name.equals("StringTime") && !name.equals("NonRequiredStringDate") && !name.equals("StringDateTime") &&
						!name.equals("NonRequiredStringDateTime") && !name.equals("StringDate") && !name.equals("NonRequiredStringTime") && !name.equals(".tXML")){
						System.out.println(name);
						obj_.put("description", map.get(name));
					}
					getObject(obj.getJSONObject(name), map);
				}
			}
			catch(Exception e){
				;
			}
		}
		return null;
	}

	public String procesar(File file) throws Exception{
		StringBuilder str=new StringBuilder();
		BufferedReader br = new BufferedReader(new FileReader(file)); 	    	  
		String st;
		while ((st = br.readLine()) != null)
			str.append(st);
		return str.toString();
	}
	
	private static Map<String, String> readContent(File file){
		Map<String, String> map=new HashMap<String, String>();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				System.out.println(scanner.nextLine());
				String[] datos=scanner.nextLine().split("\\|");
				map.put(datos[0], datos[6]);
				
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		return map;
	}
	
	public static void write(StringBuffer content, String fileName) throws URISyntaxException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL url = classLoader.getResource(fileName);
		File file = new File(url.toURI().getPath());		
		
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(content.toString());
			//System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * @author gfierro
	 * @date 28-02-2020
	 */
	private File getFile(String path){
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(path);
		if (resource == null) {
			throw new IllegalArgumentException("file is not found!");
		} else {
			return new File(resource.getFile());
		}
	}
}