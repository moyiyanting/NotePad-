package cn.itcast.notepad.utils;
//5.创建数据库
//(1)创建DBUtils类
//(2)定义数据库的名称、表名、数据库版本、数据库表中的列名以及获取当前日期等消息

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBUtils {
    public static final String DATABASE_NAME = "Notepad";//数据库名
    public static final String DATABASE_TABLE = "Note";  //表名
    public static final int DATABASE_VERION = 1;          //数据库版本
    //数据库表中的列名
    public static final String NOTEPAD_ID = "id";//记录的id
    public static final String NOTEPAD_CONTENT = "content";//记录的内容
    public static final String NOTEPAD_TIME = "notetime";//保存记录的时间
    //获取当前日期
    public static final String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");//日期格式化
        Date date = new Date(System.currentTimeMillis());//获取当前时间
        return simpleDateFormat.format(date);
    }
}
