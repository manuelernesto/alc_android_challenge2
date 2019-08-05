package io.github.manuelernesto.alc4phase1_2.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.github.manuelernesto.alc4phase1_2.R
import io.github.manuelernesto.alc4phase1_2.model.TravelDeals
import io.github.manuelernesto.alc4phase1_2.util.FirebaseUtil

class DealActivity : AppCompatActivity() {

    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference

    private lateinit var mtxtTitle: EditText
    private lateinit var mtxtPrice: EditText
    private lateinit var mtxtDescription: EditText
    private lateinit var mTravelDeals: TravelDeals

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
        setUI()


        val intent = intent
        if (intent.hasExtra("Deal")) {
            mTravelDeals = intent.getSerializableExtra("Deal") as TravelDeals

            mtxtTitle.setText(this.mTravelDeals.title)
            mtxtPrice.setText(this.mTravelDeals.price)
            mtxtDescription.setText(this.mTravelDeals.descripton)
        } else
            mTravelDeals = TravelDeals(null, null, null, null, null)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.save_menu, menu)

        val saveMenu: MenuItem = menu!!.findItem(R.id.save_menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.delete_deal)

        saveMenu.isVisible = FirebaseUtil.isAdmin
        deleteMenu.isVisible = FirebaseUtil.isAdmin
        enanbleEditText(FirebaseUtil.isAdmin)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu -> {
                saveDeal()
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                backToList()
            }
            R.id.delete_deal -> {
                deleteDeal()
                Toast.makeText(
                    this@DealActivity,
                    "Deal deleted",
                    Toast.LENGTH_SHORT
                ).show()
                backToList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUI() {
        FirebaseUtil.openFbReference("traveldeals", ListActivity::class.java.newInstance())
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = FirebaseUtil.mDatabaseReference

        mtxtTitle = findViewById(R.id.txtTitle)
        mtxtPrice = findViewById(R.id.txtPrice)
        mtxtDescription = findViewById(R.id.txtDescription)
    }

    private fun saveDeal() {

        mTravelDeals.title = mtxtTitle.text.toString()
        mTravelDeals.price = mtxtPrice.text.toString()
        mTravelDeals.descripton = mtxtDescription.text.toString()

        if (mTravelDeals.id == null)
            mDatabaseReference.push()
                .setValue(mTravelDeals)
        else
            mDatabaseReference.child(mTravelDeals.id.toString()).setValue(mTravelDeals)
        clean()

    }

    private fun deleteDeal() {
        if (mTravelDeals.id == null)
            Toast.makeText(
                this@DealActivity,
                "Please, save the deal before deleting",
                Toast.LENGTH_SHORT
            ).show()
        else
            mDatabaseReference.child(mTravelDeals.id.toString()).removeValue()
    }

    private fun clean() {
        mtxtTitle.setText("")
        mtxtTitle.requestFocus()
        mtxtPrice.setText("")
        mtxtDescription.setText("")
    }

    private fun backToList() {
        startActivity(Intent(this@DealActivity, ListActivity::class.java))
    }

    private fun enanbleEditText(isEnabled: Boolean) {
        mtxtTitle.isEnabled = isEnabled
        mtxtPrice.isEnabled = isEnabled
        mtxtDescription.isEnabled = isEnabled
    }

}
