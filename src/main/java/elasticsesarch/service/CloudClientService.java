package elasticsesarch.service;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import util.ConfigUtils;

public class CloudClientService {

	private static Logger log = Logger.getLogger(CloudClientService.class);

	private static ElasticsearchClient client = null;

	public static ElasticsearchClient getESClient() throws Exception {
		if (client == null) {
			try {
				// final String cloudId = ConfigUtils.getProperty("es.cloud.id");
				final String cloudUser = ConfigUtils.getProperty("es.cloud.user");
				final String cloudPassword = ConfigUtils.getProperty("es.cloud.password");
				final String cloudHttps = ConfigUtils.getProperty("es.cloud.https");
				// final String cloudPort = ConfigUtils.getProperty("es.cloud.port");
				log.info("Establishing Elastic Cloud 7.17 connection");

				// all of this extra stuff for objectMapper is not needed, per se,
				// but included here
				ObjectMapper objectMapper = new ObjectMapper();/**/
//						.registerModule(new JavaTimeModule())/**/
//						.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)/**/
//						.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)/**/
//						.setSerializationInclusion(JsonInclude.Include.NON_NULL);

				RestClientBuilder builder = RestClient.builder(HttpHost.create(cloudHttps));

				final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
				credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(cloudUser, cloudPassword));

				builder.setHttpClientConfigCallback(b -> b.setDefaultCredentialsProvider(credentialsProvider));

				// RestClient restClient = builder.build();
				RestClient restClient = builder // RestClient.builder(new HttpHost(host, port))/**/
						.setRequestConfigCallback(/**/
								new RestClientBuilder.RequestConfigCallback() {
									@Override
									public Builder customizeRequestConfig(Builder requestConfigBuilder) {
										return requestConfigBuilder.setConnectTimeout(30000).setSocketTimeout(60000);
									}
								})/**/
						.build();

				ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper(objectMapper));

				// And create the API client
				client = new ElasticsearchClient(transport);

				log.info("ElasticsearchClient acquired: " + cloudHttps);
			} catch (Exception e) {
				throw e;
			}
		}

		return client;
	}

	public static void shutdownClient() throws Exception {
		try {
			if (client != null) {
				client.shutdown();
			}
		} catch (Exception e) {
			throw e;
		}
	}

//	private static RestHighLevelClient client = null;
//
//	public static RestHighLevelClient getESClient() throws Exception {
//		if (client == null) {
//			try {
//				String host = ConfigUtils.getProperty("es.host");
//				int port = ConfigUtils.getPropertyAsInt("es.port");
//
//				log.info("Acquiring Elasticsearch RestClient");
//				client = new RestHighLevelClient(RestClient.builder(new HttpHost(host, port, "http")));
//				log.info("Elasticsearch RestClient acquired: " + host + ":" + port);
//			} catch (Exception e) {
//				throw e;
//			}
//		}
//		
//
//		return client;
//	}
}
