package org.edu.cdtu.yz.crazy.utils.sqlServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * 测试插入数据 启动类
 * @author wenc
 * @create 2019-07-24 16:23
 */
public class App {

    public static void main(String[] args) {
        //目标机
        String url2 = "jdbc:sqlserver://192.168.3.246:1433;DatabaseName=test_01;characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        String user2 = "wenc";
        String password2 = "Admin!123";
        Connection conn2 = null;
        PreparedStatement pstm2 = null;
        String schemaPattern2 ="guest";
        String tableName ="a";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Long startTime = System.currentTimeMillis();
            conn2 = DriverManager.getConnection(url2, user2, password2);
            String sql2 = "insert INTO "+schemaPattern2+"."+tableName+"("+"id,name,addr"+")"+" values("+"?,?,?"+")";
            pstm2 = conn2.prepareStatement(sql2);

            conn2.setAutoCommit(false);

            int count =0;
            for (int i = 0; i < 100000; i++) {
                count++;
                pstm2.setInt(1,i);
                pstm2.setString(2,"a"+i);
                pstm2.setString(3,"a"+i);
                pstm2.addBatch();
                if(count%10000==0){
                    pstm2.executeBatch();
                    conn2.commit();
                    System.out.println(" 第：" + count + " 条数据！");
                }
            }

            pstm2.executeBatch();
            conn2.commit();
            //快速插入start
            Long endTime = System.currentTimeMillis();
            System.out.println(count+"表传输完成 用时：" + (endTime - startTime));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
