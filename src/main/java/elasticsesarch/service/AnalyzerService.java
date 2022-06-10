package elasticsesarch.service;

import java.util.List;

import org.apache.log4j.Logger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;

public class AnalyzerService {
	private static Logger log = Logger.getLogger(AnalyzerService.class);

	public static void analyzeGamecastField(ElasticsearchClient client, String indexName, String analyzer, String fieldName, Object text) throws Exception {
		AnalyzeRequest request = null;

		try {
			if (analyzer != null) {
				log.info("ANALYZING: " + indexName + " " + analyzer + " " + fieldName + "=" + String.valueOf(text));

				request = new AnalyzeRequest.Builder()/**/
						.index(indexName)/**/
						.field(fieldName)/**/
						.text(String.valueOf(text))/**/
						.explain(Boolean.FALSE)/**/
						.analyzer(analyzer)/**/
						.build();
			} else {
				log.info("ANALYZING: " + indexName + " " + fieldName + "=" + String.valueOf(text));

				request = new AnalyzeRequest.Builder()/**/
						.index(indexName)/**/
						.field(fieldName)/**/
						.text(String.valueOf(text))/**/
						.explain(Boolean.FALSE)/**/
						// .analyzer(analyzer)/**/
						.build();
			}
			AnalyzeResponse response = client.indices().analyze(request);

			List<AnalyzeToken> tokens = response.tokens();
			for (AnalyzeToken token : tokens) {
				log.info(token.type() + " -> " + token.token());
			}
		} catch (Exception e) {
			throw e;
		}

	}

}
