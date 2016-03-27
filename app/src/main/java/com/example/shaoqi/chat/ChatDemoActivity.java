package com.example.shaoqi.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatDemoActivity extends ActionBarActivity {
    private Button sendButton=null;
    private EditText contentEditText=null;
    private ListView chatListView=null;
    private List<ChatEntity> chatList=null;
    private ChatAdapter chatAdapter=null;
    private Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();
        contentEditText = (EditText) this.findViewById(R.id.editText);
        sendButton = (Button) this.findViewById(R.id.button);
        chatListView = (ListView) this.findViewById(R.id.listView);

        chatList = new ArrayList<ChatEntity>();
        ChatEntity chatEntity = null;
        for (int i = 0; i < 2; i++) {
            chatEntity = new ChatEntity();
            if (i % 2 == 0) {
                chatEntity.setComeMsg(false);
                chatEntity.setContent("你好");
                chatEntity.setChatTime(calendar.get(Calendar.HOUR_OF_DAY) + "时" + calendar.get(Calendar.MINUTE) + "分");
            } else {
                chatEntity.setComeMsg(true);
                chatEntity.setContent("Hello,Nice to meet you");
                chatEntity.setChatTime(calendar.get(Calendar.HOUR_OF_DAY) + "时" + calendar.get(Calendar.MINUTE) + "分");
            }
            chatList.add(chatEntity);
        }

        chatAdapter = new ChatAdapter(this,chatList);
        chatListView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!contentEditText.getText().toString().equals("")) {
                    //发送消息
                    send();
                }else {
                    Toast.makeText(ChatDemoActivity.this,"消息为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void send(){
        calendar = Calendar.getInstance();
        ChatEntity chatEntity = new ChatEntity();
        chatEntity.setChatTime(calendar.get(Calendar.HOUR_OF_DAY) + "时" + calendar.get(Calendar.MINUTE) + "分");
        chatEntity.setContent(contentEditText.getText().toString().trim());
        chatEntity.setComeMsg(false);
        chatList.add(chatEntity);
        chatAdapter.notifyDataSetChanged();
        chatListView.setSelection(chatList.size() - 1);
        contentEditText.setText("");
    }

    private  class ChatAdapter extends BaseAdapter{
        private Context context = null;
        private List<ChatEntity> chatList = null;
        private LayoutInflater inflater = null;   //这个类要搞清楚
        private int COME_MSG = 0;
        private int TO_MSG = 1;

        public ChatAdapter(Context context,List<ChatEntity> chatList){
            this.context = context;
            this.chatList = chatList;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return chatList.size();
        }

        @Override
        public Object getItem(int position) {
            return chatList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            // 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
            ChatEntity entity = chatList.get(position);
            if (entity.isComeMsg())
            {
                return COME_MSG;
            }else{
                return TO_MSG;
            }
        }

        @Override
        public int getViewTypeCount() {
            // 这个方法默认返回1，如果希望listview的item都是一样的就返回1，我们这里有两种风格，返回2
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ChatHolder chatHolder = null;
            if (convertView == null) {
                chatHolder = new ChatHolder();
                if (chatList.get(position).isComeMsg()) {
                    convertView = inflater.inflate(R.layout.chat_from_item, null);
                } else {
                    convertView = inflater.inflate(R.layout.chat_to_item, null);
                }
                chatHolder.timeTextView = (TextView) convertView.findViewById(R.id.tv_time);
                chatHolder.contentTextView = (TextView) convertView.findViewById(R.id.tv_content);
                chatHolder.userImageView = (ImageView) convertView.findViewById(R.id.iv_user_image);
                convertView.setTag(chatHolder);
            }else {
                chatHolder = (ChatHolder)convertView.getTag();
            }
            chatHolder.timeTextView.setText(chatList.get(position).getChatTime());
            chatHolder.contentTextView.setText(chatList.get(position).getContent());
            chatHolder.userImageView.setImageResource(chatList.get(position).getUserImage());

             return convertView;
        }
        private class ChatHolder{
            private TextView timeTextView;
            private ImageView userImageView;
            private TextView contentTextView;
        }
        }




        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
