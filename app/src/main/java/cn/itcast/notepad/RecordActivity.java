package cn.itcast.notepad;
//8.实现添加记录界面功能
//9.实现修改记录界面功能
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import cn.itcast.notepad.database.SQLiteHelper;
import cn.itcast.notepad.utils.DBUtils;

public class RecordActivity extends Activity implements View.OnClickListener {
    //(1)声明第二界面控件、数据库
    ImageView note_back;
    TextView note_time;
    EditText content;
    ImageView delete;
    ImageView note_save;
    SQLiteHelper mSQLiteHelper;
    TextView noteName;
    String id;
//(2)重写oncreate()方法


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);//(1).0.加载xml布局文件
        note_back = (ImageView) findViewById(R.id.note_back);//(1).1.与xml控件联系
        note_time = (TextView)findViewById(R.id.tv_time);
        content = (EditText) findViewById(R.id.note_content);
        delete = (ImageView) findViewById(R.id.delete);
        note_save = (ImageView) findViewById(R.id.note_save);
        noteName = (TextView) findViewById(R.id.note_name);
        //(1).2选择调用任意“保存”、“清空”、“返回”按钮
        note_back.setOnClickListener(this);
        delete.setOnClickListener(this);
        note_save.setOnClickListener(this);
        initData();
    }
    //(3)初始化数据：数据库
    protected void initData() {
        mSQLiteHelper = new SQLiteHelper(this);//(3).0.创建数据库
        noteName.setText("添加记录");//(3).1.标题修改为“添加记录”
        Intent intent = getIntent();//获取Intent对象
        //如果item里有内容，给个id,如果有id,再次进入标题修改为“修改记录”，且重新写入内容、时间
        if(intent!= null){
            id = intent.getStringExtra("id");
            if (id != null){
                noteName.setText("修改记录");
                content.setText(intent.getStringExtra("content"));
                note_time.setText(intent.getStringExtra("time"));
                note_time.setVisibility(View.VISIBLE);//将时间显示到对应的控件
            }
        }
    }
//(4)监听：“保存”、“清空”、“返回”按钮实现点击事件(对应(1).2)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.note_back://后退键
                finish();//关闭当前页面
                break;
            case R.id.delete://清空键
                content.setText("");//清空输入框的内容
                break;
            case R.id.note_save://保存键
                String noteContent=content.getText().toString().trim();//获取内容
                if (id != null){//修改操作
                    if (noteContent.length()>0){
                        if (mSQLiteHelper.updateData(id, noteContent, DBUtils.getTime())){
                            showToast("修改成功");
                            setResult(2);//修改页面返回结果号2，将添加的数据回传到主界面
                            finish();//关闭当前页面
                        }else {
                            showToast("修改失败");
                        }
                    }else {
                        showToast("修改内容不能为空!");
                    }
                }else {
                    //向数据库中添加数据
                    if (noteContent.length()>0){
                        if (mSQLiteHelper.insertData(noteContent, DBUtils.getTime())){
                            showToast("保存成功");
                            setResult(2);
                            finish();
                        }else {
                            showToast("保存失败");
                        }
                    }else {
                        showToast("修改内容不能为空!");
                    }
                }
                break;
        }
    }

    public void showToast(String message){
        Toast.makeText(RecordActivity.this,message,Toast.LENGTH_SHORT).show();
    }
}
