package cn.com.jonpad.juc.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

/**
 * 任务启动器接口
 * 由各个具体任务去实现
 *
 * @author Jon Chan
 * @date 2020/4/24 23:33
 */
public interface TaskStarter<T> extends Callable<T> {

    /**
     * 任务相关常量
     */
    interface TASK_CONSTANT {
        /**
         * 任务队列长度
         */
        int QUEUE_SIZE = 5000;
    }


}
