package com.example.demoactivity.content.resolver;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demoactivity.R;
import com.example.demoactivity.content.DemoTable;

public class ContentResolverActivity extends AppCompatActivity {

    private EditText nameText;
    private EditText ageText;
    private EditText sexText;
    private EditText idEntry;
    private TextView labelView;
    private TextView displayView;
    private Button add;
    private Button queryAll;
    private Button deleteAll;
    private Button update;
    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        resolver = getContentResolver();
        initView();
        initEvent();
    }

    private void initView(){
        nameText = findViewById(R.id.et_name);
        ageText = findViewById(R.id.et_age);
        sexText = findViewById(R.id.et_sex);
        idEntry = findViewById(R.id.et_id);
        labelView = findViewById(R.id.labelView);
        displayView = findViewById(R.id.displayView);
        add = findViewById(R.id.btn_add);
        queryAll = findViewById(R.id.btn_queryALL);
        deleteAll = findViewById(R.id.btn_delAll);
        update = findViewById(R.id.btn_update);
    }

    private void initEvent() {

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(DemoTable.KEY_ID, Integer.parseInt(idEntry.getText().toString()));
                values.put(DemoTable.KEY_AGE, Integer.parseInt(ageText.getText().toString()));
                values.put(DemoTable.KEY_NAME, nameText.getText().toString());
                values.put(DemoTable.KEY_SEX, sexText.getText().toString());
                Uri newUri = resolver.insert(DemoTable.CONTENT_URI, values);
                labelView.setText("添加成功， URI：" + newUri);
            }
        });

        queryAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = resolver.query(DemoTable.CONTENT_URI, new String[]{DemoTable.KEY_ID, DemoTable.KEY_NAME, DemoTable.KEY_AGE, DemoTable.KEY_SEX},
                        null, null, null);
                if (cursor == null) {
                    labelView.setText("数据库中没有数据");
                    return;
                }
                labelView.setText("数据库：" + String.valueOf(cursor.getCount()) + "条记录");
                String msg = "";
                if (cursor.moveToFirst()) {
                    do {
                        msg += "ID:" + cursor.getString(cursor.getColumnIndex(DemoTable.KEY_ID)) + ",";
                        msg += "NAME:" + cursor.getString(cursor.getColumnIndex(DemoTable.KEY_NAME)) + ",";
                        msg += "AGE:" + cursor.getInt(cursor.getColumnIndex(DemoTable.KEY_AGE)) + ",";
                        msg += "SEX:" + cursor.getString(cursor.getColumnIndex(DemoTable.KEY_SEX)) + ",";
                    } while (cursor.moveToNext());
                }
                displayView.setText(msg);
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolver.delete(DemoTable.CONTENT_URI, null, null);
                labelView.setText("数据库全部删除");
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(DemoTable.KEY_NAME, nameText.getText().toString());
                values.put(DemoTable.KEY_AGE, Integer.parseInt(ageText.getText().toString()));
                values.put(DemoTable.KEY_SEX, sexText.getText().toString());
                Uri uri = Uri.parse(DemoTable.CONTENT_URI_STRING + "/" + idEntry.getText().toString());
                int result = resolver.update(uri, values, null, null);
                String msg = "更新ID为" + idEntry.getText().toString() + "的数据" + (result > 0 ? "成功" : "失败");
                labelView.setText(msg);
            }
        });
    }
}
