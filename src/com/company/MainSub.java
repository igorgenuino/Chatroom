package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class MainSub {
    public static void main(String[] args) throws Exception {
        User user1 = new User();
        user1.init();
        user1.publishInteractions();
        user1.subscribeInteraction();


        Thread.sleep(5000);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String s = br.readLine();
            List<String> list = Arrays.asList(s.split(","));
            String userName = "Igor";
            String chat = "chat 01";
            user1.sendInteraction(list.get(0), userName, chat);
        }

    }
}
