package com.example.app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mgsoftware.backstackpressedmanager.FragmentAdapter
import com.mgsoftware.backstackpressedmanager.api.FragmentAdapterProvider

open class LoggerFragment : Fragment(), FragmentAdapterProvider {
    override lateinit var fragmentAdapter: FragmentAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (debug)
            println("${javaClass.name}.onAttach")
        fragmentAdapter = FragmentAdapter(this)
        fragmentAdapter.onAttach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (debug)
            println("${javaClass.name}.onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val onCreateView = super.onCreateView(inflater, container, savedInstanceState)
        if (debug)
            println("${javaClass.name}.onCreateView")
        return onCreateView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (debug)
            println("${javaClass.name}.onViewCreated")
    }

    override fun onStart() {
        super.onStart()
        if (debug)
            println("${javaClass.name}.onStart")
    }

    override fun onResume() {
        super.onResume()
        if (debug)
            println("${javaClass.name}.onResume")
    }

    override fun onPause() {
        super.onPause()
        if (debug)
            println("${javaClass.name}.onPause")
    }

    override fun onStop() {
        super.onStop()
        if (debug)
            println("${javaClass.name}.onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (debug)
            println("${javaClass.name}.onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (debug)
            println("${javaClass.name}.onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        if (debug)
            println("${javaClass.name}.onDetach")
        fragmentAdapter.onDetach()
    }

    companion object {
        var debug = true
    }
}