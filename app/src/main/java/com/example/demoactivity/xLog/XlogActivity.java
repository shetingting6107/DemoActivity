package com.example.demoactivity.xLog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.PathUtils;
import com.elvishew.xlog.BuildConfig;
import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.Logger;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.DefaultFlattener;
import com.elvishew.xlog.formatter.border.DefaultBorderFormatter;
import com.elvishew.xlog.formatter.message.json.DefaultJsonFormatter;
import com.elvishew.xlog.formatter.message.throwable.DefaultThrowableFormatter;
import com.elvishew.xlog.formatter.message.xml.DefaultXmlFormatter;
import com.elvishew.xlog.formatter.stacktrace.DefaultStackTraceFormatter;
import com.elvishew.xlog.formatter.thread.DefaultThreadFormatter;
import com.elvishew.xlog.interceptor.BlacklistTagsFilterInterceptor;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.ConsolePrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy;
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;
import com.example.demoactivity.R;

public class XlogActivity extends AppCompatActivity {

    /**
     * 最多保留7天
     */
    private static final long MAX_TIME = 1000 * 60 * 60 * 24 * 7;

    private String content = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlog);
        initLog2();
        initView();
        printTest();
    }

    private void initView() {
        final TextView tv_content = findViewById(R.id.tv_text);
        TextView tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XlogActivity.this.finish();
            }
        });

        TextView print_info = findViewById(R.id.print_info);
        final EditText et_info = findViewById(R.id.et_info);
        print_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = et_info.getText().toString();
                if (!TextUtils.isEmpty(info)) {
                    content = content + "INFO: " + info + "\n";
                    tv_content.setText(content);
                    et_info.setText("");
                    XLog.i(info);
                }
            }
        });

        TextView print_error = findViewById(R.id.print_error);
        final EditText et_error = findViewById(R.id.et_error);
        print_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = et_error.getText().toString();
                if (!TextUtils.isEmpty(error)) {
                    content = content + "ERROR: " + error + "\n";
                    tv_content.setText(content);
                    et_error.setText("");
                    XLog.e(error);
                }
            }
        });

        TextView print_warn = findViewById(R.id.print_warn);
        final EditText et_warn = findViewById(R.id.et_warn);
        print_warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String warn = et_warn.getText().toString();
                if (!TextUtils.isEmpty(warn)) {
                    content = content + "WARN: " + warn + "\n";
                    tv_content.setText(content);
                    et_warn.setText("");
                    XLog.w(warn);
                }
            }
        });

        TextView print_debug = findViewById(R.id.print_debug);
        final EditText et_debug = findViewById(R.id.et_debug);
        print_debug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String debug = et_debug.getText().toString();
                if (!TextUtils.isEmpty(debug)) {
                    content = content + "DEBUG: " + debug + "\n";
                    tv_content.setText(content);
                    et_debug.setText("");
                    XLog.d(debug);
                }
            }
        });
    }

    private void initLog() {
        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(BuildConfig.DEBUG ? LogLevel.ALL             // Specify log level, logs below this level won't be printed, default: LogLevel.ALL 指定日志级别，此级别以下的日志将不会打印，默认值:LogLevel.ALL
                        : LogLevel.NONE)
                .tag("MY_TAG")                                         // Specify TAG, default: "X-LOG" 指定标签，默认:“X-LOG”
                .enableThreadInfo()                                    // Enable thread info, disabled by default 启用线程信息，默认禁用
                .enableStackTrace(2)                                   // Enable stack trace info with depth 2, disabled by default 启用深度为2的堆栈跟踪信息，默认禁用
                .enableBorder()                                        // Enable border, disabled by default 启用边框，默认禁用
                .jsonFormatter(new DefaultJsonFormatter())                  // Default: DefaultJsonFormatter 默认值:DefaultJsonFormatter
                .xmlFormatter(new DefaultXmlFormatter())                    // Default: DefaultXmlFormatter 默认值:DefaultXmlFormatter
                .throwableFormatter(new DefaultThrowableFormatter())        // Default: DefaultThrowableFormatter 默认值:DefaultThrowableFormatter
                .threadFormatter(new DefaultThreadFormatter())              // Default: DefaultThreadFormatter 默认值:DefaultThreadFormatter
                .stackTraceFormatter(new DefaultStackTraceFormatter())      // Default: DefaultStackTraceFormatter 默认值:DefaultStackTraceFormatter
                .borderFormatter(new DefaultBorderFormatter())               // Default: DefaultBorderFormatter 默认值:DefaultBorderFormatter
//                .addObjectFormatter(AnyClass.class,                    // Add formatter for specific class of object 为对象的特定类添加格式化器
//                        new AnyClassObjectFormatter())                     // Use Object.toString() by default 默认情况下使用Object.toString()
                .addInterceptor(new BlacklistTagsFilterInterceptor(    // Add blacklist tags filter 添加黑名单标签过滤器
                        "blacklist1", "blacklist2", "blacklist3"))
//                .addInterceptor(new MyInterceptor())                   // Add other log interceptor 添加其他日志拦截器
                .build();

        Printer androidPrinter = new AndroidPrinter(true);         // Printer that print the log using android.util.Log 使用android.util.Log打印日志的打印机
        Printer consolePrinter = new ConsolePrinter();             // Printer that print the log to console using System.out 使用System.out将日志打印到控制台的打印机
//        Printer filePrinter = new FilePrinter                      // Printer that print(save) the log to file 打印(保存)日志到文件的打印机
//                .Builder(PathUtils.getExternalAppCachePath() + "/log/")                         // Specify the directory path of log file(s) 指定日志文件的目录路径
//                .fileNameGenerator(new DateFileNameGenerator())        // Default: ChangelessFileNameGenerator("log") 默认值:ChangelessFileNameGenerator(“日志”)
//                .backupStrategy(new NeverBackupStrategy())             // Default: FileSizeBackupStrategy(1024 * 1024) 默认:FileSizeBackupStrategy(1024 * 1024)
//                .cleanStrategy(new FileLastModifiedCleanStrategy(MAX_TIME))     // Default: NeverCleanStrategy() 默认值:NeverCleanStrategy ()
//                .flattener(new DefaultFlattener())                          // Default: DefaultFlattener 默认值:DefaultFlattener
//                .build();


        Printer filePrinter = new FilePrinter                      // Printer that print(save) the log to file 打印(保存)日志到文件的打印机
                .Builder(PathUtils.getExternalAppCachePath() + "/log/")// Specify the directory path of log file(s) 指定日志文件的目录路径
                .fileNameGenerator(new DateFileNameGenerator()) //自定义文件名称 默认值:ChangelessFileNameGenerator(“日志”)
                .backupStrategy(new NeverBackupStrategy()) //单个日志文件的大小默认:FileSizeBackupStrategy(1024 * 1024)
                .cleanStrategy(new FileLastModifiedCleanStrategy(  30L * 24L * 60L * 60L * 1000L))  //日志文件存活时间，单位毫秒
                .flattener(new DefaultFlattener()) //自定义flattener，控制打印格式
                .build();

        XLog.init(                                                 // Initialize XLog 初始化XLog
                config,                                                // Specify the log configuration, if not specified, will use new LogConfiguration.Builder().build() 指定日志配置，如果未指定，将使用新的LogConfiguration.Builder().build()
                androidPrinter,                                        // Specify printers, if no printer is specified, AndroidPrinter(for Android)/ConsolePrinter(for java) will be used. 指定打印机，如果没有指定打印机，AndroidPrinter(用于Android)/ConsolePrinter(用于java)将被使用。
//                consolePrinter,
                filePrinter);

        testLog();
    }

    private void initLog2() {
        LogConfiguration config = new LogConfiguration.Builder()
                .logLevel(LogLevel.ALL)          // 指定日志级别，低于该级别的日志将不会被打印，默认为 LogLevel.ALL
                .tag("MY_TAG")                                         // 指定 TAG，默认为 "X-LOG"
                .enableThreadInfo()                                    // 允许打印线程信息，默认禁止
                .enableStackTrace(2)                                   // 允许打印深度为 2 的调用栈信息，默认禁止
                .enableBorder()                                        // 允许打印日志边框，默认禁止
                .build();

        Printer androidPrinter = new AndroidPrinter(true);         // 通过 android.util.Log 打印日志的打印器
//        Printer consolePrinter = new ConsolePrinter();             // 通过 System.out 打印日志到控制台的打印器
        Printer filePrinter = new FilePrinter                      // 打印日志到文件的打印器
                .Builder(PathUtils.getExternalAppCachePath() + "/log/")                             // 指定保存日志文件的路径
                .fileNameGenerator(new FileNameConfig())        // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
                .backupStrategy(new NeverBackupStrategy())             // 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
                .cleanStrategy(new FileLastModifiedCleanStrategy(MAX_TIME))     // 指定日志文件清除策略，默认为 NeverCleanStrategy()
                .flattener(new MyFlattener())                          // 指定日志平铺器，默认为 DefaultFlattener
                .build();

        XLog.init(                                                 // 初始化 XLog
                config,                                                // 指定日志配置，如果不指定，会默认使用 new LogConfiguration.Builder().build()
                androidPrinter,
//                consolePrinter,
                filePrinter);

        testLog();
    }

    private void initLog1() {
        XLog.init(LogLevel.ALL);
        testLog();
    }

    private void testLog() {
        XLog.v("hello xlog");
        XLog.d("hello xlog");
        XLog.i("hello xlog");
        XLog.w("hello xlog");
        XLog.e("hello xlog");
        XLog.d("你好%s, 我今年%d岁了", "Tom", 20);
        Intent intent = getIntent();
        XLog.d(intent);
    }

    private void printTest() {
        Logger logger = XLog.tag("TAG-A")
                .build();
        logger.d("定制了TAG的消息");
    }
}