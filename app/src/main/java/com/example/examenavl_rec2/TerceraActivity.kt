package com.example.examenavl_rec2

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.practicaexamen.DatabaseHelper


class TerceraActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listaviajes: ListView
    private lateinit var listaViajesLogic: MutableList<Viaje>
    private lateinit var adapter: SimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.tercera_activity)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("FragmentBarCreateAndList", "Solicitando permiso de notificación...")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            } else {
                Log.d("FragmentBarCreateAndList", "Permiso de notificación ya concedido")
            }
        }
        dbHelper = DatabaseHelper(this)
        listaviajes = findViewById(R.id.listaViajes)
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val btnBorrar = findViewById<Button>(R.id.btnBorrar)
        val btnModificar = findViewById<Button>(R.id.btnModificar)

        listaViajesLogic = dbHelper.getAllBars().toMutableList()
        //updateBarList()

        btnModificar.setOnClickListener {
            Toast.makeText(this, "Modificado", Toast.LENGTH_SHORT).show()
        }

        btnBorrar.setOnClickListener {
            val fecha = "Fecha gnerica"
            val hora = "hora generica"
            val notificationHelper = this.let { it1 -> NotificationHelper(it1) }
            if (notificationHelper != null) {
                notificationHelper.showNewBarNotification(fecha, hora)
            }
            Toast.makeText(this, "Borrado", Toast.LENGTH_SHORT).show()
        }
    }
    fun updateBarList() {
        listaViajesLogic = dbHelper.getAllBars().toMutableList()

        // Crear lista de mapas con el nombre y el enlace web
        val dataList = listaViajesLogic.map { viaje ->
            mapOf("fecha" to viaje.fecha, "hora" to viaje.hora, "origen" to viaje.origen, "destino" to viaje.destino)
        }

        // Adaptador para personalizar el ListView
        adapter = SimpleAdapter(
            this,
            dataList,
            R.layout.item_viaje,
            arrayOf("name", "website"),
            intArrayOf(R.id.textViewHora, R.id.textViewFecha, R.id.textViewOrigen, R.id.textViewDestino)
        )

        listaviajes.adapter = adapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("FragmentBarCreateAndList", "Permiso de notificación concedido")
            } else {
                Log.e("FragmentBarCreateAndList", "Permiso de notificación denegado")
                Toast.makeText(
                    this,
                    "Se requieren permisos para notificaciones",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}