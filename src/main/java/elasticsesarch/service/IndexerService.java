package elasticsesarch.service;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

		JsonData.getJsonArray().forEach(jsonElement -> {
			if (jsonElement.isJsonObject()) {
				JsonObject jsonObject = jsonElement.getAsJsonObject();

//					jsonObject.keySet().forEach(key -> {
//						log.info(key + " -> " + jsonObject.get(key));
//					});

				try {
					ObjectMapper objectMapper = new ObjectMapper();
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

	}
}
