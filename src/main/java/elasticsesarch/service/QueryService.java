package elasticsesarch.service;

import org.apache.log4j.Logger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import jakarta.json.JsonObject;

public class QueryService {
	private static Logger log = Logger.getLogger(QueryService.class);

	public static void compoundQuery(ElasticsearchClient client, String indexName, String searchTerm) throws Exception {
		int attendanceThreshold = 5000;

		try {
			Query teamNameQuery = MatchQuery.of(m -> m.field("roadTeamName").query(searchTerm))._toQuery();

			Query attendanceQuery = RangeQuery.of(r -> r.field("gameAttendance").gte(JsonData.of(attendanceThreshold)))._toQuery();

			SearchResponse<JsonData> response = client.search(s -> s.index(indexName)/**/
					.query(q -> q/**/
							.bool(b -> b/**/.must(teamNameQuery).must(attendanceQuery)/**/
							)/**/
					)/**/
					, JsonData.class);

			for (Hit<JsonData> hit : response.hits().hits()) {
				JsonObject document = hit.source().toJson().asJsonObject();
				log.info("Attendance: " + document.get("gameAttendance") + "  " + document.get("roadTeamName") + " -> " + document.toString());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static void multiMatch(ElasticsearchClient client, String searchTerm) throws Exception {
		// multi-match query will search across all specified fields
		// Here, I use the "*" wildcard to indicate 'all fields'
		try {
			// without param passed in, this request will be run against all
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
		} catch (Exception e) {
			throw e;
		}
	}

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
	public static <SearchSourceBuilder> void matchQuery(ElasticsearchClient client, String indexName, String searchField, String searchTerm) throws Exception {
		try {
			// sort is not working
			SortOptions sortOptions = new SortOptions.Builder().field(f -> f.field("Gamecast.gameAttendance").order(SortOrder.Asc)).build();

			FieldValue fieldValue = new FieldValue.Builder().stringValue(searchTerm).build();

			Query query = new Query.Builder().match(f -> f.field(searchField).query(fieldValue)).build();

			SearchRequest searchRequest = new SearchRequest.Builder()/**/
					.index(indexName)/**/
					.query(query)/**/
					.size(Integer.valueOf(9999))/**/
					.sort(sortOptions)/**/
					.build();

			SearchResponse<JsonData> searchResponse = client.search(searchRequest, JsonData.class);
			for (Hit<JsonData> hit : searchResponse.hits().hits()) {
				JsonObject document = hit.source().toJson().asJsonObject();
				log.info("Attendance: " + document.get("gameAttendance") + " -> " + document.toString());
			}

		} catch (Exception e) {
			throw e;
		}
	}

	public static void matchAllQuery(ElasticsearchClient client, String indexName) throws Exception {
		try {
			// interesting that if I omit the ".index(indexName)" the returned result set
			// remains the same as when I include it

			// for sort options: can't sort on a text field - have to use the keyword
			// this is not working
			SortOptions sortOptions = new SortOptions.Builder().field(f -> f.field("Gamecast.gameAttendance.keyword").missing("0").order(SortOrder.Asc)).build();

			Query query = new Query.Builder().matchAll(QueryBuilders.matchAll().build()).build();
			SearchRequest searchRequest = new SearchRequest.Builder()/**/
					.index(indexName)/**/
					.query(query)/**/
					.size(Integer.valueOf(9999))/**/
					.sort(sortOptions)/**/
					.build();

			SearchResponse<JsonData> searchResponse = client.search(searchRequest, JsonData.class);
			for (Hit<JsonData> hit : searchResponse.hits().hits()) {
				JsonObject document = hit.source().toJson().asJsonObject();
				log.info("Attendance: " + document.get("gameAttendance") + " -> " + document.toString());
			}

		} catch (Exception e) {
			throw e;
		}
	}

}
