module app {
	exports vo; // IndexerService, method indexGamecastDataWithObjectMapper
	exports data; // IndexerService, method indexDirectoryDocumentsWithJson (objectmapper)

	requires java.sql;
	requires log4j;
	// requires elasticsearch.rest.high.level.client; //deprecated as of 7.15
	requires elasticsearch.rest.client;
	requires elasticsearch.java;
	requires elasticsearch.x.content;
	// requires elasticsearch; // this is the one
	requires org.apache.httpcomponents.httpcore;
	requires org.apache.httpcomponents.httpclient;
	requires org.apache.httpcomponents.httpcore.nio;
	requires org.apache.httpcomponents.httpasyncclient;
	requires commons.logging;
	requires org.apache.commons.codec;
	requires jakarta.json;
	requires org.eclipse.parsson;
	// requires javax.json.api;
	// requires javax.json;

	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.datatype.jsr310;
	requires lucene.analyzers.common;
	requires commons.csv;
	requires com.google.gson;
}