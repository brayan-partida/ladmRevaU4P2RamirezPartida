package mx.edu.ittepic.revau4p2ramirezpartida

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var baseDatos = FirebaseFirestore.getInstance()
    var dataLista = ArrayList<String>()
    var listaId = ArrayList<String>()


    //todo-------------crud----------->


//todo storage---------->

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Store = FirebaseStorage.getInstance()
        // storageReference = Store!!.reference
        //todo--------------storage------------------->


        btn_choose_image.setOnClickListener {
            val i = Intent(this, MainActivity2::class.java)
            startActivity(i)
        }
        //btn_upload_image.setOnClickListener { uploadImage() }

        btnInsertar.setOnClickListener {
            insertar()
        }
        alerta("Bienvenido Modifica los elementos")

        baseDatos.collection("evento").addSnapshotListener { querySnapshot,
                                                             firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                //todo:error si es diferente
                Toast.makeText(this, " error no se puede acceder a la consulata", Toast.LENGTH_LONG)
                    .show()
                return@addSnapshotListener
            }
            dataLista.clear()
            listaId.clear()

            for (documento in querySnapshot!!) {
                var cadena = "Nombre: " + documento.getString("nombre") +
                        "\n Domicilio: " + documento.getString("domicilio") +
                        "\n Carrera: " + documento.getString("carrera") +
                        "\n Sueldo: " + documento.getString("sueldo") +
                        "\n Antiguedad: " + documento.getString("antiguedad") +
                        "\n Fecha: " + documento.getString("fecha")
                dataLista.add(cadena)
                listaId.add(documento.id)
            }//todo=for
            if (dataLista.size == 0) {
                dataLista.add("no hay data")

            }
            var adaptador =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataLista)
            lista.adapter = adaptador
        }

        lista.setOnItemClickListener { parent, view, position, id ->
            if (listaId.size == 0) {
                return@setOnItemClickListener
            }
            alertaEliminar(position)
        }
    }

    private fun insertar() {
        var datosInsertar = hashMapOf(
            "nombre" to txtNombre.text.toString(),
            "domicilio" to txtDomicilio.text.toString(),
            "carrera" to txtCarrera.text.toString(),
            "sueldo" to txtSueldo.text.toString(),
            "antiguedad" to txtAntiguedad.text.toString(),
            "fecha" to txtFecha.text.toString()

        )
        baseDatos.collection("evento").add(datosInsertar as Any).addOnSuccessListener {
            Toast.makeText(this, "se inserto el Trabajador", Toast.LENGTH_LONG).show()
            limpiar()
        }
            .addOnFailureListener {
                Toast.makeText(this, " no se inserto el Trabajador", Toast.LENGTH_LONG).show()
            }
    }//todo---------------insertar--------------------->

    fun limpiar() {
        txtNombre.setText("")
        txtDomicilio.setText("")
        txtCarrera.setText("")
        txtSueldo.setText("")
        txtFecha.setText("")
        txtAntiguedad.setText("")
    }//todo---------------limpiar--------------------->


    fun alerta(alerta: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Hola")
        builder.setMessage(alerta)
        builder.setPositiveButton("aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }//todo:----------------Alerta-------------------------->

    private fun alertaEliminar(position: Int) {
        AlertDialog.Builder(this).setTitle("atencion")
            .setMessage("que deseas haceer ${dataLista[position]}")
            .setPositiveButton("eliminar") { d, w ->
                eliminar(listaId[position])

            }

            .setNegativeButton("actualizar") { d, w ->
                llamarVentanaActualizar(listaId[position])
            }
            .setNeutralButton("Cancelar") { d, w -> }
            .show()
    }//todo-----------------------------------AlertaEliminar-------------->

    private fun eliminar(idEliminar: String) {
        baseDatos.collection("evento")
            .document(idEliminar)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "se elimino", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "no se elimino", Toast.LENGTH_LONG).show()

            }

    }//todo---------------Eliminar--------------------->


    private fun llamarVentanaActualizar(idEliminar: String) {
        baseDatos.collection("evento")
            .document(idEliminar)
            .get()

            .addOnSuccessListener {
                var v = Intent(this, MainActivity::class.java)
                v.putExtra("id", idEliminar)
                v.putExtra("nombre", it.getString("nombre"))
                v.putExtra("domicilio", it.getString("domicilio"))
                v.putExtra("carrera", it.getString("carrera"))
                v.putExtra("sueldo", it.getString("sueldo"))
                v.putExtra("antiguedad", it.getString("antiguedad"))
                v.putExtra("fecha", it.getString("fecha"))

                startActivity(v)
            }
            .addOnFailureListener {
                Toast.makeText(this, "no hay conexion", Toast.LENGTH_LONG).show()

            }


    }//todo---------------llamarVentanaActualizar--------------------->

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        // startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }//todo---------------LAUNCHGALLERY----------------------------->


    private fun uploadImage() {


        TODO("Not Implemented")
    }//TODO------------------------UPLOADIMAGE---------------------->


    /*
     override fun onClick(p0: View?) {
         if (p0 === btn_choose_image)
             launchGallery()
         else if (p0 === btn_upload_image)
             uploadFile()

     }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)
         if (requestCode == PICK_IMAGE_REQUEST &&
             resultCode == Activity.RESULT_OK &&
             data != null && data.data != null
         ) {
             filePath = data.data
             try {
                 val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                 imageView!!.setImageBitmap(bitmap)
             } catch (e: IOException) {
                 e.printStackTrace()
             }
         }

     }

     private fun uploadFile() {
         if (filePath != null) {
             val progressDialog = ProgressDialog(this)
             progressDialog.show()


         }

     }*/


}
