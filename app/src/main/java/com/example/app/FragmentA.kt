package com.example.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app.databinding.FragmentABinding
import com.mgsoftware.backstackpressedmanager.api.FragmentAdapterProvider
import com.mgsoftware.backstackpressedmanager.api.OnBackPressedListener

class FragmentA : LoggerFragment(), FragmentAdapterProvider {
    private lateinit var binding: FragmentABinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentABinding.inflate(inflater, container, false)
        binding.textView.text = this::class.java.simpleName
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backAction = object : OnBackPressedListener {

            override fun onBackPressed(): Boolean {
                val handled: Boolean
                if (binding.floatingActionMenu.isOpened) {
                    binding.floatingActionMenu.close(true)
                    handled = true
                    Log.d("echo", "Close floatingActionMenu")
                } else {
                    handled = false
                }
                return handled
            }
        }
        fragmentAdapter.putRunnable(CLOSE_FLOATING_ACTION_MENU_BUTTON, backAction)

        binding.floatingActionMenu.setOnMenuToggleListener { opened ->
            if (opened)
                fragmentAdapter.backStackPressedManager.addToBackStack(
                    FragmentA::class.java.name,
                    CLOSE_FLOATING_ACTION_MENU_BUTTON
                )
        }
    }

    companion object {
        private const val CLOSE_FLOATING_ACTION_MENU_BUTTON = "close floatingActionMenu"

        fun newInstance() = FragmentA()
    }
}
