package com.tweets.tweets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import twitter4j.Paging;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class TweetService {

    private boolean finished;
    private boolean run = true;
    private String query;
    private Collection<TwitterResult> results;

    @Autowired
    private Twitter twitter;

    @Async
    public void analyzeProfile(String user) {
        results = new ArrayList<>();
        finished = false;
        run = true;
        query = user;
        int pageno = 1;
        while (run) {
            try {
                int size = results.size();
                results.addAll(twitter.getUserTimeline(user, new Paging(pageno++, 100)).stream().map(TwitterResult::new).peek(res -> {
                    if (res.getDate().getYear() < 2015)
                        run = false;
                }).collect(toList()));
                if (results.size() == size) {
                    run = false;
                    break;
                }
            } catch (TwitterException e) {
                e.printStackTrace();
                return;
            }
        }
        finished = true;
    }

    public String getQuery() {
        return query;
    }

    public void cancel() {
        run = false;
    }

    public TwitterResponse status(boolean raw, boolean day, boolean month) {
        TwitterResponse res = new TwitterResponse();
        ArrayList<TwitterResult> copied = new ArrayList<>(results);
        if (raw)
            res.setRaw(copied);

        if (month)
            res.setMonthStats(copied.stream().collect(groupingBy(tr -> format("%s %s", tr.getDate().getMonth().toString(), tr.getDate().getYear())))
                    .entrySet().stream().map(this::toResult).collect(Collectors.toList()));
        if (day)
            res.setDayStats(copied.stream().collect(groupingBy(TwitterResult::getDate))
                    .entrySet().stream().map(this::toResultDate).collect(Collectors.toList()));

        res.setOverallStats(toResultOverall(results));
        res.setFinished(finished);
        res.setQuery(query);
        return res;
    }

    private TwitterResult toResult(Map.Entry<String, List<TwitterResult>> s) {
        return new TwitterResult().setMonth(s.getKey())
                .setLikes(s.getValue().stream().mapToInt(TwitterResult::getLikes).sum())
                .setReplies(s.getValue().stream().mapToInt(TwitterResult::getReplies).sum())
                .setRetweets(s.getValue().stream().mapToInt(TwitterResult::getRetweets).sum())
                .setTweets(s.getValue().size());
    }

    private TwitterResult toResultDate(Map.Entry<LocalDate, List<TwitterResult>> s) {
        return new TwitterResult().setDate(s.getKey())
                .setLikes(s.getValue().stream().mapToInt(TwitterResult::getLikes).sum())
                .setReplies(s.getValue().stream().mapToInt(TwitterResult::getReplies).sum())
                .setRetweets(s.getValue().stream().mapToInt(TwitterResult::getRetweets).sum())
                .setTweets(s.getValue().size());
    }

    private TwitterResult toResultOverall(Collection<TwitterResult> s) {
        return new TwitterResult()
                .setLikes(s.stream().mapToInt(TwitterResult::getLikes).sum())
                .setReplies(s.stream().mapToInt(TwitterResult::getReplies).sum())
                .setRetweets(s.stream().mapToInt(TwitterResult::getRetweets).sum())
                .setTweets(s.size());
    }

    public String asStringCsv() {
        StringBuilder b = new StringBuilder("date;likes;replies;retweets\n");
        results.forEach(r-> b.append(r.getDate().format(ISO_DATE)).append(";")
                .append(r.getLikes()).append(";")
                .append(r.getReplies()).append(";")
                .append(r.getRetweets())
                .append("\n"));
        return b.toString();

    }
}
