package com.example.my1

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.control_layout.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*



class ControlActivity: AppCompatActivity() {

    companion object {
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        lateinit var m_address: String
        var m_isConnected: Boolean = false
    }

    var m_isConnected: Boolean = false
    var btAdapter: BluetoothAdapter? = null
//=======================================================================================
    //Animation variables


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.control_layout)

        m_address = intent.getStringExtra(MainActivity.EXTRA_ADDRESS)   // pobranie MAC adresu




        var bgapp : ImageView = findViewById(R.id.bgapp)
        var clover : ImageView = findViewById(R.id.clover)
        lateinit var bganim : Animation
        lateinit var secAnim : Animation
        var textSplash : LinearLayout = findViewById(R.id.textsplash)
        var textHome : LinearLayout = findViewById(R.id.texthome)
        var menu : LinearLayout = findViewById(R.id.menus)
        lateinit var fbutton : Animation


        //tworzenie animacji
        bganim = AnimationUtils.loadAnimation(this, R.anim.bganim)  // załaduj daną animację
        secAnim = AnimationUtils.loadAnimation(this, R.anim.secondanim)

        //translationY(o ile się przesunąć).setDuration(długość trwania przesunięcia).setStartDelay(po jakim czasie zacząc)
        textHome.animate().alpha(0.toFloat()).setDuration(10).setStartDelay(20)
        menu.animate().alpha(0.toFloat()).setDuration(10).setStartDelay(20)
        bgapp.animate().translationY(-1700.toFloat()).setDuration(2000).setStartDelay(1000)
        clover.animate().alpha(0.toFloat()).setDuration(2000).setStartDelay(1300)
        textSplash.animate().translationY(150.toFloat()).alpha(0.toFloat()).setDuration(2000).setStartDelay(1000)
        textHome.animate().translationY(20.toFloat()).alpha(1.toFloat()).setDuration(800).setStartDelay(2500)
        menu.animate().translationY(20.toFloat()).alpha(1.toFloat()).setDuration(800).setStartDelay(3000)

        ConnectToDevice(this).execute() //połączenie z układem
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // aktywacja bluetooth Adaptera
        checkBTState()



        //tutaj akcje guzików

        buttonA.setOnClickListener {
            sendCommand("a")
            Toast.makeText(applicationContext,"Ustawiono parametry podlewania Celozji",Toast.LENGTH_SHORT)
        }
        buttonB.setOnClickListener {
            sendCommand("b")
            Toast.makeText(applicationContext,"Ustawiono parametry podlewania Grubosza",Toast.LENGTH_SHORT)
        }
        buttonC.setOnClickListener {
            sendCommand("c")
            Toast.makeText(applicationContext,"Ustawiono parametry podlewania Kaktusa",Toast.LENGTH_SHORT)
        }
        buttonT.setOnClickListener {
            sendCommand("t")
            Toast.makeText(applicationContext,"Test serwomechanizmu",Toast.LENGTH_SHORT)
        }



        buttonA.setOnLongClickListener {
            openPage("https://aniastarmach.pl/jak-usmazyc-idealny-stek/", "Jak przygotować idealny stek!")
            toast("Jak przygotować idealny stek!")
        }
        buttonB.setOnLongClickListener {
            openPage("https://www.kwestiasmaku.com/dania_dla_dwojga/salatka_grecka/przepis.html", "Jak zrobić sałatkę grecką!")
            toast("JAk zrobić sałatkę grecką!")
        }
        buttonC.setOnLongClickListener {
            openPage("https://www.domowe-wypieki.pl/przepisy/desery/1104-omlet-na-slodko", "Może omlet na słodko?")
            toast("Może omlet na słodko?")
        }
        buttonT.setOnLongClickListener {
            disconnect()
            toast("Rozłączenie układu!")
        }

    }
    fun openPage(adress: String, message: String) :Boolean {
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        var adressYT =adress
        var chennel = Intent( ACTION_VIEW, Uri.parse(adressYT) ) //intencja niejawna Action_view pokazuje to co chcemy zrobić
        startActivity(chennel)
        return false
    }

    private fun sendCommand(input: String) {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
                toast("Wystąpił Błąd")
            }
        }
    }

    private fun disconnect() :Boolean {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
        return false
    }

    private fun checkBTState() {
        if (btAdapter == null) {
            Toast.makeText(baseContext, "Urządzenie nie ma funkcji Bluetooth", Toast.LENGTH_LONG)
                .show()
        } else {
            if (btAdapter!!.isEnabled) {
            } else {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 1)
            }
        }
    }
    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        //wykonywanie połączenia
        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }
/*
    fun tapToFadeA(view: View) {

        var animation: Animation = AnimationUtils.loadAnimation(this,R.anim.button_anim)
        buttonA.startAnimation(animation)
    }
    fun tapToFadeB(view: View) {

        var animation: Animation = AnimationUtils.loadAnimation(this,R.anim.button_anim)
        buttonB.startAnimation(animation)
    }
    fun tapToFadeC(view: View) {

        var animation: Animation = AnimationUtils.loadAnimation(this,R.anim.button_anim)
        buttonC.startAnimation(animation)
    }
    fun tapToFadeT(view: View) {

        var animation: Animation = AnimationUtils.loadAnimation(this,R.anim.button_anim)
        buttonT.startAnimation(animation)
    }

 */
    fun toast(text : String) : Boolean {
        Toast.makeText(applicationContext,text,Toast.LENGTH_SHORT)
        return false
    }

}


