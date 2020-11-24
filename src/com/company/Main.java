package com.company;

import sun.rmi.runtime.NewThreadAction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        User user = new User();
        user.init();
        user.publishInteractions();
        user.subscribeInteraction();

        Thread.sleep(5000);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String s = br.readLine();
            List<String> list = Arrays.asList(s.split(","));
            String userName = "Alisson";
            String chat = "chat 01";
            user.sendInteraction(list.get(0), userName, chat);
        }
    }

}
