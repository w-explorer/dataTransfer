package org.edu.cdtu.yz.crazy.been;

/**
 * 数据库  表的字段承接
 * @author wenc
 * @create 2019-07-20 9:27
 */
public class Filed {
    private String filed; //字段名
    private String type;  //字段类型

    public String getFiled() {
        return filed;
    }

    public void setFiled(String filed) {
        this.filed = filed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Filed{" +
                "filed='" + filed + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
