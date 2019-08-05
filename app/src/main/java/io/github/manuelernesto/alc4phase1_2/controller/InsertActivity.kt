package io.github.manuelernesto.alc4phase1_2.controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class InsertActivity : AppCompatActivity() {

    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference

    private lateinit var mtxtTitle: EditText
    private lateinit var mtxtPrice: EditText
    private lateinit var mtxtDescription: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
        setUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu -> {
                saveDeal()
                Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUI() {
        FirebaseUtil.openFbReference("traveldeals")
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = FirebaseUtil.mDatabaseReference

        mtxtTitle = findViewById(R.id.txtTitle)
        mtxtPrice = findViewById(R.id.txtPrice)
        mtxtDescription = findViewById(R.id.txtDescription)
    }

    private fun saveDeal() {

        val title = mtxtTitle.text.toString()
        val price = mtxtPrice.text.toString()
        val descripton = mtxtDescription.text.toString()

        val travelMeal = TravelDeals(
            id = null,
            title = title,
            price = price,
            descripton = descripton,
            imageUrl = ""
        )

        mDatabaseReference.push()
            .setValue(travelMeal)
        clean()

    }

    private fun clean() {
        mtxtTitle.setText("")
        mtxtTitle.requestFocus()
        mtxtPrice.setText("")
        mtxtDescription.setText("")
    }

}
