package org.telegram.toto.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WakeController {

    @GetMapping("/wake")
    public String wakeUp() {
        return "I am awake!";
    }

}
