package io.github.manuelernesto.alc4phase1_2.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import io.github.manuelernesto.alc4phase1_2.R
import io.github.manuelernesto.alc4phase1_2.adapter.DealAdapter
import io.github.manuelernesto.alc4phase1_2.util.FirebaseUtil

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.list_activity_menu, menu)
        val addMenu: MenuItem = menu!!.findItem(R.id.add_travel_deal)
        addMenu.isVisible = FirebaseUtil.isAdmin

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_travel_deal ->
                startActivity(Intent(this@ListActivity, DealActivity::class.java))
            R.id.logout_menu -> AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    FirebaseUtil.attachListener()
                }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        FirebaseUtil.detachListener()
    }

    override fun onResume() {
        super.onResume()
        val rvDeal = findViewById<RecyclerView>(R.id.rvDeals)
        val adapter = DealAdapter(this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvDeal.adapter = adapter
        rvDeal.layoutManager = layoutManager
        FirebaseUtil.attachListener()
    }

    fun showMenu() {
        invalidateOptionsMenu()
    }
}
