package com.example.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.databinding.FragmentBBinding
import com.mgsoftware.backstackpressedmanager.api.OnBackPressedListener

class FragmentB : LoggerFragment() {
    private lateinit var binding: FragmentBBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBBinding.inflate(inflater, container, false)
        binding.textView.text = this::class.java.simpleName
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val backActionA = object : OnBackPressedListener {

            override fun onBackPressed(): Boolean {
                Log.d("echo", "${FragmentB::class.java.simpleName} backActionA")
                return false
            }
        }
        fragmentAdapter.putRunnable(ACTION_A, backActionA)

        val backActionB = object : OnBackPressedListener {

            override fun onBackPressed(): Boolean {
                Log.d("echo", "${FragmentB::class.java.simpleName} backActionB")
                return true
            }
        }
        fragmentAdapter.putRunnable(ACTION_B, backActionB)

        binding.addActionIgnore.setOnClickListener {
            fragmentAdapter.backStackPressedManager.addToBackStack(
                FragmentB::class.java.name,
                ACTION_A
            )
        }

        binding.addActionHandle.setOnClickListener {
            fragmentAdapter.backStackPressedManager.addToBackStack(
                FragmentB::class.java.name,
                ACTION_B
            )
        }
    }

    companion object {
        private const val ACTION_A = "ignore back"
        private const val ACTION_B = "handle back"

        fun newInstance(): FragmentB {
            return FragmentB()
        }
    }
}
