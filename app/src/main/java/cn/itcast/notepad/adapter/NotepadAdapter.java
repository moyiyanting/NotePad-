package cn.itcast.notepad.adapter;
//4.编写记事本界面列表适配器
//(1)继承BaseAdapter
//(2)获取Item总数、对应Item对象、Item对象的Id,对应的Item视图
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.itcast.notepad.R;
import cn.itcast.notepad.bean.NotepadBean;

public class NotepadAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;//XML布局对象
    private List<NotepadBean> list;//实体信息对象

    public NotepadAdapter(Context context, List<NotepadBean> list) {
        this.layoutInflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;//声明当前项的ViewHolder类实例
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.notepad_item_layout, null);//实例化一个对象(布局加载器)
            viewHolder = new ViewHolder(convertView);//实例化类对象
            convertView.setTag(viewHolder);//关联viewholder与convertView
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NotepadBean noteInfo = (NotepadBean) getItem(position);// 获取NotepadBean类实例
        viewHolder.tvNoteoadContent.setText(noteInfo.getNotepadContent());//获取item中的文本视图并设置内容
        viewHolder.tvNotepadTime.setText(noteInfo.getNotepadTime());//获取item中的文本视图并设置时间
        return convertView;
    }

    //(3)把Item的布局放为一个类
    class ViewHolder {
        TextView tvNoteoadContent;
        TextView tvNotepadTime;
//(4)实现数据跟控件的一一对应（在View中找到控件）findViewById
        public ViewHolder(View view) {
            tvNoteoadContent = (TextView) view.findViewById(R.id.item_content);
            tvNotepadTime = (TextView) view.findViewById(R.id.item_time);
        }
    }
}