package tic_tac_toe;

public class Uri {
	public String[] path;
	public String query;
	public String fragment;
	
	Uri(String[] p, String q, String f) {
		path = p;
		query = q;
		fragment = f;
	}

	public static Uri parseUri(String uri) {
		String[] split = uri.split("#");
		
		uri = split[0];
		String fragment = "";
		for (int i = 1; i < split.length; i++) {
			fragment = fragment + split[i];
		}
		
		split = uri.split("\\?");
		String path = split[0];
		String query = "";
		for (int i = 1; i < split.length; i++) {
			query = query + split[i];
		}
		
		String[] splitPath = path.split("/");
		
		Uri parsed = new Uri(splitPath, query, fragment);
		return parsed;
	}
}