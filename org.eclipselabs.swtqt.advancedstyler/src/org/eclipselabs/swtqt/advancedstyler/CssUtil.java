package org.eclipselabs.swtqt.advancedstyler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CssUtil {

	public static String loadStyle(Style st) {
		String style = "/styles/" + st.getCss() + ".css";
		InputStream ins = CssUtil.class.getResourceAsStream(style);

		if (null == ins) {
			throw new RuntimeException("Style not found: " + style);
		}

		StringBuilder out = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(ins));
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				out.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return out.toString();
	}

	public enum Style {
		COMPEOPLE("compeople"), LIGHTBLUE("cust1"), DARK("dark"), GLOSSY("glossy"), ECLIPSE_RIENA("eclipse-riena"), CUST2_RIENA(
				"cust2-riena");

		private String css;

		Style(String css) {
			this.css = css;
		}

		public String getCss() {
			return css;
		}
	}
}
