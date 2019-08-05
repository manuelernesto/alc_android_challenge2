package io.github.manuelernesto.alc4phase1_2.controller

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.core.view.isVisible
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import io.github.manuelernesto.alc4phase1_2.R
import io.github.manuelernesto.alc4phase1_2.model.TravelDeals
import io.github.manuelernesto.alc4phase1_2.util.FirebaseUtil

class DealActivity : AppCompatActivity() {

    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mProgressBar: ProgressBar

    private lateinit var mtxtTitle: EditText
    private lateinit var mtxtPrice: EditText
    private lateinit var mtxtDescription: EditText
    private lateinit var mBtnUpload: Button
    private lateinit var mImg: ImageView
    private lateinit var mTravelDeals: TravelDeals
    private var PICTURE_RESULT = 42

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
            showImage(this.mTravelDeals.imageUrl!!)
        } else
            mTravelDeals = TravelDeals(null, null, null, null, null, null)


        mBtnUpload.isVisible = FirebaseUtil.isAdmin
        mBtnUpload.setOnClickListener { selectImage() }
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
        mBtnUpload = findViewById(R.id.btnImage)
        mImg = findViewById(R.id.image)
        mProgressBar =  findViewById(R.id.progressBar)
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
        else {
            mDatabaseReference.child(mTravelDeals.id.toString()).removeValue()
            if (mTravelDeals.imageName != null && mTravelDeals.imageName!!.isNotEmpty()) {
                val picRef = FirebaseUtil.mStorage.getReferenceFromUrl(mTravelDeals.imageUrl!!)
                picRef.delete().addOnSuccessListener {

                }
            }
        }
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

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/jpeg"
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(Intent.createChooser(intent, "Insert Picture"), PICTURE_RESULT)
    }

    private fun showImage(url: String) {
        if (url.isNotEmpty()) {
            val width = Resources.getSystem().displayMetrics.widthPixels
            Picasso
                .get()
                .load(url)
                .resize(width, width * 2 / 3)
                .centerCrop()
                .into(mImg)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mProgressBar.visibility =  ProgressBar.VISIBLE
        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
            val imageUri: Uri = data?.data!!
            val ref: StorageReference = FirebaseUtil.mStorageRef.child(imageUri.lastPathSegment!!)
            ref.putFile(imageUri).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    val url: String = uri.toString()
                    val picName: String = it.storage.path
                    mTravelDeals.imageUrl = url
                    mTravelDeals.imageName = picName
                    showImage(url)
                    mProgressBar.visibility =  ProgressBar.GONE
                }
            }
        }
    }


}
