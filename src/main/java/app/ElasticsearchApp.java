package app;

import org.apache.log4j.Logger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import elasticsesarch.service.ClientService;
import elasticsesarch.service.IndexerService;
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
			// new IndicesService();
			// IndicesService.defineGamecastIndexUsingAPI(client,
			// ConfigUtils.getGamecastIndexName());
//			IndicesService.defineGamecastIndexUsingWithJson(client, ConfigUtils.getGamecastIndexDefinitionFile(), ConfigUtils.getGamecastIndexName());

//			AnalyzerService.analyzeGamecastField(client, ConfigUtils.getGamecastIndexName(), "my_analyzer", "venueName", "Frank Erwin Center");
//			AnalyzerService.analyzeGamecastField(client, ConfigUtils.getGamecastIndexName(), null, "gameId", 401368093);
//			AnalyzerService.analyzeGamecastField(client, ConfigUtils.getGamecastIndexName(), null, "gameTimeUTC", "01:00");
//			AnalyzerService.analyzeGamecastField(client, ConfigUtils.getGamecastIndexName(), null, "gameDay", 20220216);
			// PipelineService.refreshPipelineProcessors(client);
			// PipelineService.simulatePipelineRequest(client, "multilingual");

//			IndexerService.indexGamecastDataWithObjectMapper(client, ConfigUtils.getGamecastIndexName());
			IndexerService.indexGamecastDataWithJson(client, /**/
					ConfigUtils.getProperty("directory.gamecast.document"), /**/
					ConfigUtils.getGamecastIndexName());

//			QueryService.matchAllQuery(client, ConfigUtils.getGamecastIndexName());
//			QueryService.matchQuery(client, ConfigUtils.getGamecastIndexName(), "roadTeamName", "Duke");
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
