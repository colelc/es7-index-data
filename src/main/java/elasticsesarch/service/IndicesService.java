package elasticsesarch.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.analysis.Analyzer;
import co.elastic.clients.elasticsearch._types.analysis.CustomAnalyzer;
import co.elastic.clients.elasticsearch._types.analysis.NGramTokenizer;
import co.elastic.clients.elasticsearch._types.analysis.StandardAnalyzer;
import co.elastic.clients.elasticsearch._types.analysis.StandardTokenizer;
import co.elastic.clients.elasticsearch._types.analysis.TokenChar;
import co.elastic.clients.elasticsearch._types.analysis.Tokenizer;
import co.elastic.clients.elasticsearch._types.analysis.TokenizerDefinition;
import co.elastic.clients.elasticsearch._types.mapping.BooleanProperty;
import co.elastic.clients.elasticsearch._types.mapping.DateProperty;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.FieldNamesField;
import co.elastic.clients.elasticsearch._types.mapping.IntegerNumberProperty;
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
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
import co.elastic.clients.elasticsearch.indices.GetMappingRequest;
import co.elastic.clients.elasticsearch.indices.GetMappingResponse;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.indices.IndexSettingsAnalysis;
import co.elastic.clients.elasticsearch.indices.get_alias.IndexAliases;
import co.elastic.clients.elasticsearch.indices.get_mapping.IndexMappingRecord;
import co.elastic.clients.transport.endpoints.BooleanResponse;

public class IndicesService {
	private static Logger log = Logger.getLogger(IndicesService.class);

