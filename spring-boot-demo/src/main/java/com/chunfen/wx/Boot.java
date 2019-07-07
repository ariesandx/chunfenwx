package com.chunfen.wx;

//import java.util.Scanner;
//
///**
// * Created by xi.w on 2018-10-20.
// */
//@SpringBootApplication
//public class Boot {
//
//    public static void main(String[] args) {
//        //保存 spring boot 生成的context 对象
//        ConfigurableApplicationContext applicationContext = SpringApplication.run(Boot.class, args);
//
//        // 生成获取控制台输入信息对象
//        Scanner scanner = new Scanner(System.in);
//
//        // 循环
//        while(true){
//            //获取控制台输入，如果没有会被阻塞
//            String in = scanner.nextLine();
//            //如果输入 q表示退出程序
//            if(in.equals("q")){
//                break;
//            }
//        }
//        // 退出 spring boot
//        SpringApplication.exit(applicationContext);
//    }
//}

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chunfen.wx.domain")
public class Boot {

    public static void main(String[] args) {
        SpringApplication.run(Boot.class, args);
    }
}