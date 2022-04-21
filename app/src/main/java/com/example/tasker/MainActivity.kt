package com.example.tasker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tasker.databinding.ActivityMainBinding
import com.example.tasker.fragments.AddTaskFragment
import com.example.tasker.fragments.HistoryFragment
import com.example.tasker.fragments.HomeFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val historyFragment = HistoryFragment()
        val newTaskFragment = AddTaskFragment()

        makeCurrentFragment(homeFragment)

        binding.nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.ic_home -> makeCurrentFragment(homeFragment)
                R.id.ic_history -> makeCurrentFragment(historyFragment)
                R.id.ic_add -> makeCurrentFragment(newTaskFragment)
            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_layout, fragment)
            val fm = supportFragmentManager
            addToBackStack(null)
            commit()
        }
}