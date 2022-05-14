package elasticsesarch.service;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.client.RestClient;

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
				
				// Create the low-level client
				RestClient restClient = RestClient.builder(new HttpHost(host, port)).build();
				
				// Create the transport with a Jackson mapper
				ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
				
				// And create the API client
				client = new ElasticsearchClient(transport);

				log.info("ElasticsearchClients acquired: " + host + ":" + port);
			}catch(Exception e) {
				throw e;
			}
		}
		return client;
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
