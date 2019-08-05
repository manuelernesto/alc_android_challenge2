package io.github.manuelernesto.alc4phase1_2.util

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.github.manuelernesto.alc4phase1_2.model.TravelDeals

class FirebaseUtil {
    companion object {
        lateinit var mDeals: ArrayList<TravelDeals>
        lateinit var mFirebaseDatabase: FirebaseDatabase
        lateinit var mDatabaseReference: DatabaseReference
        var firebaseUtil: FirebaseUtil? = null

        fun openFbReference(ref: String) {
            if (firebaseUtil == null) {
                firebaseUtil = FirebaseUtil()
                mFirebaseDatabase = FirebaseDatabase.getInstance()
                mDeals = ArrayList()
            }
            mDatabaseReference = mFirebaseDatabase.reference.child(ref)
        }
    }


}