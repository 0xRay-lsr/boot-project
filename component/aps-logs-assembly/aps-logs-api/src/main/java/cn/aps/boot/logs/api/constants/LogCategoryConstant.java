package cn.aps.boot.logs.api.constants;

/**
 * @Description : 日志Logger名称前缀，按照技术贯标要求，分为多种日志类型
 * @Author : lishirui
 * @Date ：2025/3/31 17:13
 */
public interface LogCategoryConstant {
    /**
     * 应用日志
     * <p>
     * 应用在运行时执行相关业务逻辑时，打印的业务处理日志，包括同步和异步的处理日志
     */
    String APS_APP = "aps.app";

    /**
     * 启停日志
     * <p>
     * 应用在启动和停止两个阶段打印的日志，主要包括启动配置加载、元数据载入、缓存加载、优雅停机等事件
     */
    String APS_BOOT = "aps.boot";

    /**
     * 定时任务日志
     * <p>
     * 应用在触发定时任务、轮询调度和调度主控等技术场景时所打印的日志
     */
    String APS_TIMER = "aps.timer";

    /**
     * 慢SQL日志
     * <p>
     * 应用在执行SQL时耗时超过了指定的时间后所打印的日志，主要用于排查慢交易和异常交易的原因
     */
    String APS_SLOWSQL = "aps.slowsql";

    /**
     * 通讯日志
     * <p>
     * 应用在发生接口外调时所产生的通讯相关日志，包括通讯双方信息、交易报文等
     */
    String APS_LINKS = "aps.links";

    /**
     * 中间件日志
     * <p>
     * 应用在运行时通过依赖的第三方中间件相关包打印的日志，如心跳日志、服务注册、服务发现等
     */
    String APS_MIDWARE = "aps.midware";

    /**
     * 告警日志
     * <p>
     * 应用在运行时出现的部分技术或业务类的告警信息而打印的相关日志
     */
    String APS_ALERT = "aps.alert";

    /**
     * 性能调优日志
     * <p>
     * 应用在运行时打印的与性能相关日志，如交易处理总耗时、报文解包和组包耗时等
     */
    String APS_PROFILE = "aps.profile";

    /**
     * APM内部链路日志, 不在日志规范类别范围内
     * <p>
     * 方便排查APM内部问题
     */
    String APS_TRACE = "aps.trace";

    /**
     * SQL日志, 不在日志规范类别范围内, 输出到应用日志区
     * <p>
     * 应用执行数据库操作所打印的日志
     */
    String APS_SQL = "aps.sql";

    /**
     * BATCH批量日志, 不在日志规范类别范围内, 输出到应用日志区
     * <p>
     * 应用执行数据库操作所打印的日志
     */
    String APS_BATCH = "aps.batch";
}
