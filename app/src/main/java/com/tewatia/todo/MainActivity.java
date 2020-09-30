package com.tewatia.todo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Output;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity {

    final List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listView = findViewById(R.id.listview);
        final TextAdapter adapter = new TextAdapter();
        readInfo();
        adapter.setData(list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Task")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                adapter.setData(list);
                                saveInfo();
                            }
                        }).setNegativeButton("NO", null)
                        .create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1560bd")));
                dialog.show();
            }
        });

        final Button newTaskButton = findViewById(R.id.newTaskButton);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskInput = new EditText(MainActivity.this);
                taskInput.setSingleLine();
                taskInput.setTextColor(Color.WHITE);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add a New Task").setMessage("Enter your task")
                        .setView(taskInput)
                        .setPositiveButton("Add Task", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.add(taskInput.getText().toString());
                                adapter.setData(list);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1560bd")));
                dialog.show();
            }
        });


        final Button deleteAllTasksButton = findViewById(R.id.deleteAllTasksButton);

        deleteAllTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Dump all?")
                        .setMessage("Seriously?")
                        .setPositiveButton("Dump All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                list.clear();
                                adapter.setData(list);
                                saveInfo();
                            }
                        }).setNegativeButton("NO", null)
                        .create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9800")));
            }
        });

    }
    private void saveInfo(){
        try{
            File file = new File(this.getFilesDir(),"saved");
            FileOutputStream fOut = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));
            for(int i=0;i<list.size();i++){
                bw.write(list.get(i));
                bw.newLine();
            }
            bw.close();
            fOut.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void readInfo(){
       File file = new File(this.getFilesDir(),"saved");
       if(!file.exists()){
           return;
       }
       try{
           FileInputStream is = new FileInputStream(file);
           BufferedReader reader = new BufferedReader(new InputStreamReader(is));
           String line = reader.readLine();
           while(line!=null){
               list.add(line);
               line = reader.readLine();
           }


       }catch(Exception e){
           e.printStackTrace();
       }
    }
    class TextAdapter extends BaseAdapter {

        List<String> list = new ArrayList<>();

        void setData(List<String> mList)
        {
            list.clear();
            list.addAll(mList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount(){return list.size();}
        @Override
        public Object getItem(int position){
            return null;
        }

        @Override
        public long getItemId(int position)
        {return 0;}

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            if(convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item,parent,false);
            }
            final TextView textView = convertView.findViewById(R.id.task);
            textView.setTextColor(Color.WHITE);
            textView.setText(list.get(position));
//            ToggleButton toggleButton = (ToggleButton) convertView.findViewById(R.id.toggle_button);
            return convertView;

        }
    }
}
