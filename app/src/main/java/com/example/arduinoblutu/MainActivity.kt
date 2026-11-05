package com.example.arduinoblutu

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

class MainActivity : AppCompatActivity() {

    // NUMERO DE GRUPO !!!!!!!!!!! DESPUES CAMBIAR
    private val numGrupo = "Grupo07"
    private val numPassword = "1234"

    private var salida: OutputStream? = null
    var entrada: InputStream? = null

    private lateinit var btnConectar: Button
    private lateinit var btnEnviar: Button
    private lateinit var etMensaje: EditText
    private lateinit var tvLog: TextView

    private fun conectarBluetooth() {

        val adp = BluetoothAdapter.getDefaultAdapter()

        // DISPOISTIVO BLUETOOTH CON EL NOMBRE DEL GRUPO
        val device = adp.bondedDevices.first {
            if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return }
            it.name == numGrupo
        }

        // Android — botón Conectar
        val uuid = UUID.fromString("00001101-...34FB")
        val socketBT = device.createRfcommSocketToServiceRecord(uuid)
        socketBT?.connect()

        salida = socketBT?.outputStream
        tvLog.append("Conectado por Bluetooth\n")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // PEDIR PERMISOS DE BLUETOOTH
        val permBT = Manifest.permission.BLUETOOTH
        if (Build.VERSION.SDK_INT >= 31 &&
            checkSelfPermission(permBT) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permBT), 1)
        }

        // REFS DE ACTIVITY MAIN
        btnConectar = findViewById(R.id.btnConectar)
        btnEnviar = findViewById(R.id.btnEnviar)
        etMensaje = findViewById(R.id.etMensaje)
        tvLog = findViewById(R.id.tvLog)

        // botón Enviar
        btnConectar.setOnClickListener { conectarBluetooth() }
        btnEnviar.setOnClickListener {
            val texto = etMensaje.text.toString() + "\n"
            salida?.write(texto.toByteArray())
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

//    fun conectarWifi()
//    {
//        /* Ejercicio 3 */
//    }

}