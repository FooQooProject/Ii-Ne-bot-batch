package com.fooqoo56.iine.bot.function.appication.config;

import com.fooqoo56.iine.bot.function.appication.job.JobListener;
import com.fooqoo56.iine.bot.function.appication.service.IiNeBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * PriceStep設定クラス.
 */
@Component
@RequiredArgsConstructor
@EnableBatchProcessing
public class StepConfig {

    public static final String JOB = "job";
    private static final String II_NE_BOT_STEP = "Ii-Ne-bot";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobListener jobListener;

    private final IiNeBotService iiNeBotService;

    /**
     * ステップのbean.
     *
     * @return Stepインスタンス
     */
    @Bean(name = II_NE_BOT_STEP)
    @NonNull
    public Step iiNeBotStep(final Tasklet twitterTasklet) {
        return stepBuilderFactory.get(II_NE_BOT_STEP).tasklet(twitterTasklet).build();
    }

    /**
     * Jobのbean.
     *
     * @param iiNeBotStep Stepインスタンス
     * @return Job
     */
    @Bean(name = JOB)
    @NonNull
    public Job job(@Qualifier(II_NE_BOT_STEP) final Step iiNeBotStep) {

        return jobBuilderFactory
                .get(JOB)
                .incrementer(new RunIdIncrementer())
                .listener(jobListener)
                .start(iiNeBotStep)
                .build();
    }

    /**
     * タスクレット.
     *
     * @param query          クエリ
     * @param favoriteCount  いいね数
     * @param followersCount フォロワー数
     * @param friendsCount   フォロー数
     * @param retweetCount   リツイート数
     * @return タスクレット
     */
    @Bean
    @StepScope
    public Tasklet twitterTasklet(
            @Value("#{jobParameters['query']}") final String query,
            @Value("#{jobParameters['favoriteCount']}") final Long favoriteCount,
            @Value("#{jobParameters['followersCount']}") final Long followersCount,
            @Value("#{jobParameters['friendsCount']}") final Long friendsCount,
            @Value("#{jobParameters['retweetCount']}") final Long retweetCount) {

        final MethodInvokingTaskletAdapter tasklet = new MethodInvokingTaskletAdapter();

        tasklet.setTargetObject(iiNeBotService);
        tasklet.setTargetMethod("execute");
        tasklet.setArguments(
                new Object[] {query, favoriteCount, followersCount, friendsCount, retweetCount});

        return tasklet;
    }
}
