package application.service

import com.fooqoo56.iine.bot.function.appication.service.TwitterService
import com.fooqoo56.iine.bot.function.domain.model.TweetCondition
import com.fooqoo56.iine.bot.function.domain.repository.api.TwitterRepository
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.request.TweetRequest
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetListResponse
import com.fooqoo56.iine.bot.function.infrastracture.api.dto.response.TweetResponse
import spock.lang.Specification
import spock.lang.Unroll

class TwitterServiceSpec extends Specification {

    TwitterRepository twitterRepository
    TwitterService twitterService

    final setup() {
        twitterRepository = Mock(TwitterRepository)
    }


    @Unroll
    final "getFollower"() {
        given:
        twitterService = new TwitterService(twitterRepository)
        final response = new TweetListResponse()
        twitterRepository.findTweet(_ as TweetRequest) >> response

        when:
        final result = twitterService.findTweet(request)

        then:
        result == expected

        where:
        request    || expected
        new TweetCondition("Next.js", 3, 3, 10, 10) || []
    }
}
