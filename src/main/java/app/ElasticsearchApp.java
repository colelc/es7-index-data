package app;

import org.apache.log4j.Logger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import elasticsesarch.service.ClientService;
import elasticsesarch.service.QueryService;
import util.ConfigUtils;

public class ElasticsearchApp {

	private static Logger log = Logger.getLogger(ElasticsearchApp.class);

	private static ElasticsearchClient client = null;

	public static void main(String[] args) {
		log.info("This is an implementation of Elasticsearch 7.17.0 Indexing Capabilities");

		// what a great day it is today
		try {
			client = ClientService.getESClient();

			// new TireKickerService().go(client);
//			RawDataIngestService.go();
			// IndicesService.defineGamecastIndex(client,
			// ConfigUtils.getProperty("es.index.name.gamecast"));

//			AnalyzerService.analyzeGamecastField(client, ConfigUtils.getProperty("es.index.name.gamecast"), "my_analyzer", "venueName", "Frank Erwin Center");
//			AnalyzerService.analyzeGamecastField(client, ConfigUtils.getProperty("es.index.name.gamecast"), null, "gameId", 401368093);
//			AnalyzerService.analyzeGamecastField(client, ConfigUtils.getProperty("es.index.name.gamecast"), null, "gameTimeUTC", "01:00");
//			AnalyzerService.analyzeGamecastField(client, ConfigUtils.getProperty("es.index.name.gamecast"), null, "gameDay", 20220216);
			// PipelineService.refreshPipelineProcessors(client);
			// PipelineService.simulatePipelineRequest(client, "multilingual");

			// IndicesService.refreshMultilingualIndex(client, "multilingual");
			// IndexerService.indexMulticulturalDocumentTest(client, "multicultural");
//			IndexerService.indexGamecastData(client, ConfigUtils.getProperty("es.index.name.gamecast"));

			QueryService.matchAllQuery(client, ConfigUtils.getProperty("es.index.name.gamecast"));
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
