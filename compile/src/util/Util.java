package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Util {

	public static void printTextFile(String fileName) throws IOException {
		BufferedReader buffReader = null;
		try {
			buffReader = new BufferedReader(new FileReader(fileName));
			String content = buffReader.readLine();
			while (content != null) {
				System.out.println(content);
				content = buffReader.readLine();
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (buffReader != null)
					buffReader.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}
}
