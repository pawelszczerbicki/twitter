package com.tweets.tweets;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import twitter4j.Status;

import java.time.LocalDate;
import java.time.ZoneId;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
public class TwitterResult {

    private LocalDate date;
    private String monthYear;
    private int year;
    private String lang;
    private int retweets;
    private int replies;
    private int likes;
    private int tweets;

    public TwitterResult() {
    }

    public TwitterResult(Status s) {
        this.date = s.getCreatedAt().toInstant().atZone(ZoneId.of("UTC")).toLocalDate();
        this.lang = s.getLang();
        this.retweets = s.getRetweetCount();
        this.replies = 0;
        this.likes = s.getFavoriteCount();
    }

    @JsonIgnore
    public LocalDate getDate() {
        return date;
    }

    public String getDateString() {
        return date !=null ? String.format("%s-%s-%s", date.getDayOfMonth(), date.getMonth().toString(), date.getYear()) : null;
    }

    public String getLang() {
        return lang;
    }

    public int getRetweets() {
        return retweets;
    }

    public int getReplies() {
        return replies;
    }

    public int getLikes() {
        return likes;
    }

    public String getMonth() {
        return monthYear;
    }

    public TwitterResult setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public TwitterResult setMonth(String month) {
        this.monthYear = month;
        return this;
    }

    public TwitterResult setLang(String lang) {
        this.lang = lang;
        return this;
    }

    public TwitterResult setRetweets(int retweets) {
        this.retweets = retweets;
        return this;
    }

    public TwitterResult setReplies(int replies) {
        this.replies = replies;
        return this;
    }

    public TwitterResult setLikes(int likes) {
        this.likes = likes;
        return this;
    }

    public int getYear() {
        return year;
    }

    public TwitterResult setYear(int year) {
        this.year = year;
        return this;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public int getTweets() {
        return tweets;
    }

    public TwitterResult setTweets(int tweets) {
        this.tweets = tweets;
        return this;
    }
}
