package swagger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
		Main this_ = new Main();
		System.out.println("1");
		JSONObject obj = new JSONObject(this_.procesar(this_.getFile("in/CompanyRelationship.json")));
		System.out.println("2");
		Map<String, String> mapDescripciones = Main.readCsvContent(this_.getFile("in/Relationship_detail.csv"));
		System.out.println("3");
		this_.getObject(obj, mapDescripciones);
		System.out.println("4");
		Main.write(new StringBuffer(obj.toString(4)), "temp.json");
	}

	public JSONObject getObject(JSONObject obj, Map<String, String> map) {
		for (String name : JSONObject.getNames(obj)) {
			try {
				JSONObject obj_ = obj.getJSONObject(name);
				if (obj_.has("description")) {
					String description = obj_.getString("description");
				} else {
					if (!name.equals("properties") && !name.equals("definitions") && !name.equals("StringTime")
							&& !name.equals("NonRequiredStringDate") && !name.equals("StringDateTime")
							&& !name.equals("NonRequiredStringDateTime") && !name.equals("StringDate")
							&& !name.equals("NonRequiredStringTime") && !name.equals(".tXML")) {
						System.out.println(name);
						obj_.put("description", map.get(name));
					}
					getObject(obj.getJSONObject(name), map);
				}
			} catch (Exception e) {
				;
			}
		}
		return null;
	}

	public String procesar(InputStream is) throws Exception {
		StringBuilder str = new StringBuilder();
		Scanner scanner = new Scanner(is);
		while (scanner.hasNextLine()) {
			str.append(scanner.nextLine());
		}
		scanner.close();
		return str.toString();
	}

	private static Map<String, String> readCsvContent(InputStream is) {
		Map<String, String> map = new HashMap<String, String>();

		Scanner scanner = new Scanner(is);
		while (scanner.hasNextLine()) {
			System.out.println(scanner.nextLine());
			String[] datos = scanner.nextLine().split("\\|");
			map.put(datos[0], datos[6]);

		}
		scanner.close();
		return map;
	}

	public static void write(StringBuffer content, String fileName) throws Exception {
		File file = new File(fileName);
		  
		//Create the file
		if (file.createNewFile())
		{
		    System.out.println("File is created!");
		} else {
		    System.out.println("File already exists.");
		}
		 
		//Write Content
		FileWriter writer = new FileWriter(file);
		writer.write(content.toString());
		writer.close();
	}

	/**
	 * @author gfierro
	 * @date 28-02-2020
	 */
	private InputStream getFile(String path){
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream is = classLoader.getResourceAsStream(path);
		return is;
	}
}