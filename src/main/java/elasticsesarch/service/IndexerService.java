package elasticsesarch.service;

import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import data.JsonData;
import util.ConfigUtils;
import util.FileUtils;
import util.JsonUtils;

public class IndexerService {
	private static Logger log = Logger.getLogger(IndexerService.class);

	/**
	 * only a single document per file ! not the best way
	 * 
	 * @param client
	 * @param directory
	 * @param targetFileName
	 * @param indexName
	 * @throws Exception
	 */
	public static void indexDocumentWithJson(ElasticsearchClient client, String directory, String targetFileName, String indexName, String run) throws Exception {

		if (!ConfigUtils.execute(run, "Indexer Service - Index documents for " + indexName)) {
			return;
		}

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

	public static void indexDirectoryDocumentsWithJson(ElasticsearchClient client, String directory, String targetFileName, String indexName, String className, String run) throws Exception {

		if (!ConfigUtils.execute(run, "Indexer Service - Index documents for " + indexName)) {
			return;
		}

		Set<String> files = new HashSet<>(FileUtils.getFileListFromDirectory(directory, targetFileName));

		files.forEach(file -> {
			try {
				List<String> documentList = FileUtils.readFileIntoList(directory, file);

				for (String docString : documentList) {
					JsonObject jo = JsonUtils.stringToJsonObject(docString);
					ObjectMapper objectMapper = new ObjectMapper();

					// Player player = objectMapper.readValue(jo.toString(), Player.class);
					Object o = objectMapper.readValue(jo.toString(), Class.forName(className));

					// IndexRequest<Player> request;
					IndexRequest<Object> request;

					request = IndexRequest.of(b -> b/**/
							.index(indexName)/**/
							.id(jo.get("id").getAsString())/**/
							// .document(player));/**/
							.document(o));

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
