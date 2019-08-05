package io.github.manuelernesto.alc4phase1_2.adapter

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import io.github.manuelernesto.alc4phase1_2.R
import io.github.manuelernesto.alc4phase1_2.controller.DealActivity
import io.github.manuelernesto.alc4phase1_2.controller.ListActivity
import io.github.manuelernesto.alc4phase1_2.model.TravelDeals
import io.github.manuelernesto.alc4phase1_2.util.FirebaseUtil

class DealAdapter(activity: ListActivity) : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    var deals: ArrayList<TravelDeals>
    private var mFirebaseDatabase: FirebaseDatabase
    private var mDatabaseReference: DatabaseReference
    private var mChildListener: ChildEventListener

    init {
        FirebaseUtil.openFbReference("traveldeals", activity)
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = FirebaseUtil.mDatabaseReference
        deals = FirebaseUtil.mDeals
        val childEventListener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val td = dataSnapshot.getValue(TravelDeals::class.java)
                td?.id = dataSnapshot.key
                td?.let {
                    deals.add(it)
                    notifyItemInserted(deals.size)
                }

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

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


    inner class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        var tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        var imgDeal: ImageView = itemView.findViewById(R.id.imageDeal)

        fun bind(deal: TravelDeals) {
            tvTitle.text = deal.title
            tvPrice.text = deal.price
            tvDescription.text = deal.descripton
            showImage(deal.imageUrl!!)
        }

        private fun showImage(url: String) {
            if (url.isNotEmpty()) {
                Picasso
                    .get()
                    .load(url)
                    .resize(160, 160)
                    .centerCrop()
                    .into(imgDeal)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            val selectedDeal = deals[position]
            val intent = Intent(itemView.context, DealActivity::class.java)
            intent.putExtra("Deal", selectedDeal)
            itemView.context.startActivity(intent)
        }
    }
}