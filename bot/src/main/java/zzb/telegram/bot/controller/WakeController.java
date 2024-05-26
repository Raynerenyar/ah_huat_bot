package zzb.telegram.bot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WakeController {

    @GetMapping("/wake")
    public String wakeUp() {
        return "I am awake!";
    }

}
