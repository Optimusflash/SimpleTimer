package com.optimus.simpletimer

import android.net.Uri
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

class MainActivity : AppCompatActivity(), TimerDialogFragment.OnFragmentInteractionListener {

    private var startValue = 0
    private val disposeBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        progress_bar.visibility = View.GONE



        tv_timer_value.setOnClickListener {
            val dialog = TimerDialogFragment()
            dialog.show(supportFragmentManager, TimerDialogFragment.TAG)
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

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}




