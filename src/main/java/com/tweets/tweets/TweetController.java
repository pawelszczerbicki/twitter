package com.tweets.tweets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TweetController {

    @Autowired
    private TweetService service;

    @RequestMapping("/")
    public String index() {
        return "Twitter search from 2015 to now! Type /tweets?query=xxxx to start search";
    }

    @RequestMapping("/tweets")
    public String find(String query) {
        service.analyzeProfile(query);
        return String.format("Started searching for: [%s], Result URL: <a href='/status'>/status</a>, " +
                "File: <a href='/file'>/file</a>, Cancel URL <a href='/cancel'>/cancel</a><br/>" +
                "Status can be run with params: raw, day, month. Ex /status?month=true", query);
    }

    @RequestMapping("/cancel")
    public String cancel() {
        String query = service.getQuery();
        service.cancel();
        return "Canceled query: " + query;
    }

    @RequestMapping("/status")
    public TwitterResponse status(boolean raw, boolean day, boolean month) {
        return service.status(raw, day, month);
    }

    @RequestMapping("/file")
    public ResponseEntity<byte[]> status() {
        byte[] output = service.asStringCsv().getBytes();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("charset", "utf-8");
        responseHeaders.setContentType(MediaType.TEXT_PLAIN);
        responseHeaders.setContentLength(output.length);
        responseHeaders.set("Content-disposition", String.format("attachment; filename=%s.txt", service.getQuery()));

        return new ResponseEntity<>(output, responseHeaders, HttpStatus.OK);
    }

}
