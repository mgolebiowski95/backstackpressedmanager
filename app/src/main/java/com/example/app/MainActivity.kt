package com.example.app

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.mgsoftware.backstackpressedmanager.ActivityAdapter
import com.mgsoftware.backstackpressedmanager.api.ActivityAdapterProvider
import com.mgsoftware.backstackpressedmanager.api.BackStackChangedListener
import com.mgsoftware.backstackpressedmanager.extras.BackStackPressedManagerDebugLog

class MainActivity : AppCompatActivity(), ActivityAdapterProvider {
    private val activityAdapter = ActivityAdapter()
    private lateinit var backStackChangedListener: BackStackChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityAdapter.onCreate(this, savedInstanceState)

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        if (fm.findFragmentById(R.id.under_layer) == null)
            ft.add(R.id.under_layer, FragmentA.newInstance(), FragmentA::class.java.name)
        if (fm.findFragmentById(R.id.middle_layer) == null)
            ft.add(R.id.middle_layer, FragmentB.newInstance(), FragmentB::class.java.name)
        if (fm.findFragmentById(R.id.over_layer) == null)
            ft.add(R.id.over_layer, FragmentC.newInstance(), FragmentC::class.java.name)
        ft.commit()

        backStackChangedListener = BackStackPressedManagerDebugLog(activityAdapter.backStackPressedManager)
        if (BuildConfig.DEBUG)
            activityAdapter.backStackPressedManager.addOnBackStackChangedListener(backStackChangedListener)
        backStackChangedListener.onBackStackChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (BuildConfig.DEBUG)
            activityAdapter.backStackPressedManager.removeOnBackStackChangedListener(backStackChangedListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activityAdapter.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (!activityAdapter.onBackPressed())
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun getActivityAdapter(): ActivityAdapter {
        return activityAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logcat -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
