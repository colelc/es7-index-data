package elasticsesarch.service;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.xcontent.XContentBuilder;
import org.elasticsearch.xcontent.XContentFactory;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;

public class IndexerService {
	private static Logger log = Logger.getLogger(IndexerService.class);
	private static List<String> testContentList;

	static {
		testContentList = Arrays.asList(new String[] { /**/
				"I want to go home", /**/
				"Ich mÃ¶chte nach Hause gehen", /**/
				"Ø£Ø±ÙŠØ¯ Ø§Ù„Ø¹ÙˆØ¯Ø© Ø¥Ù„Ù‰ Ø¯ÙŠØ§Ø±Ù‡Ù…", /**/ // I want to go home
				"Ù‡Ø°Ù‡ Ø¬Ù…Ù„ØªÙŠ Ø§Ù„Ø¹Ø´ÙˆØ§Ø¦ÙŠØ© Ø§Ù„ØªÙŠ Ù„ÙŠØ³ Ù„Ù‡Ø§ Ø£ÙŠ Ù…Ø¹Ù†Ù‰ Ø¬ÙˆÙ‡Ø±ÙŠ Ø¹Ù„Ù‰ Ø§Ù„Ø¥Ø·Ù„Ø§Ù‚/**/" });
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

	public static void indexTestDocument(ElasticsearchClient client, String indexName) throws Exception {
		try {
			IndexRequest.Builder<TDocument>.document("");
			IndexRequest.Builder<TDocument> x = new Builder<>().
			XContentBuilder builder = XContentFactory.jsonBuilder().startObject();
			builder.field("raw", "The quick brown fox jumped over the lazy dog");
			builder.field("random_english", "A lazy white duck flew above the lake");
			builder.field("arabic", "Ù‚Ù�Ø² Ø§Ù„Ø«Ø¹Ù„Ø¨ Ø§Ù„Ø¨Ù†ÙŠ Ø§Ù„Ø³Ø±ÙŠØ¹ Ù�ÙˆÙ‚ Ø§Ù„ÙƒÙ„Ø¨ Ø§Ù„ÙƒØ³ÙˆÙ„");

			// random arabic translation is "This is my random sentence that has absolutely
			// no essential meaning."
			builder.field("random_arabic", "ï»¿Ù‡Ø°Ù‡ Ø¬Ù…Ù„ØªÙŠ Ø§Ù„Ø¹Ø´ÙˆØ§Ø¦ÙŠØ© Ø§Ù„ØªÙŠ Ù„ÙŠØ³ Ù„Ù‡Ø§ Ø£ÙŠ Ù…Ø¹Ù†Ù‰ Ø¬ÙˆÙ‡Ø±ÙŠ Ø¹Ù„Ù‰ Ø§Ù„Ø¥Ø·Ù„Ø§Ù‚.");

			builder.field("english", "english");
			builder.field("unicode", "unicode");
			builder.endObject();

			IndexRequest request = new IndexRequest(indexName).source(builder);
			IndexResponse response = client.index(request, RequestOptions.DEFAULT);
			Result result = response.getResult();
			log.info("Indexer result is: " + result.toString());

		} catch (Exception e) {
			throw e;
		}
	}

}
