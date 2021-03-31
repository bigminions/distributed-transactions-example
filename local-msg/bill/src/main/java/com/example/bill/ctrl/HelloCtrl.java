package com.example.bill.ctrl;

import org.apache.logging.log4j.util.Strings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("hello")
public class HelloCtrl {

    @GetMapping(path = "/{name}")
    public ResponseEntity<String> hello(@PathVariable("name") String name) {
        if (Strings.isBlank(name)) {
            return ResponseEntity.ok("Hello, body");
        }
        return ResponseEntity.ok("Hello, " + name);
    }
}
