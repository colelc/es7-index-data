package elasticsesarch.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.analysis.Analyzer;
import co.elastic.clients.elasticsearch._types.analysis.StandardAnalyzer;
import co.elastic.clients.elasticsearch._types.analysis.StandardTokenizer;
import co.elastic.clients.elasticsearch._types.analysis.Tokenizer;
import co.elastic.clients.elasticsearch._types.analysis.TokenizerDefinition;
import co.elastic.clients.elasticsearch._types.mapping.BooleanProperty;
import co.elastic.clients.elasticsearch._types.mapping.DateProperty;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.ElasticsearchIndicesClient;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.elasticsearch.indices.GetAliasResponse;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.indices.IndexSettingsAnalysis;
import co.elastic.clients.elasticsearch.indices.get_alias.IndexAliases;
import co.elastic.clients.transport.endpoints.BooleanResponse;

public class IndicesService {
	private static Logger log = Logger.getLogger(IndicesService.class);
	private static final String TIRE_KICK = "Tire Kick: ";

//	public static void refreshMultilingualIndex(ElasticsearchClient client, String indexName) throws Exception {
//		// NOTE: IMPORTANT IMPORTANT IMPORTANT IMPORTANT
//
//		// The mapping for this multilingual index requires the ICU Analyzer plugin.
//		// To install this plugin:
//		// From the Elasticsearch bin directory:
//		// Enter the command:
//		// ./elasticsearch-plugin install analysis-icu
//		// And then of course: recycle the ES instance.
//
//		// 1st we send a CreateIndexRequest
//		// 2nd we send a PutMappingRequest
//		try {
//			deleteIndex(client, indexName);
//
//			Builder settings = Settings.builder()/**/
//					.put("index.number_of_shards", 1)/**/
//					.put("index.number_of_replicas", 0);/**/
//
//			CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName).settings(settings);
//			CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//			log.info("The index \"" + createIndexResponse.index() + "\" " + "has been created");
//
//			PutMappingRequest putMappingRequest = new PutMappingRequest(indexName).source(MappingService.getMulticulturalMapping());
//			AcknowledgedResponse putMappingResponse = client.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
//
//			if (putMappingResponse.isAcknowledged()) {
//				log.info("The mapping for index \"" + indexName + "\" " + "has been created");
//				// getMappings(client, indexName);
//			}
//
//		} catch (Exception e) {
//			client.shutdown();
//			throw e;
//		}
//	}

