package util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

public class FileUtils {
	private static Logger log = Logger.getLogger(FileUtils.class);

	public static Set<String> getFileListFromDirectory(String directory, String targetFileName) throws Exception {
		try {
			return Stream.of(new File(directory).listFiles())/**/
					.filter(f -> f.getName().startsWith(targetFileName))/**/
					.map(File::getName)/**/
					.collect(Collectors.toSet());
		} catch (Exception e) {
			throw e;
		}
	}

	public static List<String> readFileIntoList(String directory, String fileName) throws Exception {
		try {
			String filePath = directory + File.separator + fileName;
			List<String> lines = Files.readAllLines(Paths.get(filePath));
			return lines;
		} catch (Exception e) {
			throw e;
		}
	}

	public static void combine() throws Exception {
		try (PrintWriter out = new PrintWriter(new FileWriter(/**/
				ConfigUtils.getProperty("directory.playbyplay.document") + File.separator + ConfigUtils.getProperty("playbyplay.document.json.file.name") + ".json"))) {

			Set<String> files = FileUtils.getFileListFromDirectory(ConfigUtils.getProperty("directory.playbyplay.document"), ConfigUtils.getProperty("files.playbyplay.input"));

			for (String file : files) {
				List<String> documentList = FileUtils.readFileIntoList(ConfigUtils.getProperty("directory.playbyplay.document"), file);

				for (String docString : documentList) {
					out.write(docString + "\n");
				}
			}

		} catch (Exception e) {
			throw e;
		}

	}

//	public static void main(String[] args) {
//		try {
//			Set<String> test = getFileListFromDirectory("C:\\Users\\oggie\\eclipse-workspace\\WbbScreenScraper\\output\\data", "gamecast");
//			for (String t : test) {
//				log.info(t);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}

}