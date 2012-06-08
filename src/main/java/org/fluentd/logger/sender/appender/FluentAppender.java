package org.fluentd.logger.sender.appender;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.commons.lang.StringUtils;
import org.fluentd.logger.FluentLogger;

public class FluentAppender extends AppenderSkeleton {

	private FluentLogger fluentLogger;
	
	protected String tag = "log4j-appender";
	
	protected String host = "localhost";

	protected int port = 24224;
	
	protected String label = "label";
	
	@Override
	public void activateOptions() {
		try {
			fluentLogger = FluentLogger.getLogger(tag, host, port);
		} catch (RuntimeException e) {
//			addError("Cannot create FluentLogger.", e);
		}
		super.activateOptions();
	}
	
	@Override
	public void close() {
		// TODO Auto-generated method stub
		try {
			fluentLogger.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean requiresLayout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void append(LoggingEvent event) {
		Map<String, Object> messages = new HashMap<String, Object>();
		messages.put("level", event.getLevel().toString());
		messages.put("loggerName", event.getLoggerName());
		messages.put("thread", event.getThreadName());
		messages.put("message", event.getMessage().toString());
		messages.put("msec", event.getTimeStamp() % 1000);
		messages.put("throwableInfo", 
			(event.getThrowableInformation() != null)?
				StringUtils.join(event.getThrowableStrRep(), "\n") : "");
		
		fluentLogger.log(label, messages, event.getTimeStamp() / 1000);
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTag() {
		return tag;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getLabel() {
		return label;
	}
}
