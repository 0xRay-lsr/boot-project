package cn.aps.boot.demo.java8.abs;

import cn.aps.boot.demo.java8.function.RunnableWithReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description : 抽象的支付实现，主要抽取一些公共的机制
 * @Author : lishirui
 * @Date ：2024/8/26 16:56
 */
public abstract class PayAbstract implements Pay {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void initPay() {
        logger.info("初始化转账环境");
        countCodeBlockTimeCost(() -> {
            doInitPlay();
            return null;
        });
        logger.info("转账环境初始化结束");
    }

    /**
     * 抽象的初始化方法，由具体实现定义方法细节
     */
    abstract void doInitPlay();

    @Override
    public void sendPay(String src, String dest, int money) {
        logger.info("开始转账，从 {} - 转入 {} ,转入金额为:{}", src, dest, money);
        countCodeBlockTimeCost(() -> {
            doSendPay(src, dest, money);
            return null;
        });
        logger.info("转账结束");
    }

    /**
     * 抽象的转账方法，由具体实现定义方法细节
     *
     * @param src
     * @param dest
     * @param money
     */
    abstract void doSendPay(String src, String dest, int money);

    /**
     * 方法调用耗时统计
     * @param runnableWithReturn 生产者函数
     */
    protected void countCodeBlockTimeCost(RunnableWithReturn runnableWithReturn) {
        long startTime = System.currentTimeMillis();
        runnableWithReturn.execute();
        long timeCost = System.currentTimeMillis() - startTime;
        logger.debug("当前方法执行耗时为：" + timeCost + " 毫秒");
    }
}