	public static void defineGamecastIndex(ElasticsearchClient client, String indexName) throws Exception {
		try {
			IndicesService.deleteIndex(client, indexName);

			List<TokenChar> tokenChars = Arrays.asList(TokenChar.Digit, TokenChar.Letter);

			NGramTokenizer ngram = new NGramTokenizer.Builder()/**/
					.minGram(3)/**/
					.maxGram(3)/**/
					.tokenChars(tokenChars)/**/
					.customTokenChars("%")/**/
					.build();

			TokenizerDefinition ngramTokenizerDefinition = new TokenizerDefinition.Builder().ngram(ngram).build();
			Tokenizer ngramTokenizer = new Tokenizer.Builder().definition(ngramTokenizerDefinition).build();
			Map<String, Tokenizer> ngramTokenizerMap = new HashMap<String, Tokenizer>();
			ngramTokenizerMap.put("my_tokenizer", ngramTokenizer);

			CustomAnalyzer customAnalyzer = new CustomAnalyzer.Builder().tokenizer("my_tokenizer").build();
			Analyzer myAnalyzer = new Analyzer.Builder().custom(customAnalyzer).build();

			Map<String, Analyzer> analyzerMap = new HashMap<>();
			analyzerMap.put("my_analyzer", myAnalyzer);

			IndexSettingsAnalysis indexSettingsAnalysis = new IndexSettingsAnalysis.Builder()/**/
					.analyzer(analyzerMap)/**/
					.tokenizer(ngramTokenizerMap)/**/
					.build();

			IndexSettings indexSettings = new IndexSettings.Builder()/**/
					.maxInnerResultWindow(250)/**/
					.numberOfReplicas("1")/**/
					.numberOfShards("3")/**/
					.analysis(indexSettingsAnalysis)/**/
					.build();

			// multi-mapping - (using a field as both a keyword and as text)
			Property multiMappedProperty = getMultimappedProperty();
			Map<String, Property> propertyMap = new HashMap<>();

			// gamecast object - everything is multi-mapped
			TextProperty gamecastTextProperty = new TextProperty.Builder()/**/
					.index(Boolean.TRUE)/**/
					.fields("networkCoverage", multiMappedProperty)/**/
					.fields("networkCoverage", multiMappedProperty)/**/
					.fields("roadTeamConferenceShortName", multiMappedProperty)/**/
					.fields("roadTeamConferenceLongName", multiMappedProperty)/**/
					.fields("roadTeamName", multiMappedProperty)/**/
					.fields("homeTeamConferenceShortName", multiMappedProperty)/**/
					.fields("homeTeamConferenceLongName", multiMappedProperty)/**/
					.fields("homeTeamName", multiMappedProperty)/**/
					.fields("venueName", multiMappedProperty)/**/
					.fields("venueCity", multiMappedProperty)/**/
					.fields("venueState", multiMappedProperty)/**/
					.fields("status", multiMappedProperty)/**/
					.fields("referees", multiMappedProperty)/**/
					.fields("sourceFile", multiMappedProperty)/**/
					/**/
					.fields("gamePercentageFull", multiMappedProperty)/**/
					.fields("gameAttendance", new Property.Builder().integer(new IntegerNumberProperty.Builder().build()).build())/**/
					.fields("venueCapacity", new Property.Builder().integer(new IntegerNumberProperty.Builder().build()).build())/**/
					/**/
					.fields("gameTimeUTC", new Property.Builder().date(new DateProperty.Builder().format("hour_minute").build()).build())/**/
					.fields("gameDay", new Property.Builder().date(new DateProperty.Builder().format("basic_date").build()).build())/**/
					.build();
			propertyMap.put("Gamecast", new Property.Builder().text(gamecastTextProperty).build());

			// 'individual fields'
			propertyMap.put("gameId", new Property.Builder().keyword(new KeywordProperty.Builder().build()).build());

			propertyMap.put("roadTeamConferenceId", new Property.Builder().keyword(new KeywordProperty.Builder().build()).build());
			propertyMap.put("roadTeamId", new Property.Builder().keyword(new KeywordProperty.Builder().build()).build());
			propertyMap.put("homeTeamConferenceId", new Property.Builder().keyword(new KeywordProperty.Builder().build()).build());
			propertyMap.put("homeTeamId", new Property.Builder().keyword(new KeywordProperty.Builder().build()).build());
			propertyMap.put("roadTeamConferenceId", new Property.Builder().keyword(new KeywordProperty.Builder().build()).build());
			propertyMap.put("recordId", new Property.Builder().keyword(new KeywordProperty.Builder().build()).build());

			TypeMapping mapping = new TypeMapping.Builder()/**/
					// .dynamic(DynamicMapping.Strict)/**/
					// .dynamic(DynamicMapping.True)/**/
					.properties(propertyMap)/**/
					.build();

			CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder()/**/
					.settings(indexSettings)/**/
					.mappings(mapping)/**/
					.index(indexName)/**/
					// .withJson(is)/**/
					.build();

			CreateIndexResponse response = client.indices().create(createIndexRequest);
			log.info("The index \"" + response.index() + "\" " + "has been created");

			getIndexMapping(client, indexName);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * I want all fields to be multi-mapped for search via text or keyword
	 * 
	 * @return
	 */
	private static Property getMultimappedProperty() {

		KeywordProperty keywordProperty = new KeywordProperty.Builder().ignoreAbove(Integer.valueOf(256)).build();

		TextProperty textProperty = new TextProperty.Builder()/**/
				.fields("keyword", new Property.Builder().keyword(keywordProperty).build())/**/
				.build();

		Property property = new Property.Builder().text(textProperty).build();
		return property;
	}

	public static void defineIndexWithStandardAnalyzer(ElasticsearchClient client, String indexName) throws Exception {
		try {
			IndicesService.deleteIndex(client, indexName);

			Analyzer standardAnalyzer = new Analyzer.Builder()/**/
					.standard(new StandardAnalyzer.Builder().build())/**/
					.build();
			Map<String, Analyzer> analyzerMap = new HashMap<>();
			analyzerMap.put("analyzer", standardAnalyzer);

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
			log.info("The index \"" + response.index() + "\" " + "has been created");

			// getIndexMapping(client, indexName);

		} catch (Exception e) {
			throw e;
		}
	}

	public static void getIndexMapping(ElasticsearchClient client, String indexName) throws Exception {
		try {
			GetMappingRequest mappingsRequest = new GetMappingRequest.Builder()/**/
					.index(indexName)/**/
					.build();

			GetMappingResponse mappingResponse = client.indices().getMapping(mappingsRequest);
			Map<String, IndexMappingRecord> mapping = mappingResponse.result();
			mapping.forEach((ix, indexMappingRecord) -> {
				TypeMapping map = indexMappingRecord.mappings();
				FieldNamesField fieldNames = map.fieldNames();
			});

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
