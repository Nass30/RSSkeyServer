package com.rsskey.server.Controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class PingController {

    @RequestMapping("/ping")
    public String ping() {
        System.out.println("ping request");
        return new String("pong");
    }
}
