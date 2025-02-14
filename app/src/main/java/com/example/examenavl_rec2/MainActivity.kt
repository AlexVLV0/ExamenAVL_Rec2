package com.example.examenavl_rec2

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.practicaexamen.DatabaseHelper


class MainActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var dbHelper: DatabaseHelper
    private val institutos = arrayOf("IES Julián Marias","IES Virgen del Espino", "IES Claudio Moyano")
    private val logos = arrayOf(
        R.drawable.julian_marias_logo,
        R.drawable.virgen_espino_logo,
        R.drawable.claudio_moyano_logo
    )

    private val telefonos = arrayOf(
        "11111111",
        "22222222",
        "33333333"
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val btnLogo = findViewById<ImageButton>(R.id.logoBtn)
        val listaIes = findViewById<ListView>(R.id.iesLista)
        val adaptadorPersonalizado = AdaptadorPersonalizado(this,R.layout.linealista, institutos)
        val telefonoTextView = findViewById<TextView>(R.id.telefonoTV)
        listaIes.adapter = adaptadorPersonalizado

        listaIes.setOnItemClickListener{ _, view, position, _ ->
            telefonoTextView.text = telefonos[position]
            btnLogo.setImageResource(logos[position])


            telefonoTextView.setOnClickListener{
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.setData(Uri.parse("tel:" + telefonoTextView.text))
                val chooser = Intent.createChooser(dialIntent,"Llamar con...")
                startActivity(chooser)
            }


            btnLogo.setOnClickListener{

                val intent = Intent(this, SegundaActivity::class.java)
                intent.putExtra("institutoSeleccionado", institutos[position])
                intent.putExtra("longitudSeleccionada", longitudes[position])
                intent.putExtra("latitudSeleccionada", latitudes[position])
                intent.putExtra("institutos", institutos)
                intent.putExtra("logos", logos)
                intent.putExtra("telefonos", telefonos)
                intent.putExtra("longitudes", longitudes)
                intent.putExtra("latitudes", latitudes)
                startActivity(intent)
            }

        }




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
            val rowView = convertView ?: layoutInflater.inflate(R.layout.linealista, parent, false)

            //Fijamos el nombre de la ciudad
            rowView.findViewById<TextView>(R.id.nombre).text = institutos[position]

            //Fijamos la descripción de la ciudad
            rowView.findViewById<TextView>(R.id.telefono).text = telefonos[position].toString()

            //Fijamos la imagen de la ciudad
            rowView.findViewById<ImageView>(R.id.imagenCiudad).setImageResource(logos[position])

            // Devuelve la vista de fila personalizada
            return rowView
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val btnLogo = view?.findViewById<ImageButton>(R.id.logoBtn)
        if (btnLogo != null) {
            btnLogo.setImageResource(logos[position])
        }
    }


}

