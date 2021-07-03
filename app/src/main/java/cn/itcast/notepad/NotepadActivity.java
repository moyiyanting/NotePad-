package cn.itcast.notepad;
//6.实现记事本界面的显示功能
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.itcast.notepad.adapter.NotepadAdapter;
import cn.itcast.notepad.bean.NotepadBean;
import cn.itcast.notepad.database.SQLiteHelper;
//(1)继承Activity
public class NotepadActivity extends Activity {
    //(2)声明控件、数据库、实体信息、适配器
    ListView listView;
    ImageView add;
    List<NotepadBean> list;
    SQLiteHelper mSQLiteHelper;
    NotepadAdapter adapter;
//(3)重写监听点击按钮：跳转
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);//(3).0.加载xml布局文件
        //用于显示便签的列表
        listView = (ListView) findViewById(R.id.listview);//(3).1.与xml控件联系
        add = (ImageView) findViewById(R.id.add);
//(4)点击添加的触发器:匿名内部类实现事件监听(页面跳转)
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotepadActivity.this,
                        RecordActivity.class);//(4).0.从NotepadActivity数据传递到ReacorActivity
                startActivityForResult(intent, 1);//(4).1.跳转到添加记录界面
            }
        });
        initData();//(4).2.初始化数据
    }
//(5)初始化数据：数据库
    protected void initData() {
        mSQLiteHelper = new SQLiteHelper(this); //(5).0.创建数据库
        showQueryData();
//(6)点击列表的触发器:匿名内部类实现事件监听（实现查看记录的功能）
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                NotepadBean notepadBean = list.get(position);//(6).0.获取对应的Item数据
                Intent intent = new Intent(NotepadActivity.this, RecordActivity.class);//(6).1.从NotepadActivity数据传递到ReacordActivity
               //(6).1.把要传递的数据打包
                intent.putExtra("id", notepadBean.getId());
                intent.putExtra("time", notepadBean.getNotepadTime()); //记录的时间
                intent.putExtra("content", notepadBean.getNotepadContent()); //记录的内容
                NotepadActivity.this.startActivityForResult(intent, 1);//(6).2.跳转到修改记录界面
            }
        });
        //删除记事本中的记录
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int
                    position, long id) {
                AlertDialog dialog;//定义对话框，用于提示用户是否删除长按的Item对应的信息
                AlertDialog.Builder builder = new AlertDialog.Builder( NotepadActivity.this)
                        .setMessage("是否删除此事件？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//点击确定时
                                NotepadBean notepadBean = list.get(position);//获取对应的信息
                                if(mSQLiteHelper.deleteData(notepadBean.getId())){//删除对应的id对应的数据库的记录信息
                                    list.remove(position);//删除列表界面上对应的Item
                                    adapter.notifyDataSetChanged();//更新记事本界面
                                    Toast.makeText(NotepadActivity.this,"删除成功",
                                            Toast.LENGTH_SHORT).show();//显示
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//点击取消时
                                dialog.dismiss();//关闭对话框
                            }
                        });
                dialog =  builder.create();//创建对话框
                dialog.show();//显示对话框
                return true;
            }
        });

    }

    private void showQueryData(){
        if (list!=null){
            list.clear();//清除了对象的引用
        }
        //从数据库中查询数据(保存的标签)
        list = mSQLiteHelper.query();//查询数据库中保存的记录数据
        adapter = new NotepadAdapter(this, list);//获得获取的记录数据
        listView.setAdapter(adapter);//调用自定义适配器
    }

//(7)关闭添加、修改页面返回主页面时的显示：
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){//请求的来源1与返回数据来自2
            showQueryData();//再调用一次数据（因为更新了数据）使他显示在主页面的列表里
        }
    }
}

