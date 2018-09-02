package com.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Utility class for functioning on JSON which was used to send responses and 
 */
public class JsonUtility {

	private final Gson gson;

	public JsonUtility() {
		gson = new GsonBuilder().create();
	}

	/*
	 * @Method getJson Converts JSON string to map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getJson(String json) {
		return (HashMap<String, String>) gson.fromJson(json, HashMap.class);
	}

	/*
	 * @Method convertTOJsonArray Converts List of objects to JSONArray
	 * 
	 * @return JSONArray as string
	 */
	public String convertTOJsonArray(List<Object> objects) {
		JsonArray jarray = gson.toJsonTree(objects).getAsJsonArray();
		return jarray.getAsString();
	}

	/*
	 * @Method convertStringsTOJsonArray takes List of strings and
	 * 
	 * @return JSONArray of strings
	 */
	public String convertStringsTOJsonArray(List<String> properties) {
		JsonArray jarray = gson.toJsonTree(properties).getAsJsonArray();
		return jarray.getAsString();
	}

	/*
	 * @Method convertToJson takes
	 * 
	 * @param key
	 * 
	 * @param List of objects and
	 * 
	 * @returns JSON Object
	 */
	public String convertToJson(String key, List<Object> objects) {
		JsonArray jarray = gson.toJsonTree(objects).getAsJsonArray();
		JsonObject jsonObject = new JsonObject();
		jsonObject.add(key, jarray);
		return jsonObject.toString();
	}

	/*
	 * @Method convertToJson takes
	 * 
	 * @param key
	 * 
	 * @param object
	 * 
	 * @returns JSON Object
	 */
	public String convertToJson(String key, Object object) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.add(key, gson.toJsonTree(object));
		return jsonObject.toString();
	}

	/*
	 * @Method convertToJson takes
	 * 
	 * @param map which consists of keys and values as strings 
	 * 
	 * @param another map which consists of keys as strings and values as objects
	 * 
	 * @returns JSONObject
	 */
	public String convertToJson(Map<String, String> normalProperties, Map<String, Object> objects) {
		JsonObject jsonObject = new JsonObject();
		for (String key : normalProperties.keySet()) {
			jsonObject.add(key, new JsonPrimitive(normalProperties.get(key)));
		}
		for (String key : objects.keySet()) {
			jsonObject.add(key, gson.toJsonTree(objects.get(key)));
		}
		return jsonObject.toString();
	}

}
