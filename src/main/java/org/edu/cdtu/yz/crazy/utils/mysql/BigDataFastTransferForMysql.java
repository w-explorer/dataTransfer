package org.edu.cdtu.yz.crazy.utils.mysql;

import org.edu.cdtu.yz.crazy.been.Filed;

import java.sql.*;
import java.util.List;

/**
 * @author wenc
 * @create 2019-07-20 9:00
 */
public class BigDataFastTransferForMysql {
    public  void biubiubiu(String tableName, StringBuffer fileds, List<Filed> listFiled){

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

        //源机
        String url = "jdbc:mysql://mysql5.cdqdops.org:3306/standard_5101000047?characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
        String user = "clean";
        String password = "cDqd!8883";
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rt = null;

        //目标机
        String url2 = "jdbc:mysql://mysql6.cdqdops.org:3306/zl_analysis_reader?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
        String user2 = "zl_write";
        String password2 = "Pdw(2323a";
        Connection conn2 = null;
        PreparedStatement pstm2 = null;


        try {
            System.out.println(tableName+"开始传输");
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            //批量读取start
            String sql = "select "+fileds+" from "+tableName;
            System.out.println(sql);
            pstm = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            pstm.setFetchSize(Integer.MIN_VALUE);
            pstm.setFetchDirection(ResultSet.FETCH_REVERSE);
            rt = pstm.executeQuery();
            //批量读取end

            //快速插入start
            Long startTime = System.currentTimeMillis();
            conn2 = DriverManager.getConnection(url2, user2, password2);
            String sql2 = "replace INTO "+tableName+"("+fileds+")"+" values("+sb+")";

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
                    System.out.println(tableName+" 第：" + count + " 条数据！");
                }
            }
            pstm2.executeBatch();
            conn2.commit();
            System.out.println(tableName+" 第：" + count + " 条数据！");

            //快速插入start

            Long endTime = System.currentTimeMillis();
            System.out.println(tableName+"表传输完成 用时：" + (endTime - startTime));
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
