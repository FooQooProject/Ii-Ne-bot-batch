package application.service

import com.fooqoo56.iine.bot.function.appication.sharedservice.TwitterSharedService
import com.fooqoo56.iine.bot.function.domain.model.TweetCondition
import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.request.TweetRequest
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetListResponse
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.lang.Unroll

class TwitterSharedServiceSpec extends Specification {

    TwitterRepository twitterRepository
    TwitterSharedService twitterService

    final setup() {
        twitterRepository = Mock(TwitterRepository)
    }


    @Unroll
    final "getFollower"() {
        given:
        twitterService = new TwitterSharedService(twitterRepository)
        final response = Mono.just(new TweetListResponse())
        twitterRepository.findTweet(_ as TweetRequest) >> response

        when:
        final result = twitterService.findTweet(request).block()

        then:
        result == expected

        where:
        request                                     || expected
        new TweetCondition("Next.js", 3, 3, 10, 10) || []
    }
}
