package cn.aps.boot.aop.agent.classz;

import org.benf.cfr.reader.api.OutputSinkFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @Description :
 * @Author : lishirui
 * @Date ：2025/7/10 15:09
 */

public class StringBuilderSinkFactory implements OutputSinkFactory {
    private final StringBuilder output;

    public StringBuilderSinkFactory(StringBuilder output) {
        this.output = output;
    }

    @Override
    public List<SinkClass> getSupportedSinks(SinkType sinkType, Collection<SinkClass> collection) {
        // 返回你支持的 sink 类型，这里只处理 Java 反编译输出
        if (sinkType == SinkType.JAVA) {
            return Collections.singletonList(SinkClass.STRING);
        }
        return Collections.emptyList();
    }

    @Override
    public <T> Sink<T> getSink(SinkType sinkType, SinkClass sinkClass) {
        return (Sink<T>) s -> {
            if (sinkType == SinkType.JAVA && sinkClass == SinkClass.STRING) {
                output.append(s).append("\n");
            }
        };
    }
}
