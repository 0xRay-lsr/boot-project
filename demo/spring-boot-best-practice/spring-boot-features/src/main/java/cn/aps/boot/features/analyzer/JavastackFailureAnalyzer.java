package cn.aps.boot.features.analyzer;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * 来源@Author : lishirui
 */
public class JavastackFailureAnalyzer extends AbstractFailureAnalyzer<JavastackException> {

	@Override
	protected FailureAnalysis analyze(Throwable rootFailure, JavastackException cause) {
		return new FailureAnalysis("Java技术栈发生异常了……",
				"赶快去检查一下吧！",
				cause);
	}

}