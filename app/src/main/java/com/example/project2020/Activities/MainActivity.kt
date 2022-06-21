package com.example.project2020.Activities

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.ActivityChooserView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.project2020.Fragment.ChatsFragment
import com.example.project2020.Fragment.StoryFragment
import com.example.project2020.Fragment.StoryUpdateFragment
import com.example.project2020.Listeners.FailureCallback
import com.example.project2020.R
import com.example.project2020.user_utilities.DATA_USERS
import com.example.project2020.user_utilities.DATA_USER_PHONE
import com.example.project2020.user_utilities.PERMISSION_REQUEST_READ_CONTACTS
import com.example.project2020.user_utilities.REQUEST_NEW_CHAT
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import androidx.fragment.app.Fragment as Fragment1

class MainActivity : AppCompatActivity(), FailureCallback {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private var mSectionPagerAdapter: SectionPagerAdapter? =null
    private val firebaseDB = FirebaseFirestore.getInstance()
    private val chatsFragment =ChatsFragment()
    private val storyUpdateFragment = StoryUpdateFragment()
    private val storyFragment = StoryFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatsFragment.setFailureCallbackListener(this)

        setSupportActionBar(toolbar)
        mSectionPagerAdapter = SectionPagerAdapter(supportFragmentManager)

        container.adapter =mSectionPagerAdapter
        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
        resizeTabs()
        tabs.getTabAt(1)?.select()

        tabs.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
               when(tab?.position){
                   0 ->{fab.hide()}
                   1 ->{fab.show()}
                   2 ->{fab.hide()}
               }
            }

        })
    }

    override fun onUserError() {
        Toast.makeText(this ,"User not found", Toast.LENGTH_SHORT).show()
        startActivity(LoginActivity.newIntent(this))
        finish()
    }
    fun resizeTabs(){
        val layout = (tabs.getChildAt(0) as LinearLayout).getChildAt(0) as LinearLayout
        val layoutParams =layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight=0.4f
        layout.layoutParams= layoutParams
    }
    fun onNewChat(v: View){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
            // permission not granted
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)  ){
                AlertDialog.Builder(this)
                    .setTitle("Contacts permission")
                    .setMessage("This app requires access to your contacts to initiate a concersation.")
                    .setPositiveButton("Ask me"){dialog, which -> requestContactsPermission() }
                    .setNegativeButton("No") {dialog, which ->  }
                    .show()
            }else{
                requestContactsPermission()
            }
        }else{
            // permission is granted
            startNewActivity()

        }
    }
    fun requestContactsPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSION_REQUEST_READ_CONTACTS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
      when(requestCode){
          PERMISSION_REQUEST_READ_CONTACTS ->{
              if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                startNewActivity()
              }
          }
      }
    }
    fun startNewActivity() {
        startActivityForResult(ContactsActivity.newIntent(this), REQUEST_NEW_CHAT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode ==Activity.RESULT_OK){
            when (requestCode){
                REQUEST_NEW_CHAT -> {
                    val name = data?.getStringExtra(PARAM_NAME) ?: ""
                    val phone = data?.getStringExtra(PARAM_PHONE) ?: ""
                    NewUser(name, phone)
                }
            }
        }
    }
    private fun NewUser(name:String, phone:String) {
        if (!name.isNullOrEmpty() && !phone.isNullOrEmpty()){
            firebaseDB.collection(DATA_USERS)
                .whereEqualTo(DATA_USER_PHONE,phone)
                .get()
                .addOnSuccessListener { result ->
                    if(result.documents.size >0) {
                        chatsFragment.newChat(result.documents[0].id)
                    }else{
                        AlertDialog.Builder(this)
                            .setTitle("User not found")
                            .setMessage("$name does not have an account. do you want to send SMS to install this application? ")
                            .setPositiveButton("OK"){ dialog, which ->
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = Uri.parse("sms:$phone")
                                intent.putExtra("sms_body","Hi.Please install my application so we can chat")
                                startActivity(intent)
                            }
                            .setNegativeButton("Cancel",null)
                            .show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "ERROR!.Please try again later", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
        }

    }

    override fun onResume() {
        super.onResume()

        if (firebaseAuth.currentUser == null) {
            startActivity(LoginActivity.newIntent(this))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
       when(item?.itemId) {
           R.id.action_profile -> onProfile()
            R.id.action_logout -> onLogout()
        }

        return super.onOptionsItemSelected(item)
    }
    private fun onLogout(){
        firebaseAuth.signOut()
        startActivity(LoginActivity.newIntent(this))
        finish()

    }
    private fun onProfile(){
        startActivity(ProfileActivity.newIntent(this))
    }

    inner class SectionPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){

        override fun getItem(position: Int): Fragment1 {
            return when(position){
                0->storyUpdateFragment
                1->chatsFragment
                2->storyFragment

                else ->storyFragment
            }

        }

        override fun getCount(): Int {
            return 3
        }

    }

    companion object{
        val PARAM_NAME="Param name"
        val PARAM_PHONE="Param phone"
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}


