package app;

import org.apache.log4j.Logger;

import elasticsesarch.service.BulkIndexerService;
import elasticsesarch.service.ClientService;
import elasticsesarch.service.IndicesService;
import service.IngestGamecastService;
import service.IngestGamestatService;
import service.IngestPlaybyplayService;
import service.IngestPlayerService;
import util.ConfigUtils;

public class ElasticsearchApp {

	private static Logger log = Logger.getLogger(ElasticsearchApp.class);

	// private static ElasticsearchClient client = null;

	public static void main(String[] args) {
		log.info("This is an implementation of Elasticsearch 7.17.0 Indexing Capabilities");
//		try {
//			FileUtils.combine();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		System.exit(0);

		// what a great day it is today
		try {
			IngestPlayerService.go(ConfigUtils.getProperty("ingest.run.player"));
			IngestGamecastService.go(ConfigUtils.getProperty("ingest.run.gamecast"));
			IngestGamestatService.go(ConfigUtils.getProperty("ingest.run.gamestat"));
			IngestPlaybyplayService.go(ConfigUtils.getProperty("ingest.run.playbyplay"));

			// gamecast index definition - this would be for dynamic index creation
			// IndicesService.defineGamecastIndexUsingAPI(ClientService.getESClient(),
			// ConfigUtils.getGamecastIndexName());

			// player index definition
			IndicesService.defineIndexUsingWithJson(ClientService.getESClient(), /**/
					ConfigUtils.getProperty("player.index.definition.file"), /**/
					ConfigUtils.getProperty("es.index.name.player"), /**/
					ConfigUtils.getProperty("index.define.player"));

			// gamecast index definition
			IndicesService.defineIndexUsingWithJson(ClientService.getESClient(), /**/
					ConfigUtils.getProperty("gamecast.index.definition.file"), /**/
					ConfigUtils.getProperty("es.index.name.gamecast"), /**/
					ConfigUtils.getProperty("index.define.gamecast"));

			// game stats index definition
			IndicesService.defineIndexUsingWithJson(ClientService.getESClient(), /**/
					ConfigUtils.getProperty("gamestat.index.definition.file"), /**/
					ConfigUtils.getProperty("es.index.name.gamestat"), /**/
					ConfigUtils.getProperty("index.define.gamestat"));

			// play-by-play index definition
			IndicesService.defineIndexUsingWithJson(ClientService.getESClient(), /**/
					ConfigUtils.getProperty("playbyplay.index.definition.file"), /**/
					ConfigUtils.getProperty("es.index.name.playbyplay"), /**/
					ConfigUtils.getProperty("index.define.playbyplay"));

//			AnalyzerService.analyzeGamecastField(ClientService.getESClient(), ConfigUtils.getGamecastIndexName(), "my_analyzer", "venueName", "Frank Erwin Center");
//			AnalyzerService.analyzeGamecastField(ClientService.getESClient(), ConfigUtils.getGamecastIndexName(), null, "gameId", 401368093);
//			AnalyzerService.analyzeGamecastField(ClientService.getESClient(), ConfigUtils.getGamecastIndexName(), null, "gameTimeUTC", "01:00");
//			AnalyzerService.analyzeGamecastField(ClientService.getESClient(), ConfigUtils.getGamecastIndexName(), null, "gameDay", 20220216);

			BulkIndexerService.indexDirectoryDocumentsWithJson(ClientService.getESClient(), /**/
					ConfigUtils.getProperty("directory.player.document"), /**/
					ConfigUtils.getProperty("player.document.json.file.name"), /**/
					ConfigUtils.getProperty("es.index.name.player"), /**/
					"vo.Player", /**/
					ConfigUtils.getProperty("documents.index.player")); /**/

			BulkIndexerService.indexDirectoryDocumentsWithJson(ClientService.getESClient(), /**/
					ConfigUtils.getProperty("directory.gamecast.document"), /**/
					ConfigUtils.getProperty("gamecast.document.json.file.name"), /**/
					ConfigUtils.getProperty("es.index.name.gamecast"), /**/
					"vo.Gamecast", /**/
					ConfigUtils.getProperty("documents.index.gamecast")); /**/

			BulkIndexerService.indexDirectoryDocumentsWithJson(ClientService.getESClient(), /**/
					ConfigUtils.getProperty("directory.gamestat.document"), /**/
					ConfigUtils.getProperty("gamestat.document.json.file.name"), /**/
					ConfigUtils.getProperty("es.index.name.gamestat"), /**/
					"vo.Gamestat", /**/
					ConfigUtils.getProperty("documents.index.gamestat")); /**/

			BulkIndexerService.indexDirectoryDocumentsWithJson(ClientService.getESClient(), /**/
					ConfigUtils.getProperty("directory.playbyplay.document"), /**/
					ConfigUtils.getProperty("playbyplay.document.json.file.name"), /**/
					ConfigUtils.getProperty("es.index.name.playbyplay"), /**/
					"vo.Playbyplay", /**/
					ConfigUtils.getProperty("documents.index.playbyplay")); /**/

//			QueryService.matchAllQuery(ClientService.getESClient(), ConfigUtils.getProperty("es.index.name.gamecast"));
//			QueryService.matchQuery(ClientService.getESClient(), ConfigUtils.getProperty("es.index.name.gamestat"), "playerLastName", "Bueckers");
//			QueryService.compoundQuery(ClientService.getESClient(), ConfigUtils.getProperty("es.index.name.player"), "Duke");
			// public static void sqlQuery(ElasticsearchClient client, String indexName,
			// String searchTerm) throws Exception {

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
