package zlc.season.rxjava2demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxjava2demo.demo.ChapterNine;
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

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/test.txt");

        // 普遍章节都有4个demo测试.
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ChapterNine.demo4();
//                ChapterSeven.demo5(); // // TODO: 2017/7/27 这个demo有问题.
                ChapterNine.demo1();
            }
        });

        findViewById(R.id.request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ChapterNine.request(96);
                ChapterNine.request(96);
            }
        });

        findViewById(R.id.demo2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChapterNine.demo2();
            }
        });

        findViewById(R.id.demo3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChapterNine.demo3();
            }
        });

        findViewById(R.id.demo4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChapterNine.demo4();
            }
        });

        findViewById(R.id.demo5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChapterSeven.demo5();
            }
        });

        findViewById(R.id.demo6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                practice1();
            }
        });


    }

    public static Subscription mSubscription;

    public static void practice1() {
        Flowable
                .create(new FlowableOnSubscribe<String>() {
                    @Override
                    public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                        try {
                            InputStream is = this.getClass().getClassLoader().getResourceAsStream("assets/test.txt");
                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                            String str;
                            while ((str = br.readLine()) != null && !emitter.isCancelled()) {
                                while (emitter.requested() == 0) {
                                    if (emitter.isCancelled()) {
                                        break;
                                    }
                                }
                                emitter.onNext(str);
                            }
                            br.close();
                            is.close();
                            emitter.onComplete();
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    }
                }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        mSubscription = s;
                        s.request(1); // 异步的,所以这个设置没有用,默认128,界值96.
                    }

                    @Override
                    public void onNext(String string) {
                        System.out.println(string);
                        try {
                            Thread.sleep(1000);
                            mSubscription.request(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


}
