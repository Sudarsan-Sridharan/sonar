package sonar.core.internal;

import com.google.gson.Gson;

public class JsonConverter {
	
	public static String toJSON(Object o) {
		Gson gson = new Gson();
		//System.out.println("before json");
		String val;
		try {
			val = gson.toJson(o);
			return val;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object toObject(Class<?> clazz, String str) {
		Gson gson = new Gson();
		try {
			return gson.fromJson(str, clazz);
		} catch(Exception e)
		{
			System.out.println(e.toString());
		}
		return null;
	}
}
