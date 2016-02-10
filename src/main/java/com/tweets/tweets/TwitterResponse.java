package com.tweets.tweets;

import java.util.List;

public class TwitterResponse {

    private String query;

    private boolean finished;

    private TwitterResult overallStats;

    private List<TwitterResult> monthStats;

    private List<TwitterResult> dayStats;

    private List<TwitterResult> raw;

    public String getQuery() {
        return query;
    }

    public boolean getFinished() {
        return finished;
    }

    public TwitterResult getOverallStats() {
        return overallStats;
    }

    public List<TwitterResult> getMonthStats() {
        return monthStats;
    }

    public List<TwitterResult> getDayStats() {
        return dayStats;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setOverallStats(TwitterResult overallStats) {
        this.overallStats = overallStats;
    }

    public void setMonthStats(List<TwitterResult> monthStats) {
        this.monthStats = monthStats;
    }

    public void setDayStats(List<TwitterResult> dayStats) {
        this.dayStats = dayStats;
    }

    public List<TwitterResult> getRaw() {
        return raw;
    }

    public void setRaw(List<TwitterResult> raw) {
        this.raw = raw;
    }
}
