package elasticsesarch.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import util.ConfigUtils;
import util.FileUtils;
import util.JsonUtils;

public class BulkIndexerService {
	private static long total = 0;
	private static int bulkCounter = 0;
	private static Logger log = Logger.getLogger(BulkIndexerService.class);

	public static void indexDirectoryDocumentsWithJson(ElasticsearchClient client, String directory, String targetFileName, String indexName, String className, String run) throws Exception {
		if (!ConfigUtils.execute(run, "Bulk Index Service - Index documents for " + indexName)) {
			return;
		}
		int bulkLimit = ConfigUtils.getPropertyAsInt("es.bulk.index.size.limit");

		Set<String> files = new HashSet<>(FileUtils.getFileListFromDirectory(directory, targetFileName));

		files.forEach(file -> {
			BulkRequest.Builder br = new BulkRequest.Builder();
			bulkCounter = 0;

			try {
				List<String> documentList = FileUtils.readFileIntoList(directory, file);
				log.info("Indexing documents from file: " + file);

				for (String docString : documentList) {
					JsonObject jo = JsonUtils.stringToJsonObject(docString);
					ObjectMapper objectMapper = new ObjectMapper();
					Object o = objectMapper.readValue(jo.toString(), Class.forName(className));

					br.operations(op -> op/**/
							.index(idx -> idx.index(indexName).id(jo.get("id").getAsString()).document(o))/**/
					);

					++bulkCounter;
					if (bulkCounter > bulkLimit) {
						doBulkIndex(client, br);
						bulkCounter = 0;
						br = new BulkRequest.Builder();
					}
				}

				// final bulk index operation
				doBulkIndex(client, br);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				System.exit(99);
			}
		});
	}

	private static void doBulkIndex(ElasticsearchClient client, BulkRequest.Builder br) throws Exception {
		try {
			BulkResponse response = client.bulk(br.build());

			// Log errors, if any
			if (response.errors()) {
				log.error("Bulk had errors");
				for (BulkResponseItem item : response.items()) {
					if (item.error() != null) {
						log.error(item.error().reason());
					}
				}
			} else {
				total += bulkCounter;
				log.info("" + String.valueOf(total) + " documents have been indexed");
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
