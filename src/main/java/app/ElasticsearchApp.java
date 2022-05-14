package app;

import org.apache.log4j.Logger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import elasticsesarch.service.ClientService;
import elasticsesarch.service.TireKickerService;

public class ElasticsearchApp {

	private static Logger log = Logger.getLogger(ElasticsearchApp.class);

	private static ElasticsearchClient client = null;

	public static void main(String[] args) {
		log.info("This is an implementation of Elasticsearch 7.17.0 Indexing Capabilities");

		// what a great day it is today
		try {
			client = ClientService.getESClient();

			new TireKickerService().go(client);

			// PipelineService.refreshPipelineProcessors(client);
			// PipelineService.simulatePipelineRequest(client, "multilingual");

			// IndicesService.refreshMultilingualIndex(client, "multilingual");
			// IndexerService.indexMulticulturalDocumentTest(client, "multicultural");

			// log.info("ENGLISH");
			// QueryService.testMultilingualMultiMatch(client, "home");
			// log.info("GERMAN");
			// QueryService.testMultilingualMultiMatch(client, "Hause");

			// IndicesService.deleteIndex(client, "multicultural");
			// IndicesService.deleteIndex(client, "proto");

			client.shutdown();
			log.info("DONE");
		} catch (Exception e) {
			client.shutdown();
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

}
