package com.chunfen.wx;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created by xic on 2016/8/13.
 */
public class TestTest {

    @Test
    public void tt() throws Exception{
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        System.out.println("jdbc 获取连接");
        Connection conn = DriverManager.getConnection("jdbc:db2://192.168.129.131:50000/sample", "db2user", "db2user");
        conn.setAutoCommit(false);
//        PreparedStatement ps = conn.prepareStatement("select * from fap_biz_lock where biz_type = 'global' for update with rs");
        PreparedStatement ps = conn.prepareStatement("update fap_biz_lock b set biz_type = 'global' where biz_type = 'global'");
        System.out.println("jdbc 执行sql");
        ps.execute();
        System.out.println("jdbc 获得锁");
        System.out.println("jdbc 睡");
        Thread.sleep(8000);
        System.out.println("jdbc 醒");
        conn.commit();
        System.out.println("jdbc 提交事务");
        conn.close();
    }

    @Test
    public void tt2() throws Exception{
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        System.out.println("jdbc2 获取连接");
        Connection conn = DriverManager.getConnection("jdbc:db2://192.168.129.131:50000/sample", "db2user", "db2user");
        conn.setAutoCommit(false);
//        PreparedStatement ps = conn.prepareStatement("select * from fap_biz_lock where biz_type = 'global' for update with rs");
        PreparedStatement ps = conn.prepareStatement("update fap_biz_lock b set biz_type = 'global' where biz_type = 'global'");
        System.out.println("jdbc2 执行sql");
        ps.execute();
        System.out.println("jdbc2 获得锁");
        conn.commit();
        System.out.println("jdbc2 提交事务");
        conn.close();
    }

    @Test
    public void sfsdf() throws Exception{
        new Thread(){
            @Override
            public void run() {
                try {
                    tt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(3000);
        new Thread(){
            @Override
            public void run() {
                try {
                    tt2();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(20000);
    }

    @Test
    public void logTest(){
        com.chunfen.wx.Logger.debug("this is test");
    }
}
