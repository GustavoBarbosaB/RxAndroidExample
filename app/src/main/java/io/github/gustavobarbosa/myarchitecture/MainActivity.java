package io.github.gustavobarbosa.myarchitecture;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity
        extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);
        textView = findViewById(R.id.textview);

        btn.setOnClickListener((v) -> {
                doSomeWork();
            });
    }

    /* Using skip operator, it will not emit
     * the first 2 values.
     */
    private void doSomeWork() {
        getObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
               // .skip(2)
                .subscribe(getObserver());
    }

    private Observable<Teste> getObservable() {
        return Observable.just(new Teste("ZERO"),
                new Teste("UM"),
                new Teste("DOIS"),
                new Teste("TRES"),
                new Teste("QUATRO"));
    }

    private Observer<Teste> getObserver() {
        return new Observer<Teste>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(Teste value) {
                textView.append(" onNext : value : " + value.getTeste()+"\n");
                Log.d(TAG, " onNext value : " + value.getTeste()+"\n");
            }

            @Override
            public void onError(Throwable e) {
                textView.append(" onError : " + e.getMessage()+"\n");
                Log.d(TAG, " onError : " + e.getMessage()+"\n");
            }

            @Override
            public void onComplete() {
                textView.append(" onComplete"+"\n");
                Log.d(TAG, " onComplete"+"\n");
            }
        };
    }


}
