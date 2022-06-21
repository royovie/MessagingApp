package com.example.project2020.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project2020.Listene.ContactsClickListener
import com.example.project2020.R
import com.example.project2020.adapters.ContactsAdapter
import com.example.project2020.user_utilities.Contact
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactsActivity : AppCompatActivity(), ContactsClickListener {


    private val contactsList = ArrayList<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        getContacts()
    }
    private fun getContacts(){
        progressLayout.visibility = View.VISIBLE
        contactsList.clear()
        val newList = ArrayList<Contact>()
        val phone = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,null)
        while (phone!!.moveToNext()){
            val name = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val phoneNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            newList.add(Contact(name, phoneNumber))

        }
        contactsList.addAll(newList)
        phone.close()

        setupList()

    }
    fun setupList() {
        progressLayout.visibility = View.GONE
        val contactsAdapter = ContactsAdapter(contactsList)
        contactsAdapter.setOnItemClickListener(this)
        contactsRV.apply{
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(context)
            adapter = contactsAdapter
            addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        }
    }

    override fun onContactClicked(name: String?, phone: String?) {
       val intent = Intent()
        intent.putExtra(MainActivity.PARAM_NAME,name)
        intent.putExtra(MainActivity.PARAM_PHONE,phone)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    companion object{
        fun newIntent(context: Context) = Intent(context, ContactsActivity::class.java)
    }
}
