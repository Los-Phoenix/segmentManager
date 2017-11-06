package dictionary;

import java.net.URL;
import java.util.MissingResourceException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ParameterConfig {
	/**
	 * This config path is used as a resource.
	 */
	public static final String CONFIG_RES_PATH = "/nlp.properties";

	private static final URL CONFIG_URL = ParameterConfig.class
			.getResource(CONFIG_RES_PATH);

	private static Configuration config;

	static {
		try {
			config = new PropertiesConfiguration(CONFIG_URL);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	private ParameterConfig() {
		try {
			config = new PropertiesConfiguration(CONFIG_URL);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	public static String getString(String key) {
		try {
			// return RESOURCE_BUNDLE.getString(key);
			return config.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String[] getElements(String key) {
		String[] strArray = config.getStringArray(key);
		// for(int i=0;i<strArray.length;i++) {
		// System.out.println(strArray[i]);
		// }
		return strArray;
	}
}
