package io.github.manuelernesto.alc4phase1_2.util

import android.app.Activity
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.github.manuelernesto.alc4phase1_2.controller.ListActivity
import io.github.manuelernesto.alc4phase1_2.model.TravelDeals

class FirebaseUtil {
    companion object {
        lateinit var mDeals: ArrayList<TravelDeals>

        lateinit var mFirebaseDatabase: FirebaseDatabase
        lateinit var mDatabaseReference: DatabaseReference

        lateinit var mAuth: FirebaseAuth
        lateinit var mAuthListener: FirebaseAuth.AuthStateListener

        lateinit var mStorage: FirebaseStorage
        lateinit var mStorageRef: StorageReference

        private var caller: ListActivity? = null

        var firebaseUtil: FirebaseUtil? = null

        var isAdmin: Boolean = false

        val RC_SIGN_IN = 123


        fun openFbReference(ref: String, callerActivity: ListActivity) {
            if (firebaseUtil == null) {
                firebaseUtil = FirebaseUtil()
                mFirebaseDatabase = FirebaseDatabase.getInstance()
                mAuth = FirebaseAuth.getInstance()
                caller = callerActivity
                mAuthListener = FirebaseAuth.AuthStateListener {
                    if (it.currentUser == null) {
                        signIn(callerActivity)
                    } else {
                        val uid = it.uid
                        checkAdmin(uid)
                    }
                    Toast.makeText(
                        callerActivity.baseContext, "Welcome back!", Toast
                            .LENGTH_SHORT
                    ).show()

                }
                connectStorage()
            }
            mDeals = ArrayList()
            mDatabaseReference = mFirebaseDatabase.reference.child(ref)
        }

        private fun checkAdmin(uid: String?) {
            isAdmin = false
            val ref = mFirebaseDatabase.reference.child("administrators").child(uid!!)
            val childEventListener = object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {}

                override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {}

                override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                    isAdmin = true
                    caller?.showMenu()
                }

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            }
            ref.addChildEventListener(childEventListener)

        }

        fun attachListener() {
            mAuth.addAuthStateListener(mAuthListener)
        }

        fun detachListener() {
            mAuth.removeAuthStateListener(mAuthListener)
        }

        fun signIn(callerActivity: ListActivity) {
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            caller = callerActivity

            caller?.startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }

        fun connectStorage() {
            mStorage = FirebaseStorage.getInstance()
            mStorageRef = mStorage.reference.child("deals_picture")
        }
    }


}