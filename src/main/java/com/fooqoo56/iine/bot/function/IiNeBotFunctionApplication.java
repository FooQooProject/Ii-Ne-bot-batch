package com.fooqoo56.iine.bot.function;


import static com.fooqoo56.iine.bot.function.domain.model.TweetCondition.PARAM_FAVORITE_COUNT;
import static com.fooqoo56.iine.bot.function.domain.model.TweetCondition.PARAM_FOLLOWERS_COUNT;
import static com.fooqoo56.iine.bot.function.domain.model.TweetCondition.PARAM_FRIENDS_COUNT;
import static com.fooqoo56.iine.bot.function.domain.model.TweetCondition.PARAM_QUERY;
import static com.fooqoo56.iine.bot.function.domain.model.TweetCondition.PARAM_RETWEET_COUNT;


import com.fooqoo56.iine.bot.function.appication.config.StepConfig;
import com.fooqoo56.iine.bot.function.domain.model.PubSubMessage;
import com.fooqoo56.iine.bot.function.domain.model.TweetCondition;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ConfigurationPropertiesScan
public class IiNeBotFunctionApplication {

    public static final String JOB_LAUNCHER = "jobLauncher";
    public static final String DATE_PARAM = "date";

    /**
     * Spring Boot main.
     *
     * @param args args
     */
    public static void main(final String[] args)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        run(new TweetCondition("Next.js", 3L, 3L, 10L, 10L));
    }

    private static void run(final TweetCondition tweetCondition)
            throws JobParametersInvalidException, JobExecutionAlreadyRunningException,
            JobRestartException, JobInstanceAlreadyCompleteException {
        final SpringApplication application =
                new SpringApplication(IiNeBotFunctionApplication.class);

        application.setWebApplicationType(WebApplicationType.NONE);

        final ConfigurableApplicationContext context = application.run();
        final JobLauncher jobLauncher = context.getBean(JOB_LAUNCHER, JobLauncher.class);
        final Job job = context.getBean(StepConfig.JOB, Job.class);

        jobLauncher.run(job, createJobParams(tweetCondition));
    }

    /**
     * jobパラメータを取得.
     *
     * @return job
     */
    @NonNull
    private static JobParameters createJobParams(final TweetCondition tweetCondition) {
        return new JobParametersBuilder()
                .addDate(DATE_PARAM, new Date())
                .addString(PARAM_QUERY, tweetCondition.getQuery())
                .addLong(PARAM_FAVORITE_COUNT, tweetCondition.getFavoriteCount())
                .addLong(PARAM_FOLLOWERS_COUNT, tweetCondition.getFollowersCount())
                .addLong(PARAM_FRIENDS_COUNT, tweetCondition.getFriendsCount())
                .addLong(PARAM_RETWEET_COUNT, tweetCondition.getRetweetCount())
                .toJobParameters();
    }

    /**
     * Cloud Functionの実行関数.
     *
     * @return 関数
     */
    @Bean
    @NonNull
    public Consumer<PubSubMessage> pubSubFunction() {
        return message -> {
            // The PubSubMessage data field arrives as a base-64 encoded string and must be decoded.
            // See: https://cloud.google.com/functions/docs/calling/pubsub#event_structure
            try {
                final TweetCondition tweetCondition =
                        new TweetCondition(
                                getDecodedMessage(message),
                                3L,
                                3L,
                                10L,
                                10L);
                run(tweetCondition);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        };
    }

    /**
     * メッセージをデコードする.
     *
     * @param message pubsubメッセージ
     * @return デコードされたdataパラメータ
     */
    @NonNull
    private String getDecodedMessage(final PubSubMessage message) {
        if (Objects.nonNull(message)) {
            if (StringUtils.isNoneBlank(message.getData())) {
                return message.getData();
            }
        }
        return StringUtils.EMPTY;
    }

}
