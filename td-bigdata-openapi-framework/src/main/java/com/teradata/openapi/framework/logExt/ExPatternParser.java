package com.teradata.openapi.framework.logExt;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;
import org.apache.log4j.spi.LoggingEvent;

public class ExPatternParser extends PatternParser {

	public ExPatternParser(String pattern) {
		super(pattern);
	}

	/**
	 * 重写finalizeConverter，对特定的占位符进行处理，T表示线程ID占位符
	 */
	@Override
	protected void finalizeConverter(char c) {
		if (c == 'T') {
			this.addConverter(new ExPatternConverter(this.formattingInfo));
		} else {
			super.finalizeConverter(c);
		}
	}

	private static class ExPatternConverter extends PatternConverter {

		public ExPatternConverter(FormattingInfo fi) {
			super(fi);
		}

		/**
		 * 当需要显示线程ID的时候，返回当前调用线程的ID
		 */
		@Override
		protected String convert(LoggingEvent event) {
			// 排除线程名后含有#和-
			String threadName = Thread.currentThread().getName();
			if (threadName.lastIndexOf("#") > -1) {
				threadName = threadName.substring(0, threadName.lastIndexOf("#"));
			}
			if (threadName.lastIndexOf("-") > -1) {
				threadName = threadName.substring(0, threadName.lastIndexOf("-"));
			}
			return String.valueOf(threadName + " ^ " + Thread.currentThread().getId());
		}

	}
}
