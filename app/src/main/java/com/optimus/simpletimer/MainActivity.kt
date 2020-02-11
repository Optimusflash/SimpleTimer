package com.optimus.simpletimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var startValue = 0
    private val disposeBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(np_hours) {
            minValue = 0
            maxValue = 99
        }
        with(np_minutes) {
            minValue = 0
            maxValue = 59
        }
        with(np_seconds) {
            minValue = 0
            maxValue = 59
        }

        progress_bar.visibility = View.GONE

        np_hours.setOnValueChangedListener { picker, oldVal, newVal ->
            tv_timer_value.text = newVal.toString()
            startValue = newVal
            progress_bar.max = newVal
        }


        btn_timer_start.setOnClickListener {
            progress_bar.visibility = View.VISIBLE

            val disposable = startTimer(startValue)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({
                    tv_timer_value.text = it.toString()
                    progress_bar.progress = it
                },{

                },{
                    Toast.makeText(this, "Complete", Toast.LENGTH_SHORT).show()
                })


            disposeBag.add(disposable)
        }
    }


    private fun startTimer(value: Int): Observable<Int> {

        return Observable.create { subscriber ->
            for (i in value downTo 0) {
                subscriber.onNext(i)
                Thread.sleep(1000)
            }
            subscriber.onComplete()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        disposeBag.clear()
    }


}




