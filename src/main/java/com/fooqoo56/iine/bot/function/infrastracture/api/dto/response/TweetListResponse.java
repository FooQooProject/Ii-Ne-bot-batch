package com.fooqoo56.iine.bot.function.infrastracture.api.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Splitter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class TweetListResponse implements Serializable {

    private static final long serialVersionUID = -4621559503100682231L;

    private static final String DEFAULT_NEXT_MAX_ID = "-1";

    @JsonProperty("statuses")
    List<TweetResponse> statuses;

    @JsonProperty("search_metadata")
    SearchMetaData searchMetaData;

    /**
     * デフォルトコンストラクタ.
     */
    public TweetListResponse() {
        statuses = new ArrayList<>();
        searchMetaData = new SearchMetaData();
    }

    @Getter
    @ToString
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchMetaData implements Serializable {

        private static final long serialVersionUID = 47033691799811374L;

        private final String nextMaxId;

        public SearchMetaData() {
            nextMaxId = "0";
        }

        /**
         * Json生成.
         *
         * @param nextResults nextResults
         */
        @JsonCreator
        public SearchMetaData(
                @JsonProperty("next_results") final String nextResults
        ) {
            this.nextMaxId = getMaxIdFromNextResults(nextResults);
        }

        /**
         * 次のmax_idを取得する.
         *
         * @param nextResults next_resultsの値
         * @return 解析後のmax_id
         */
        private String getMaxIdFromNextResults(final String nextResults) {

            if (StringUtils.isBlank(nextResults)) {
                return "0";
            }

            final String query = nextResults.split("\\?")[1];

            final Map<String, String> useGuavaOutputMap =
                    Splitter.on('&').trimResults().withKeyValueSeparator("=").split(query);

            return useGuavaOutputMap.get("max_id");

        }
    }
}
