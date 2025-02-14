package com.example.examenavl_rec2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import kotlin.math.*

class SegundaActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val institutos = arrayOf("IES Julián Marias","IES Virgen del Espino", "IES Claudio Moyano")

    private val logos = arrayOf(
        R.drawable.julian_marias_logo,
        R.drawable.virgen_espino_logo,
        R.drawable.claudio_moyano_logo
    )

    private val longitudes = arrayOf(
        20,
        30,
        40
    )

    private val latitudes = arrayOf(
        20,
        30,
        40
    )

    private lateinit var institutoDeseado: String
    private lateinit var latitudDeseada: String
    private lateinit var longitudDeseada: String
    private lateinit var textViewDistancia: TextView
    private lateinit var latitudSeleccionada: String
    private lateinit var longitudSeleccionada: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.segunda_activity)
        val selectorDestino = findViewById<Spinner>(R.id.spinnerdestino)
        val adaptadorDestinos = AdaptadorPersonalizado(this, R.layout.lineaspiner, institutos)
        val btnViajes = findViewById<Button>(R.id.btnViajes)
        selectorDestino.adapter = adaptadorDestinos
        selectorDestino.onItemSelectedListener = this
        textViewDistancia = findViewById(R.id.textViewDistancia)
        textViewDistancia.text = "0"

        latitudSeleccionada = "30"
        longitudSeleccionada = "30"

        btnViajes.setOnClickListener{
            val intent = Intent(this, TerceraActivity::class.java)
            startActivity(intent)
        }






    }

    fun haversineCoroutine(lat1: Double, lon1: Double, lat2: Double, lon2: Double){
        CoroutineScope(Dispatchers.Main).launch{
            val distancia = withContext(Dispatchers.Default){
                haversineDistance(lat1,lon1,lat2,lon2)
                launch(Dispatchers.Main){
                }

            }
        }
    }

    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        // Radio de la Tierra en kilómetros
        val R = 6371.0

        // Convertir las latitudes y longitudes de grados a radianes
        val φ1 = Math.toRadians(lat1)
        val φ2 = Math.toRadians(lat2)
        val λ1 = Math.toRadians(lon1)
        val λ2 = Math.toRadians(lon2)

        // Diferencias de latitud y longitud
        val Δφ = φ2 - φ1
        val Δλ = λ2 - λ1

        // Fórmula de Haversine
        val a = sin(Δφ / 2).pow(2) + cos(φ1) * cos(φ2) * sin(Δλ / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        // Distancia en kilómetros
        return R * c
    }







    private inner class AdaptadorPersonalizado(
        context: Context,
        resource: Int,
        objects: Array<String>
    ) : ArrayAdapter<String>(context, resource, objects) {
        //Constructor de mi adaptador paso el contexto (this)
        // el layout, y los elementos

        /**
         * Reescribo el método getDropDownView para que me devuelva una fila personalizada en la
         * lista desplegable en vez del elemento que se encuentra en esa posición
         * @param posicion
         * @param ViewConvertida
         * @param padre
         * @return
         */
        override fun getDropDownView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {


            // Llama a la función para crear la fila personalizada y la devuelve
            return crearFilaPersonalizada(position, convertView, parent)
        }

        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            // Este método se llama para mostrar una vista personalizada en el elemento seleccionado

            // Llama a la función para crear la fila personalizada y la devuelve
            return crearFilaPersonalizada(position, convertView, parent)
        }

        /**
         * Método que me crea mis filas personalizadas pasando como parámetro la posición
         * la vista y la vista padre
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        private fun crearFilaPersonalizada(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {

            // Crea un objeto LayoutInflater para inflar la vista personalizada desde un diseño XML
            val layoutInflater = LayoutInflater.from(context)

            //Declaro una vista de mi fila, y la preparo para inflarla con datos
            // Los parametros son: XML descriptivo
            // Vista padre
            // Booleano que indica si se debe ceñir a las características del padre
            val rowView = convertView ?: layoutInflater.inflate(R.layout.lineaspiner, parent, false)

            //Fijamos el nombre de la ciudad
            rowView.findViewById<ImageView>(R.id.imagenSpiner).setImageResource(logos[position])

            // Devuelve la vista de fila personalizada
            return rowView
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        institutoDeseado = institutos[position]

        latitudDeseada = latitudes [position].toString()
        longitudDeseada = longitudes [position].toString()

        val fixedLatDes = latitudDeseada.toDouble()
        val fixedLongDes = longitudDeseada.toDouble()
        val fixedLatSel = longitudSeleccionada.toDouble()
        val fixedLongSel = longitudSeleccionada.toDouble()

        textViewDistancia.text =
            haversineCoroutine(fixedLatDes,fixedLongDes, fixedLatSel, fixedLongSel).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }



}