	public static void defineIndexWithStandardAnalyzer(ElasticsearchClient client, String indexName) throws Exception {
		try {
			IndicesService.deleteIndex(client, indexName);

			// XContentBuilder builder = AnalyzerService.getArabicAnalyzer();
			// InputStream is =
			// IndicesService.class.getClassLoader().getResourceAsStream("/json/arabic_analyzer.json");
			// InputStream is = IndicesService.class.getResourceAsStream("/json/test.json");

			// LanguageAnalyzer.Builder test = new LanguageAnalyzer.Builder();
			// Object x = test.language(new
			// LanguageAnalyzer.Builder().language(Language.Arabic).build());
			// Analyzer._kind().Language;

//			LanguageAnalyzer arabic = new LanguageAnalyzer.Builder()/**/
//					.language(Language.Arabic)/**/
//					.stemExclusion(new ArrayList<String>())/**/
//					.build();
//
//			Analyzer analyzer = new Analyzer.Builder()/**/
//					.language(arabic)/**/
//					.build();

			Analyzer standardAnalyzer = new Analyzer.Builder()/**/
					.standard(new StandardAnalyzer.Builder().build())/**/
					.build();
			Map<String, Analyzer> analyzerMap = new HashMap<>();
			analyzerMap.put("analyzer", standardAnalyzer);

			// analyzerMap.put("arabic_stemmer", new
			// Analyzer.Builder().language(arabic).build());
			// analyzerMap.put("tokenizer", new Analyzer.Builder().standard(new
			// StandardAnalyzer.Builder().build()).build());
			// analyzerMap.put("what", analyzer);

			TokenizerDefinition td = new TokenizerDefinition.Builder().standard(new StandardTokenizer.Builder().build()).build();
			Tokenizer t = new Tokenizer.Builder().definition(td).build();
			Map<String, Tokenizer> tokenizerMap = new HashMap<String, Tokenizer>();
			tokenizerMap.put("tokenizer", t);

			IndexSettingsAnalysis indexSettingsAnalysis = new IndexSettingsAnalysis.Builder()/**/
					.analyzer(analyzerMap)/**/
					.tokenizer(tokenizerMap)/**/
					.build();

			IndexSettings indexSettings = new IndexSettings.Builder()/**/
					.maxInnerResultWindow(250)/**/
					.numberOfReplicas("1")/**/
					.numberOfShards("3")/**/
					.analysis(indexSettingsAnalysis)/**/
					.build();

			TextProperty textProperty = new TextProperty.Builder()/**/
					.index(Boolean.TRUE)/**/
					.fields("first-name", new Property.Builder().text(new TextProperty.Builder().build()).build())/**/
					.fields("last-name", new Property.Builder().text(new TextProperty.Builder().build()).build())/**/
					.fields("us-person", new Property.Builder().boolean_(new BooleanProperty.Builder().build()).build())/**/
					.build();

			Property property = new Property.Builder().text(textProperty).build();

			Map<String, Property> propertyMap = new HashMap<>();

			// object field
			propertyMap.put("person", property);

			// 'individual fields'
			propertyMap.put("my-date-field", new Property.Builder().date(new DateProperty.Builder().format("yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis").build()).build());
			propertyMap.put("my-text-field", new Property.Builder().text(new TextProperty.Builder().build()).build());

			TypeMapping mapping = new TypeMapping.Builder()/**/
					.dynamic(DynamicMapping.Strict)/**/
					.properties(propertyMap)/**/
					.build();

			CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder()/**/
					.settings(indexSettings)/**/
					.mappings(mapping)/**/
					.index(indexName)/**/
					// .withJson(is)/**/
					.build();

			CreateIndexResponse response = client.indices().create(createIndexRequest);
			log.info(TIRE_KICK + "The index \"" + response.index() + "\" " + "has been created");

		} catch (Exception e) {
			throw e;
		}
	}

	public static void indicesClientCheckout(ElasticsearchClient client) throws Exception {
		try {
			getListOfAliasesFromIndicesClient(client);
		} catch (Exception e) {
			throw e;
		}
	}

	public static void getListOfAliasesFromIndicesClient(ElasticsearchClient client) throws Exception {

		try {
			ElasticsearchIndicesClient indicesClient = client.indices();
			GetAliasResponse aliasResponse = indicesClient.getAlias();

			Map<String, IndexAliases> map = aliasResponse.result();
			map.forEach((key, aliasDefinition) -> {
				aliasDefinition.aliases().forEach((alias, defn) -> {
					log.info(key + " -> " + alias + " -> " + defn.toString());
				});
			});

		} catch (Exception e) {
			throw e;
		}

	}

	public static void deleteIndex(ElasticsearchClient client, String indexName) throws Exception {
		try {
			ExistsRequest.Builder builder = new ExistsRequest.Builder();
			builder.allowNoIndices(true);
			// builder.ignoreUnavailable(true); -> this must be false (apparently) or I get
			// 'true' for my indexName even if it doesn't exist
			builder.includeDefaults(true);
			builder.index(Collections.singletonList(indexName));

			BooleanResponse booleanResponse = client.indices().exists(builder.build());

			if (booleanResponse.value()) {
				log.info("Deleting index: " + indexName);
				DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index(Collections.singletonList(indexName)).build();
				DeleteIndexResponse deleteIndexResponse = client.indices().delete(deleteIndexRequest);

				if (deleteIndexResponse.acknowledged()) {
					log.info("Index \"" + indexName + "\" has been deleted");
				} else {
					log.info("Index \"" + indexName + "\" does not exist, therefore it cannot be deleted");
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

}
