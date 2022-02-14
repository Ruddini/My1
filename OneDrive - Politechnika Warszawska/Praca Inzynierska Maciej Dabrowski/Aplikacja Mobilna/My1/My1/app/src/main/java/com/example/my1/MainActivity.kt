package com.example.my1

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1
    val list: ArrayList<BluetoothDevice> = ArrayList()


    companion object {
        val EXTRA_ADDRESS: String = "Device_address"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //sprawdzenie czy usługa Bluetooth jest dostępna
        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (m_bluetoothAdapter == null) {
            Toast.makeText(applicationContext,"To urządzenie nie ma modułu Bluetooth", Toast.LENGTH_SHORT)
        }
        else if (!m_bluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent =Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)     // akcja włączenia Bluetooth (pokazanie akcji że użytkownik pozwolił na włączenie Bluetootha)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }
        pairedDeviceList()//tutaj dodałem to przy otworzeniu aplikacji od razu powinno się pojawić urządzenie do połączenia

        select_device_refresh.setOnClickListener { pairedDeviceList() }

    }

    private fun pairedDeviceList() {
        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices    //wyszukuje urządzenia do sparowania


        //dodaje urządzenia znalezione do listy
        if (m_pairedDevices.size > 0) {
            for (device: BluetoothDevice in m_pairedDevices) {
                list.add(device)

                Log.i("device", "" + device.name) // to chyba nie potrzebne
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Nie znaleziono połączonych urządzeń",
                Toast.LENGTH_SHORT
            )
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)     //tutaj listDevName
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

        val device: BluetoothDevice = list[position]
        val address: String = device.address


        val intent = Intent(this, ControlActivity::class.java)
        intent.putExtra(EXTRA_ADDRESS, address)
        startActivity(intent)
    }
}


    override fun onDestroy() {
        super.onDestroy()
        list.clear()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (m_bluetoothAdapter!!.isEnabled) {
                    Toast.makeText(applicationContext,"Bluetooth has been enabled",Toast.LENGTH_SHORT)
                } else {
                    Toast.makeText(applicationContext,"Bluetooth has been disabled",Toast.LENGTH_SHORT)
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(applicationContext,"Bluetooth enabling has been canceled",Toast.LENGTH_SHORT)
            }
        }
    }

    fun tapToFadeR(view: View) {
        var animation: Animation = AnimationUtils.loadAnimation(this,R.anim.button_anim)
            select_device_refresh.startAnimation(animation)

    }
}