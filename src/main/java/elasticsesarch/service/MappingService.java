package elasticsesarch.service;

import org.apache.log4j.Logger;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;

public class MappingService {
	private static Logger log = Logger.getLogger(MappingService.class);
	private static final String TIRE_KICK = "Tire Kick: ";

	private static XContentBuilder getMulticulturalMapping() throws Exception {
		// this implementation is now done without XContentBuilder
		// **only** because I have a Maven issue with the .withJson method
		// urk
		// making this method private so I can see it's never used
		try {
			return XContentFactory.jsonBuilder().startObject()/**/
					.field("dynamic", "strict")/**/
					.startObject("properties")/**/
					.startObject("contents")/**/
					.startObject("properties")/**/
					.startObject("language")/**/
					.field("type", "keyword")/**/
					.endObject()/**/ // language

					.startObject("supported")/**/
					.field("type", "boolean")/**/
					.endObject()/**/ // supported

					.startObject("default")/**/
					.field("type", "text")/**/
					.field("analyzer", "default")/**/
					.startObject("fields")/**/
					.startObject("icu")/**/
					.field("type", "text")/**/
					.field("analyzer", "icu_analyzer")/**/
					.endObject()/**/ // icu
					.endObject()/**/ // fields
					.endObject()/**/ // default

					.startObject("en")/**/
					.field("type", "text")/**/
					.field("analyzer", "english")/**/
					.endObject()/**/ // en

					.startObject("ar")/**/
					.field("type", "text")/**/
					.field("analyzer", "arabic")/**/
					.endObject()/**/ // ar

					.startObject("de")/**/
					.field("type", "text")/**/
					.field("analyzer", "german")/**/
					.endObject()/**/ // de

					.endObject()/**/ // properties
					.endObject()/**/ // contents
					.endObject()/**/ // properties
					.endObject();

		} catch (Exception e) {
			throw e;
		}
	}

//	public static void putMappings(ElasticsearchClient client, String indexName) throws Exception {
//		try {
//
//			XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
//
//			builder.startObject("properties");
//			builder.startObject("raw").field("type", "text").endObject();
//			builder.startObject("arabic").field("type", "text").endObject();
//			builder.startObject("english").field("type", "text").endObject();
//			builder.startObject("unicode").field("type", "text").endObject();
//			builder.endObject(); // properties
//
//			builder.endObject();
//
//			log.info(TIRE_KICK + Strings.toString(builder));
//
//			PutMappingRequest request = new PutMappingRequest(indexName).source(builder);
//			AcknowledgedResponse response = client.indices().putMapping(request, RequestOptions.DEFAULT);
//
//			if (response.isAcknowledged()) {
//				log.info(TIRE_KICK + "The mapping for index \"" + indexName + "\" " + "has been created");
//				// getMappings(client, indexName);
//			}
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	public static void getMappings(ElasticsearchClient client, String indexName) throws Exception {
//		try {
//			GetMappingsRequest request = new GetMappingsRequest().indices(indexName);
//			GetMappingsResponse response = client.indices().getMapping(request, RequestOptions.DEFAULT);
//
//			log.info(TIRE_KICK + "The mapping for index \"" + indexName + "\" " + "is: ");
//			Map<String, MappingMetadata> mappings = response.mappings();
//			mappings.forEach((key, value) -> {
//				Map<String, Object> map = value.getSourceAsMap();
//				map.forEach((k, v) -> {
//					log.info(TIRE_KICK + k + ": " + String.valueOf(v));
//				});
//			});
//
//		} catch (Exception e) {
//			throw e;
//		}
//	}

}
