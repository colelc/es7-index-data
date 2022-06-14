package elasticsesarch.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import util.FileUtils;
import util.JsonUtils;
import vo.Gamecast;
import vo.Player;

public class IndexerService {
	private static Logger log = Logger.getLogger(IndexerService.class);

	public static void indexGamecastDataWithObjectMapper(ElasticsearchClient client, String indexName) throws Exception {

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
						;// log.info(response.result().toString() + " " + response.id() + " -> " +
							// response.index() + " ");
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

	/**
	 * only a single document per file ! not the best way
	 * 
	 * @param client
	 * @param directory
	 * @param targetFileName
	 * @param indexName
	 * @throws Exception
	 */
	public static void indexDocumentWithJson(ElasticsearchClient client, String directory, String targetFileName, String indexName) throws Exception {
		Set<String> files = new HashSet<>(FileUtils.getFileListFromDirectory(directory, targetFileName));

		files.forEach(file -> {
			try (FileReader f = new FileReader(new File(directory, file))) {
				IndexRequest<JsonData> request;

				request = IndexRequest.of(b -> b.index(indexName).withJson(f));

				IndexResponse response = client.index(request);

				if (response.result().compareTo(Result.Created) == 0) {
					;// log.info(response.result().toString() + " " + response.id() + " -> " +
						// response.index() + " ");
				} else {
					log.info("ERROR: " + response.result().toString());
				}

			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				System.exit(99);
			}
		});
	}

	public static void indexDirectoryDocumentsWithJson(ElasticsearchClient client, String directory, String targetFileName, String indexName) throws Exception {
		Set<String> files = new HashSet<>(FileUtils.getFileListFromDirectory(directory, targetFileName));

		files.forEach(file -> {
			try {
				List<String> documentList = FileUtils.readFileIntoList(directory, file);

				for (String docString : documentList) {
					JsonObject jo = JsonUtils.stringToJsonObject(docString);
					ObjectMapper objectMapper = new ObjectMapper();

					Player player = objectMapper.readValue(jo.toString(), Player.class);

					IndexRequest<Player> request;

					request = IndexRequest.of(b -> b/**/
							.index(indexName)/**/
							.id(jo.get("id").getAsString())/**/
							.document(player));

					IndexResponse response = client.index(request);

					if (response.result().compareTo(Result.Created) == 0) {
						;// log.info(response.result().toString() + " " + response.id() + " -> " +
							// response.index() + " ");
					} else {
						log.info("ERROR: " + response.result().toString());
					}

				}

			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				System.exit(99);
			}
		});
	}

}
