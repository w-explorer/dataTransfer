package org.edu.cdtu.yz.crazy.utils.sqlServer;

import org.edu.cdtu.yz.crazy.been.Filed;

import java.sql.*;
import java.util.List;

/**
 * sqlServer 之间传输数据
 * 指定多表传入指定的一张目标表中
 *
 * @author wenc
 * @create 2019-07-20 9:00
 */
public class BigDataFastTransferForSqlServer {
    public  void biubiubiu(String tableName, StringBuffer fileds, List<Filed> listFiled,List<String> sourseTableList){

//        拼接  sql  参数 ?
//        String fileds ="id,m,df,d";
//        ?,?,?,?
        String[] split = fileds.toString().split(",");
        int len = split.length;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; i++) {
            if(i==0){
                sb.append("?");
            }
            else {
                sb.append(",?");
            }
        }

        //目标机
        String url = "jdbc:sqlserver://192.168.3.246:1433;DatabaseName=test_01;characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        String user = "wenc";
        String password = "Admin!123";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rt = null;
        String schemaPattern1 ="dbo"; //表前缀


        //源机
        String url2 = "jdbc:sqlserver://192.168.3.246:1433;DatabaseName=test_01;characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
        String user2 = "wenc";
        String password2 = "Admin!123";
        Connection conn2 = null;
        PreparedStatement pstm2 = null;
        String schemaPattern2 ="guest";


        try {

            for (String sourseTableName:
            sourseTableList) {
                System.out.println(sourseTableName+"---到---"+tableName+"开始传输");
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conn = DriverManager.getConnection(url, user, password);
                //批量读取start
                String sql = "select "+fileds+" from "+schemaPattern2+"."+sourseTableName;
                pstm = conn.prepareStatement(sql);
                rt = pstm.executeQuery();
                //批量读取end

                //快速插入start
                Long startTime = System.currentTimeMillis();
                conn2 = DriverManager.getConnection(url2, user2, password2);
                String sql2 = "insert INTO "+schemaPattern1+"."+tableName+"("+fileds+")"+" values("+sb+")";
                pstm2 = conn2.prepareStatement(sql2);

                conn2.setAutoCommit(false);


                int count = 0;
                while (rt.next()) {
                    count++;
                    int i =0;
                    for (Filed filed:
                            listFiled) {
                        i++;
                        pstm2.setObject(i,rt.getObject(filed.getFiled()));
                    }
                    pstm2.addBatch();
                    // 逻辑操作
                    if (count % 10000 == 0) {
                        pstm2.executeBatch();
                        conn2.commit();
                        System.out.println(sourseTableName+" 第：" + count + " 条数据！");
                    }
                }
                pstm2.executeBatch();
                conn2.commit();
                System.out.println(sourseTableName+" 第：" + count + " 条数据！");
                //快速插入start
                Long endTime = System.currentTimeMillis();
                System.out.println(sourseTableName+"---到---"+tableName+"表传输完成 用时：" + (endTime - startTime));
                System.out.println();
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rt != null) {
                    rt.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
                if (pstm2 != null) {
                    pstm2.close();
                }
                if (conn2 != null) {
                    conn2.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
