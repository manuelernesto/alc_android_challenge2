package io.github.manuelernesto.alc4phase1_2.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import io.github.manuelernesto.alc4phase1_2.R
import io.github.manuelernesto.alc4phase1_2.adapter.DealAdapter
import io.github.manuelernesto.alc4phase1_2.model.TravelDeals
import io.github.manuelernesto.alc4phase1_2.util.FirebaseUtil
import java.util.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val rvDeal = findViewById<RecyclerView>(R.id.rvDeals)
        val adapter = DealAdapter()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvDeal.adapter = adapter
        rvDeal.layoutManager = layoutManager

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.list_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_travel_deal ->
                startActivity(Intent(this@ListActivity, InsertActivity::class.java))

        }
        return super.onOptionsItemSelected(item)
    }
}
