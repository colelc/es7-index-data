package app;

import org.apache.log4j.Logger;

import elasticsesarch.service.ClientService;
import service.IngestPlaybyplayService;

public class ElasticsearchApp {

	private static Logger log = Logger.getLogger(ElasticsearchApp.class);

	// private static ElasticsearchClient client = null;

	public static void main(String[] args) {
		log.info("This is an implementation of Elasticsearch 7.17.0 Indexing Capabilities");

		// what a great day it is today
		try {
			// new TireKickerService().go(ClientService.getESClient());
			// IngestPlayerService.go();
			// IngestGamecastService.go();
			IngestPlaybyplayService.go();

			// gamecast index
			// IndicesService.defineGamecastIndexUsingAPI(ClientService.getESClient(),
			// ConfigUtils.getGamecastIndexName());
//			IndicesService.defineIndexUsingWithJson(ClientService.getESClient(), ConfigUtils.getGamecastIndexDefinitionFile(), ConfigUtils.getGamecastIndexName());
			// player index
//			IndicesService.defineIndexUsingWithJson(ClientService.getESClient(), /**/
//					ConfigUtils.getProperty("player.index.definition.file"), /**/
//					ConfigUtils.getProperty("es.index.name.player"));

//			AnalyzerService.analyzeGamecastField(ClientService.getESClient(), ConfigUtils.getGamecastIndexName(), "my_analyzer", "venueName", "Frank Erwin Center");
//			AnalyzerService.analyzeGamecastField(ClientService.getESClient(), ConfigUtils.getGamecastIndexName(), null, "gameId", 401368093);
//			AnalyzerService.analyzeGamecastField(ClientService.getESClient(), ConfigUtils.getGamecastIndexName(), null, "gameTimeUTC", "01:00");
//			AnalyzerService.analyzeGamecastField(ClientService.getESClient(), ConfigUtils.getGamecastIndexName(), null, "gameDay", 20220216);
			// PipelineService.refreshPipelineProcessors(client);
			// PipelineService.simulatePipelineRequest(ClientService.getESClient(),
			// "multilingual");

//			IndexerService.indexDirectoryDocumentsWithJson(ClientService.getESClient(), /**/
//					ConfigUtils.getProperty("directory.player.document"), /**/
//					ConfigUtils.getProperty("player.document.json.file.name"), /**/
//					ConfigUtils.getProperty("es.index.name.player")); /**/

//			IndexerService.indexGamecastDataWithObjectMapper(ClientService.getESClient(), ConfigUtils.getGamecastIndexName());
//			IndexerService.indexDocumentWithJson(ClientService.getESClient(), /**/
//					ConfigUtils.getProperty("directory.gamecast.document"), /**/
//					ConfigUtils.getProperty(ConfigUtils.getProperty("gamecast.document.json.file.name")), /**/
//					ConfigUtils.getGamecastIndexName());

//			QueryService.matchAllQuery(ClientService.getESClient(), ConfigUtils.getGamecastIndexName());
//			QueryService.matchQuery(ClientService.getESClient(), ConfigUtils.getGamecastIndexName(), "roadTeamName", "Duke");
//			QueryService.compoundQuery(ClientService.getESClient(), ConfigUtils.getGamecastIndexName(), "Duke");
			// log.info("ENGLISH");
			// QueryService.testMultilingualMultiMatch(ClientService.getESClient(), "home");
			// log.info("GERMAN");
			// QueryService.testMultilingualMultiMatch(ClientService.getESClient(),
			// "Hause");

			// IndicesService.deleteIndex(ClientService.getESClient(), "multicultural");
			// IndicesService.deleteIndex(ClientService.getESClient(), "proto");

			ClientService.shutdownClient();

			log.info("DONE");
			System.exit(0);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			System.exit(99);
		}
	}

}
