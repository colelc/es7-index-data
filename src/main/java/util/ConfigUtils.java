package util;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConfigUtils {
	private static Logger log = Logger.getLogger(ConfigUtils.class);

	private static Properties config = new Properties();
	private static Boolean override = Boolean.FALSE;

	static {
		try {
			loadProperties();

			override = ConfigUtils.getProperty("global.override.flag").trim().toUpperCase().compareTo("N") == 0 ? Boolean.FALSE : Boolean.TRUE;
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

	public static Boolean override() {
		return override;
	}

	public static Boolean execute(String run, String service) {
		if (ConfigUtils.override()) {
			log.info("Global override flag is TRUE: " + service + " will execute");
			return ConfigUtils.override();
		}
		if (run != null && run.trim().toUpperCase().compareTo("N") == 0) {
			// log.info(service + " will take no action because run flag is set to N");
			return Boolean.FALSE;
		}

		log.info(service + " will execute");
		return Boolean.TRUE;
	}

}
