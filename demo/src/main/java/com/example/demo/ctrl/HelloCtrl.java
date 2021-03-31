package com.example.demo.ctrl;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RefreshScope
@RequestMapping("hello")
public class HelloCtrl {

    @Value("${slogan:Good bye!}")
    private String slogan;

    @GetMapping(path = "/{name}")
    public ResponseEntity<String> hello(@PathVariable("name") String name) {
        if (Strings.isBlank(name)) {
            return ResponseEntity.ok("Hello, body, " + slogan);
        }
        return ResponseEntity.ok("Hello, " + name + " " + slogan);
    }
}
