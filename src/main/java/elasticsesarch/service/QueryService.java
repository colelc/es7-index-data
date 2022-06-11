package elasticsesarch.service;

import org.apache.log4j.Logger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import vo.Gamecast;

public class QueryService {
	private static Logger log = Logger.getLogger(QueryService.class);

//	public static void testMatchPhraseQuery(RestHighLevelClient client, String searchTerm) throws Exception {
//		try {
//			SearchRequest request = new SearchRequest();
//			SearchSourceBuilder builder = new SearchSourceBuilder();
//
//			builder.query(QueryBuilders.matchPhraseQuery("contents.en", searchTerm));
//
//			request.source(builder);
//
//			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//			listResponse(response);
//
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	public static void testQueryService(RestHighLevelClient client, String searchTerm) throws Exception {
//		try {
//			testMatchAll(client);
//			testMatch(client, searchTerm);
//			testMultiMatch(client, searchTerm);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	public static void testMultiMatch(RestHighLevelClient client, String searchTerm) throws Exception {
//		// multi-match query will search across all specified fields
//		// Here, I use the "*" wildcard to indicate 'all fields'
//		try {
//			// without param passed in, this request will be run against all
//			// indices
//			SearchRequest request = new SearchRequest();
//			SearchSourceBuilder builder = new SearchSourceBuilder();
//			// builder.query(QueryBuilders.multiMatchQuery(searchTerm, "*"));
//			// builder.query(QueryBuilders.multiMatchQuery("english", "*"));
//
//			// builder.query(QueryBuilders.multiMatchQuery("الكلب", "*")); //
//			// dog
//			// log.info("multiMatchQuery(\"الكلب\", \"*\"");
//
//			builder.query(QueryBuilders.multiMatchQuery("الإطلاق.", "*"));
//
//			request.source(builder);
//
//			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//			listResponse(response);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	public static void testMultilingualMultiMatch(RestHighLevelClient client, String searchTerm) throws Exception {
//
//		try {
//			// this request will be run against all fields in all indices
//			SearchSourceBuilder builder = new SearchSourceBuilder();
//			// builder.query(QueryBuilders.multiMatchQuery(searchTerm, "contents.en",
//			// "contents.de", "contents.ar"));
//			builder.query(QueryBuilders.multiMatchQuery(searchTerm, languages));
//
//			SearchRequest request = new SearchRequest();
//			request.source(builder);
//
//			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//			listResponse(response);
//		} catch (Exception e) {
//			throw e;
//		}
//	}
//
//	private static void listResponse(SearchResponse response) {
//		SearchHits hits = response.getHits();
//		TotalHits totalHits = hits.getTotalHits();
//		if (totalHits.value == 0) {
//			log.info("0 documents returned.");
//			return;
//		}
//
//		log.info(String.valueOf(totalHits.value) + " documents were returned.");
//		SearchHit[] sh = hits.getHits();
//		for (SearchHit h : sh) {
//			Map<String, Object> map = h.getSourceAsMap();
//			map.forEach((k, v) -> log.info(k + " -> " + String.valueOf(v)));
//		}
//
//	}
//
//	public static <SearchSourceBuilder> void matchQuery(ElasticsearchClient client, String searchTerm) throws Exception {
//		try {
//			SortOptions sortOptions = new SortOptions.Builder().field(new FieldSort.Builder().field("gameDay").build()).build();
//			Query query = new Query.Builder().matchAll(QueryBuilders.matchAll().build()).build();
//			SearchRequest searchRequest = new SearchRequest.Builder()/**/
//					.query(query)/**/
//					.sort(sortOptions)/**/
//					.build();
//			///////////////////////////////////////////////////////////////////////////////
//			// without param passed in, this request will be run against all
//			// indices
//			SearchRequest request = new SearchRequest();
//			SearchSourceBuilder builder = new SearchSourceBuilder();
//			builder.query(QueryBuilders.matchQuery("arabic", searchTerm));
//			request.source(builder);
//			log.info("matchQuery(\"arabic\", " + searchTerm);
//
//			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//			SearchHits hits = response.getHits();
//			TotalHits totalHits = hits.getTotalHits();
//			if (totalHits.value == 0) {
//				log.info("0 documents returned from match query.");
//				return;
//			}
//
//			log.info(String.valueOf(totalHits.value) + " documents were returned from match query.");
//			SearchHit[] sh = hits.getHits();
//			for (SearchHit h : sh) {
//				Map<String, Object> map = h.getSourceAsMap();
//				map.forEach((k, v) -> log.info(k + " -> " + String.valueOf(v)));
//			}
//
//		} catch (Exception e) {
//			throw e;
//		}
//	}

	public static void matchAllQuery(ElasticsearchClient client, String indexName) throws Exception {
		try {
			// interesting that if I omit the ".index(indexName)" the returned result set
			// remains the same as when I include it

			// for sort options:
			SortOptions sortOptions = new SortOptions.Builder().field(f -> f.field("roadTeamName.keyword").order(SortOrder.Asc)).build();
			Query query = new Query.Builder().matchAll(QueryBuilders.matchAll().build()).build();
			SearchRequest searchRequest = new SearchRequest.Builder()/**/
					.index(indexName)/**/
					.query(query)/**/
					.size(Integer.valueOf(9999))/**/
					.sort(sortOptions)/**/
					.build();

			SearchResponse<Gamecast> searchResponse = client.search(searchRequest, Gamecast.class);
			for (Hit<Gamecast> hit : searchResponse.hits().hits()) {
				Gamecast gc = hit.source();
				log.info(gc.getRoadTeamName() + " at " + gc.getHomeTeamName() + " on " + gc.getGameDay());
			}

		} catch (Exception e) {
			throw e;
		}
	}

}
