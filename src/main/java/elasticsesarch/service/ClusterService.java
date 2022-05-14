package elasticsesarch.service;

import java.util.Map;

import org.apache.log4j.Logger;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;
import co.elastic.clients.elasticsearch.cluster.health.IndexHealthStats;

public class ClusterService {
	private static Logger log = Logger.getLogger(ClusterService.class);
	private static final String TIRE_KICK = "Tire Kick: ";

	public static void clusterClientCheckout(ElasticsearchClient client) throws Exception {
		try {
			getListOfIndexesFromClusterClient(client);
		} catch (Exception e) {
			throw e;
		}
	}
	

	public static void getListOfIndexesFromClusterClient(ElasticsearchClient client) throws Exception {

		try {
			HealthResponse response = client.cluster().health();
			
			Map<String, IndexHealthStats> indicesMap = response.indices();

			if (indicesMap.size() > 0) {
				indicesMap.forEach((key, obj) -> {
					log.info(TIRE_KICK + key + " -> " + obj.toString());
				});
			} else {
				log.info(TIRE_KICK + "There are no indices in this cluster (" + response.clusterName() + ")");
			}

		} catch (Exception e) {
			throw e;
		}

	}

//	public static void getListOfIndexesFromClusterClient(ElasticsearchClient client) throws Exception {
//
//		try {
//			ClusterHealthResponse clusterHealthResponse = client.cluster()/**/
//					.health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
//
//			Map<String, ClusterIndexHealth> indicesMap = clusterHealthResponse.getIndices();
//			if (indicesMap.size() > 0) {
//				indicesMap.forEach((key, obj) -> {
//					log.info(TIRE_KICK + key + " -> " + obj.toString());
//				});
//			} else {
//				log.info(TIRE_KICK + "There are no indices in this cluster (" + clusterHealthResponse.getClusterName() + ")");
//			}
//
//		} catch (Exception e) {
//			throw e;
//		}
//
//	}

}
