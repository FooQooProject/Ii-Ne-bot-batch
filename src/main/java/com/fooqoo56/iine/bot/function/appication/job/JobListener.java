package com.fooqoo56.iine.bot.function.appication.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * JobListener.
 */
@Component
@Slf4j
public class JobListener extends JobExecutionListenerSupport {

    /**
     * job開始.
     *
     * @param jobExecution JobExecution
     */
    @Override
    public void beforeJob(@NonNull final JobExecution jobExecution) {
        super.beforeJob(jobExecution);
        log.info("ジョブ開始");
    }

    /**
     * Job終了.
     *
     * @param jobExecution JobExecution
     */
    @Override
    public void afterJob(@NonNull final JobExecution jobExecution) {
        super.afterJob(jobExecution);
        log.info("ジョブ終了");
    }
}
