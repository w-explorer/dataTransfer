大量数据传输

一：配置mysql  跨库  之间大数据匹配传输（适用于源库和目标库笛卡尔积表名一致，但是表中字段数量不一致）

1.实现自动匹配目标库和源库对应的表与字段

    ex: A 中 有 字段 a,b,c,d  	B中有 字段 a,b,e
    目标：将A中数据传输到B来
    思路：
    	拿到B表的数据源 识别 B表字段信息，再和A表中数据源字段匹配  如果笛卡尔积  就匹配成功
    	
    	  DatabaseMetaData dbmd = conn.getMetaData();
    	 ResultSet DatabaseMetaData.getTables(String catalog,String schema,String tableName,String []type) 
          String tableName = resultSet.getString("TABLE_NAME");
          ResultSet rs = dbmd.getColumns(dbName, schemaPattern, tableName, "%");
    	
    	



2.mysql批量读取大量数据

  

     		String sql = "select "+fileds+" from "+tableName;	      
    		System.out.println(sql);
            pstm = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, 		ResultSet.CONCUR_READ_ONLY);
            pstm.setFetchSize(Integer.MIN_VALUE);
            pstm.setFetchDirection(ResultSet.FETCH_REVERSE);
            rt = pstm.executeQuery();

3.mysql实现批量大数据插入

    String url2 = "...&rewriteBatchedStatements=true";
    
    conn2.setAutoCommit(false); //关闭自动提交事物  改为手动
    
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

            

二：sqlServer  跨库  之间大数据匹配传输

sqlServer  区别mysql



     String url2 = "jdbc:sqlserver://192.168.3.246:1433;DatabaseName=test_01;characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
    
    需要指定  schemaPattern
    ResultSet DatabaseMetaData.getTables(String catalog,String schema,String tableName,String []type) 
          String tableName = resultSet.getString("TABLE_NAME");
          ResultSet rs = dbmd.getColumns(dbName, schemaPattern, tableName, "%");
            


