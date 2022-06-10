package elasticsesarch.service;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import data.JsonData;
import vo.Gamecast;

public class IndexerService {
	private static Logger log = Logger.getLogger(IndexerService.class);

	public static void indexGamecastData(ElasticsearchClient client, String indexName) throws Exception {
		try {
			JsonArray jsonArray = JsonData.getJsonArray();

			jsonArray.forEach(jsonElement -> {
				if (jsonElement.isJsonObject()) {
					JsonObject jsonObject = jsonElement.getAsJsonObject();

					jsonObject.keySet().forEach(key -> {
						log.info(key + " -> " + jsonObject.get(key));
					});

					try {
						// Gson gson = new Gson();
						log.info(jsonObject.toString());
						// Gamecast gamecast = gson.fromJson(jsonObject.toString(), vo.Gamecast.class);
						ObjectMapper objectMapper = new ObjectMapper();
						// objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
						Gamecast gamecast = objectMapper.readValue(jsonObject.toString(), Gamecast.class);

						IndexRequest.Builder<Gamecast> indexRequestBuilder = new IndexRequest.Builder<Gamecast>()/**/
								.index(indexName)/**/
								.id(jsonObject.get("id").getAsString())/**/
								.document(gamecast);

						IndexResponse response = client.index(indexRequestBuilder.build());
						if (response.result().compareTo(Result.Created) == 0) {
							log.info(response.result().toString() + " " + response.id() + " -> " + response.index() + " ");
						} else {
							log.info("ERROR: " + response.result().toString());
						}

					} catch (JsonParseException e1) {
						log.error(e1.getMessage());
						e1.printStackTrace();
					} catch (JsonMappingException e1) {
						log.error(e1.getMessage());
						e1.printStackTrace();
					} catch (IOException e1) {
						log.error(e1.getMessage());
						e1.printStackTrace();
					}

				}

			});
		} catch (Exception e) {
			throw e;
		}
	}

//	public static void indexMulticulturalDocumentTest(ElasticsearchClient client, String indexName) throws Exception {
//		try {
//			testContentList.forEach(content -> {
//				log.info("Indexing content: " + content);
//				try {
//					IndexRequest indexRequest = new IndexRequest(indexName).source(generateTestDocument(content));
//					indexRequest.setPipeline(PipelineService.getLanguageInferencePipelineId());
//
//					IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
//					Result result = indexResponse.getResult();
//					if (Result.valueOf(result.name()).compareTo(DocWriteResponse.Result.CREATED) == 0) {
//						log.info("Document has been indexed");
//					}
//				} catch (Exception e) {
//					log.error(e.getMessage());
//					e.printStackTrace();
//				}
//			});
//
//			Thread.sleep(5000l);
//
//			QueryService.testMatchAll(client);
//
//		} catch (Exception e) {
//			throw e;
//		}
//	}

	private static XContentBuilder generateTestDocument(String content) throws Exception {
		try {
			XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
			builder.field("contents", content);
			builder.endObject();

			return builder;
		} catch (Exception e) {
			throw e;
		}
	}

//	public static void indexTestDocument(ElasticsearchClient client, String indexName) throws Exception {
//		try {
//			XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
//			builder.field("raw", "The quick brown fox jumped over the lazy dog");
//			builder.field("random_english", "A lazy white duck flew above the lake");
//			builder.field("arabic", "Ù‚Ù�Ø² Ø§Ù„Ø«Ø¹Ù„Ø¨ Ø§Ù„Ø¨Ù†ÙŠ Ø§Ù„Ø³Ø±ÙŠØ¹ Ù�ÙˆÙ‚ Ø§Ù„ÙƒÙ„Ø¨ Ø§Ù„ÙƒØ³ÙˆÙ„");
//
//			// random arabic translation is "This is my random sentence that has absolutely
//			// no essential meaning."
//			builder.field("random_arabic", "ï»¿Ù‡Ø°Ù‡ Ø¬Ù…Ù„ØªÙŠ Ø§Ù„Ø¹Ø´ÙˆØ§Ø¦ÙŠØ© Ø§Ù„ØªÙŠ Ù„ÙŠØ³ Ù„Ù‡Ø§ Ø£ÙŠ Ù…Ø¹Ù†Ù‰ Ø¬ÙˆÙ‡Ø±ÙŠ Ø¹Ù„Ù‰ Ø§Ù„Ø¥Ø·Ù„Ø§Ù‚.");
//
//			builder.field("english", "english");
//			builder.field("unicode", "unicode");
//			builder.endObject();
//
//			IndexRequest request = new IndexRequest(indexName).source(builder);
//			IndexResponse response = client.index(request, RequestOptions.DEFAULT);
//			Result result = response.getResult();
//			log.info("Indexer result is: " + result.toString());
//
//		} catch (Exception e) {
//			throw e;
//		}
//	}

//	public static void indexTestDocument(ElasticsearchClient client, String indexName) throws Exception {
//		try {
//			IndexRequest.Builder<TDocument>.document("");
//			IndexRequest.Builder<TDocument> x = new Builder<>().
//			XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
//			builder.field("raw", "The quick brown fox jumped over the lazy dog");
//			builder.field("random_english", "A lazy white duck flew above the lake");
//			builder.field("arabic", "Ù‚Ù�Ø² Ø§Ù„Ø«Ø¹Ù„Ø¨ Ø§Ù„Ø¨Ù†ÙŠ Ø§Ù„Ø³Ø±ÙŠØ¹ Ù�ÙˆÙ‚ Ø§Ù„ÙƒÙ„Ø¨ Ø§Ù„ÙƒØ³ÙˆÙ„");
//
//			// random arabic translation is "This is my random sentence that has absolutely
//			// no essential meaning."
//			builder.field("random_arabic", "ï»¿Ù‡Ø°Ù‡ Ø¬Ù…Ù„ØªÙŠ Ø§Ù„Ø¹Ø´ÙˆØ§Ø¦ÙŠØ© Ø§Ù„ØªÙŠ Ù„ÙŠØ³ Ù„Ù‡Ø§ Ø£ÙŠ Ù…Ø¹Ù†Ù‰ Ø¬ÙˆÙ‡Ø±ÙŠ Ø¹Ù„Ù‰ Ø§Ù„Ø¥Ø·Ù„Ø§Ù‚.");
//
//			builder.field("english", "english");
//			builder.field("unicode", "unicode");
//			builder.endObject();
//
//			IndexRequest request = new IndexRequest(indexName).source(builder);
//			IndexResponse response = client.index(request, RequestOptions.DEFAULT);
//			Result result = response.getResult();
//			log.info("Indexer result is: " + result.toString());
//
//		} catch (Exception e) {
//			throw e;
//		}
//	}

}
