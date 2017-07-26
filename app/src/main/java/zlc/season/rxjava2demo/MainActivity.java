package zlc.season.rxjava2demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.InterruptedIOException;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import zlc.season.rxjava2demo.demo.ChapterSeven;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";

    static {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (throwable instanceof InterruptedIOException) {
                    Log.d(TAG, "Io interrupted"); // lwj
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 普遍章节都有4个demo测试.
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ChapterNine.demo4();
                ChapterSeven.request(1);
            }
        });

        findViewById(R.id.request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChapterNine.request(96);
                ChapterSeven.demo2();
            }
        });

        findViewById(R.id.demo3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChapterSeven.demo4();
            }
        });

        findViewById(R.id.demo4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChapterSeven.demo5();
            }
        });
    }

}
