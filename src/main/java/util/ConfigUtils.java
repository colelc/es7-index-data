package util;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigUtils {
	private static Logger log = Logger.getLogger(ConfigUtils.class);

	private static Properties config = new Properties();

	static {
		try {
			loadProperties();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			System.exit(99);
		}
	}

	public static void loadProperties() throws Exception {

		try (InputStream in = ConfigUtils.class.getClassLoader().getResourceAsStream("elasticsearch.properties");) {
			config.load(in);
			config.forEach((k, v) -> log.info(k + " -> " + v));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @return the config
	 */
	public static Properties getConfig() {
		return config;
	}

	public static String getProperty(String key) throws Exception {
		String value = null;
		try {
			if (key == null || key.trim().length() == 0) {
				throw new Exception("Unknown property key");
			}
			value = config.getProperty(key);
			if (value == null) {
				throw new Exception("Unknown property value for key: " + key);
			}
		} catch (Exception e) {
			throw e;
		}
		return value;
	}

	public static int getPropertyAsInt(String key) throws Exception {
		try {
			String value = getProperty(key);
			return Integer.valueOf(value).intValue();
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getGamecastJsonOutputFile() throws Exception {
		try {
			return getProperty("directory.gamecast.document") + File.separator + getProperty("gamecast.json.file.name");
		} catch (Exception e) {
			throw e;
		}

	}

}
