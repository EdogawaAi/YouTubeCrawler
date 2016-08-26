package video_id_generator;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

public class Try {
	public static void main(String args[]) {

		String str = new SimpleDateFormat("HHmmss").format(new Date());
		System.out.println(str);
	}

}
