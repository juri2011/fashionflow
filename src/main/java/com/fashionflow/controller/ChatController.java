package com.fashionflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatController {

    @GetMapping("/chat")
    public String chatGET(){
        System.out.println("@ChatController, chat GET()");
        return "chat/chater";
    }
}