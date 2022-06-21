package com.example.project2020.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project2020.Activities.ContactsActivity
import com.example.project2020.Listene.ContactsClickListener
import com.example.project2020.R
import com.example.project2020.user_utilities.Contact
class ContactsAdapter(val contacts: ArrayList<Contact>): RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    private var clickListener: ContactsClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ContactsViewHolder (
        LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
    )

    override fun getItemCount()= contacts.size

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
      holder.bind(contacts[position],clickListener)
    }

    fun setOnItemClickListener(listener: ContactsClickListener){
        clickListener= listener
        notifyDataSetChanged()
    }
    class ContactsViewHolder(view: View) :RecyclerView.ViewHolder(view){
        private var layout =view.findViewById<LinearLayout>(R.id.contactLayout)
        private var nameTV =view.findViewById<TextView>(R.id.contactNameTV)
        private var phoneTV = view.findViewById<TextView>(R.id.contactNumberTV)

        fun bind(contacts: Contact, listener: ContactsClickListener?){
            nameTV.text = contacts.name
            phoneTV.text = contacts.phone
            layout.setOnClickListener{listener?.onContactClicked(contacts.name , contacts.phone)}

        }
    }
}