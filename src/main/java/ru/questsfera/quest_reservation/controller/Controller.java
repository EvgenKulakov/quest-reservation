package ru.questsfera.quest_reservation.controller;

import org.springframework.web.bind.annotation.GetMapping;


@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping("/test")
    public String testPage() {
        return "test1";
    }

}
