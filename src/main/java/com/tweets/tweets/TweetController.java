package com.tweets.tweets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TweetController {

    @Autowired
    private TweetService service;

    @RequestMapping("/tweets")
    public String find(String query) {
        service.analyzeProfile(query);
        return String.format("Started searching for: [%s], Result URL: <a href='/status'>/status</a>, Cancel URL <a href='/cancel'>/cancel</a>", query);
    }

    @RequestMapping("/cancel")
    public String cancel() {
        String query = service.getQuery();
        service.cancel();
        return "Canceled query: " + query;
    }

    @RequestMapping("/status")
    public TwitterResponse status(boolean raw, boolean day,  boolean month) {
        return service.status(raw, day, month);
    }

}
