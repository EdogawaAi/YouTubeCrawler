package data_sample_json;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Search;
import com.google.api.services.youtube.auth.Auth;
import com.google.api.services.youtube.model.VideoListResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Video_json {

	/**
	 * Define a global variable that identifies the name of a file that contains
	 * the developer's API key.
	 */
	private static final String PROPERTIES_FILENAME = "youtube.properties";

	/**
	 * Define a global instance of a Youtube object, which will be used to make
	 * YouTube Data API requests.
	 */
	private static YouTube youtube;

	/**
	 * Initialize a YouTube object to search for videos on YouTube. Then display
	 * the name and thumbnail image of each video in the result set.
	 *
	 * @param args
	 *            command line args.
	 */
	public static void main(String[] args) {
		// Read the developer key from the properties file.
		Properties properties = new Properties();
		try {
			InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
			properties.load(in);

		} catch (IOException e) {
			System.err.println(
					"There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
			System.exit(1);
		}

		try {
			// This object is used to make YouTube Data API requests. The last
			// argument is required, but since we don't need anything
			// initialized when the HttpRequest is initialized, we override
			// the interface and provide a no-op function.
			youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
				public void initialize(HttpRequest request) throws IOException {
				}
			}).setApplicationName("youtube-cmdline-video").build();

			// Prompt the user to enter an input id.
			String inputId = getInputId();

			// Define the API request for retrieving search results.
			YouTube.Videos.List videos = youtube.videos().list("id,snippet,contentDetails,status,statistics");

			// Set your developer key from the {{ Google Cloud Console }} for
			// non-authenticated requests. See:
			// {{ https://cloud.google.com/console }}
			String apiKey = properties.getProperty("youtube.apikey");
			videos.setKey(apiKey);
			videos.setId(inputId);

			// Call the API and print results.
			VideoListResponse videoResponse = videos.execute();
			
			System.out.println(videoResponse.toPrettyString());



		} catch (GoogleJsonResponseException e) {
			System.err.println(
					"There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
		} catch (IOException e) {
			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/*
	 * Prompt the user to enter a video id and return the user-specified value.
	 */
	private static String getInputId() throws IOException {

		String inputQuery = "";

		System.out.print("Please enter video id: ");
		BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
		inputQuery = bReader.readLine();

		if (inputQuery.length() < 1) {
			// Use the string "YouTube Developers Live" as a default.
			inputQuery = "YouTube Developers Live";
		}
		return inputQuery;
	}
}
