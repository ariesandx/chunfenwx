package com.chufen.wx.demo;

import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @author chunfen.wx
 */
public class App {

    org.slf4j.Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        int i=1;
        while(i++ > 0){
            System.out.println(i + "add todo.");
            Scanner scanner = new Scanner(System.in);
            TodoItem todoItem = new TodoItem(scanner.nextLine());
            System.out.println(todoItem);
        }
    }
}
