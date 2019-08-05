package io.github.manuelernesto.alc4phase1_2.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import io.github.manuelernesto.alc4phase1_2.R
import io.github.manuelernesto.alc4phase1_2.model.TravelDeals
import io.github.manuelernesto.alc4phase1_2.util.FirebaseUtil

class DealAdapter : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    private var deals: ArrayList<TravelDeals>
    private var mFirebaseDatabase: FirebaseDatabase
    private var mDatabaseReference: DatabaseReference
    private var mChildListener: ChildEventListener

    init {
        FirebaseUtil.openFbReference("traveldeals")
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = FirebaseUtil.mDatabaseReference
        deals = FirebaseUtil.mDeals
        val childEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val td = dataSnapshot.getValue(TravelDeals::class.java)
                td?.id = dataSnapshot.key
                td?.let {
                    deals.add(it)
                    notifyItemInserted(deals.size)
                }

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
        mChildListener = childEventListener
        mDatabaseReference.addChildEventListener(mChildListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.rv_row, parent, false)
        return DealViewHolder(view)
    }

    override fun getItemCount() = deals.size

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal: TravelDeals = deals[position]
        holder.bind(deal)
    }


    class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        var imgDeal: ImageView = itemView.findViewById(R.id.imageDeal)
        fun bind(deal: TravelDeals) {
            tvTitle.text = deal.title
            tvPrice.text = deal.price
            tvDescription.text = deal.descripton
        }
    }
}