package cn.aps.boot.jdk17.demo.boot.event;

import org.springframework.context.ApplicationEvent;

/**
 * @Description : 自定义事件
 * @Author : lishirui
 * @Date ：2025/6/17 16:11
 */
public class AbstractApplicationEvent extends ApplicationEvent{
    public AbstractApplicationEvent(ApplicationEventPublishStatus eventPublishStatus) {
        super(eventPublishStatus);
    }
}
