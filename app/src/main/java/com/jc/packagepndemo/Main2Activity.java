package com.jc.packagepndemo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Main2Activity extends AppCompatActivity {
    String tag = "Main2Activity";

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tv = (TextView) findViewById(R.id.tv);

//        test1();

//        test2();

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                test3();

            }
        });

//        test4();

//        test5();

        test6();
    }

    private void test6() {

    }

    /**
     * flatMap ,将from中的类型转换成Observable<Integer>;1对n的关系
     */
    private void test5() {
        Integer[][] arrs = {{11,12},{21,22},{31,32}};
        Observable.from(arrs)
                .flatMap(new Func1<Integer[], Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer[] ints) {
                        return Observable.from(ints);
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer i) {
                        Log.d(tag, i.toString());
                    }
                });
    }

    /**
     * map ,将just中的类型转换成Drawable;1对1的关系
     */
    private void test4() {
        Observable.just(R.mipmap.ic_launcher)
                .map(new Func1<Integer, Drawable>() {
                    @Override
                    public Drawable call(Integer id) {
                        return getDrawable(id);
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Drawable>() {
                    @Override
                    public void call(Drawable drawable) {
                        Log.d(tag, "test");
//                        tv.setCompoundDrawablesWithIntrinsicBounds(drawable,drawable,drawable,drawable);
                        tv.setCompoundDrawables(drawable,drawable,drawable,drawable);
                    }
                });

        Observable.just(1,2,3)
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer i) {
                        return i.toString();
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String Str) {
                        Log.d(tag, Str);
                    }
                });
    }

    private Drawable getDrawable(Integer id) {
        Drawable drawable = getResources().getDrawable(id);
        drawable.setBounds(0, 0, 20,20);
        return drawable;
    }

    /**
     * 线程控制
     */
    private void test3() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("test one");
                SystemClock.sleep(2000);
                subscriber.onNext("test two");
                SystemClock.sleep(2000);
                subscriber.onNext("test three");
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(tag, s);
                        tv.setText(s);

                    }
                });

    }

    /**
     * just和from的使用
     */
    private void test2() {

        Observable.just("Hello", "Hi", "Aloha").subscribe(new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Log.d(tag, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(tag, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(tag, "Error!");
            }
        });

        String[] words = {"Hello", "Hi", "Aloha"};
        Observable.from(words).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(tag, s);
            }
        });
    }

    /**
     * Observable和Observer、Subscriber的使用
     */
    private void test1() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onNext(String s) {
                Log.d(tag, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(tag, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(tag, "Error!");
            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Log.d(tag, "Item: " + s);
            }

            @Override
            public void onCompleted() {
                Log.d(tag, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(tag, "Error!");
            }
        };

        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onNext("Aloha");
                subscriber.onCompleted();
            }
        });

        observable.subscribe(observer);
        // 或者：
        observable.subscribe(subscriber);

    }


}
