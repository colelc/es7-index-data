package elasticsesarch.service;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.log4j.Logger;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import util.ConfigUtils;

public class ClientService {

	private static Logger log = Logger.getLogger(ClientService.class);

	private static ElasticsearchClient client = null;

	public static ElasticsearchClient getESClient() throws Exception {
		if (client == null) {
			try {
				String host = ConfigUtils.getProperty("es.host");
				int port = ConfigUtils.getPropertyAsInt("es.port");

				log.info("Acquiring Java ElasticsearchClient");

				// set timeout values
//				RequestConfig requestConfig = RequestConfig.custom()/**/
//						.setConnectTimeout(10000)/**/
//						.setSocketTimeout(60000)/**/
//						.build();

//				RequestOptions options = RequestOptions.DEFAULT.toBuilder()/**/
//						.setRequestConfig(requestConfig)/**/
//						.build();

				// Create the low-level client
				RestClient restClient = RestClient.builder(new HttpHost(host, port))/**/
						.setRequestConfigCallback(/**/
								new RestClientBuilder.RequestConfigCallback() {
									@Override
									public Builder customizeRequestConfig(Builder requestConfigBuilder) {
										return requestConfigBuilder.setConnectTimeout(1000).setSocketTimeout(60000);
									}
								})/**/
						.build();

				// Create the transport with a Jackson mapper
				ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

				// And create the API client
				client = new ElasticsearchClient(transport);

				log.info("ElasticsearchClients acquired: " + host + ":" + port);
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
