package elasticsesarch.service;

import org.apache.log4j.Logger;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;

public class AnalyzerService {
	private static Logger log = Logger.getLogger(AnalyzerService.class);

//	public static XContentBuilder getArabicAnalyzer() throws Exception {
//		try {
//			InputStream is = AnalyzerService.class.getResourceAsStream("json/arabic_analyzer.json");
//
//		} catch (Exception e) {
//			throw e;
//		}
//		return null;
//	}

	public static XContentBuilder getArabicAnalyzer() throws Exception {
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().prettyPrint().startObject();

			builder.startObject("analysis");
			builder.startObject("arabic_stemmer")/**/
					.field("type", "stemmer")/**/
					.field("language", "arabic")/**/
					.endObject();

			builder.startObject("analyzer");
			builder.startObject("arabic");
			builder.field("tokenizer", "standard");
			builder.endObject();

			builder.endObject();
			builder.endObject();
			builder.endObject();

			System.out.println(builder.toString());

			return builder;
		} catch (Exception e) {
			throw e;
		}
	}

}
