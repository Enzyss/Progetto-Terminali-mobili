package com.example.campo

import android.annotation.TargetApi
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.vicky.sqliteexample.DataBaseHandler
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.prenotare_campo.*
import kotlinx.android.synthetic.main.prenotazioni_personali.*
import kotlinx.android.synthetic.main.user_registration.*
import java.util.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.admin_cambio_logo.*
import kotlinx.android.synthetic.main.admin_manutenzione.*
import kotlinx.android.synthetic.main.cancella_prenotazioni.*
import kotlinx.android.synthetic.main.contatti.*
import kotlinx.android.synthetic.main.nav_header.*
import java.util.jar.Manifest


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap : GoogleMap
    /* SPINNER */
    lateinit var option : Spinner
    lateinit var result : TextView
    lateinit var mese_option : Spinner
    lateinit var ora_option : Spinner

    lateinit var image : ImageView

    val context = this
    val db = DataBaseHandler(context)

    var testo = ""


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        //showHome()
        showLogIn()
        registration.setOnClickListener {
            showRegistration()
        }



        save.setOnClickListener {

            var user = User(name.text.toString(),password_register.text.toString())
            db.insertData(user)
            showLogIn()
        }

        login.setOnClickListener {
            showLogIn()
        }

        login_button.setOnClickListener {

            val contact = db.getOneName(login_email.text.toString(),login_password.text.toString())
            if(contact != null) {
                println("Sono riuscito ad entrare")
                nome_sx.setText(login_email.text.toString())

                if(contact.name.equals("admin") && contact.password.equals("admin")) {
                    println("Benvenuto capo")
                }

                showPrenotaCampo()
            } else
                Toast.makeText(this,"Email o pass errati" ,Toast.LENGTH_SHORT).show()

            /*
            val contact = login_email.text.toString()
            val pass_correct = login_password.text.toString()
            var data = db.readData()
            for (i in 0..(data.size -1)) {
                if (contact != null) {
                    if (contact.equals(data.get(i).name) && pass_correct.equals(data.get(i).password)) {
                        println("Sono riuscito ad entrare")
                        showPrenotaCampo()
                    } else {
                        println("cazzoooo" + contact)
                        Toast.makeText(this,"Email o pass errati" ,Toast.LENGTH_SHORT).show()
                    }
                }
            }

             */

        }

    }

    companion object {
        private val IMAGE_PICK_CODE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_prenotare -> {
                showPrenotaCampo()
            }
            R.id.nav_prenotazioni_pers -> {
                showPrenotazioniPersonali()
            }
            R.id.nav_cambioLogo -> {
                val contact = db.getOneName(login_email.text.toString(),login_password.text.toString())
                if (contact != null) {
                    if (contact.name.equals("admin") && contact.password.equals("admin")) {
                        showCambioLogo()
                    }else
                        Toast.makeText(this,"Non sei admin non puoi accedere qui" ,Toast.LENGTH_SHORT).show()
                }
            }

            R.id.nav_manutenzione -> {
                val contact = db.getOneName(login_email.text.toString(),login_password.text.toString())
                if (contact != null) {
                    if (contact.name.equals("admin") && contact.password.equals("admin")) {
                        showChangeManutenzione()
                    }else
                        Toast.makeText(this,"Non sei admin non puoi accedere qui" ,Toast.LENGTH_SHORT).show()
                }
            }

            R.id.nav_contatti -> {
                showContatti()
            }
            R.id.nav_cancellare -> {
                showCancella()
            }
            R.id.nav_logout -> {
                showLogIn()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun showRegistration() {
        registration_layout.visibility = View.VISIBLE
        login_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
        prenota_campo.visibility = View.GONE
        prenotazioni_pers.visibility = View.GONE
        contatti_id.visibility = View.GONE
        Contatti.visibility = View.GONE
        ChangeLogo.visibility = View.GONE
        cancellare_prenotazioni.visibility = View.GONE
        changeManutenzione.visibility = View.GONE
    }

    private fun showLogIn() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.VISIBLE
        home_ll.visibility = View.GONE
        prenota_campo.visibility = View.GONE
        prenotazioni_pers.visibility = View.GONE
        contatti_id.visibility = View.GONE
        Contatti.visibility = View.GONE
        ChangeLogo.visibility = View.GONE
        cancellare_prenotazioni.visibility = View.GONE
        changeManutenzione.visibility = View.GONE

    }

    private fun showHome() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.GONE
        home_ll.visibility = View.VISIBLE
        prenota_campo.visibility = View.GONE
        prenotazioni_pers.visibility = View.GONE
        contatti_id.visibility = View.GONE
        Contatti.visibility = View.GONE
        ChangeLogo.visibility = View.GONE
        changeManutenzione.visibility = View.GONE
    }

    private fun showAgenda() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
        agenda_layout.visibility = View.VISIBLE
        contatti_id.visibility = View.GONE
        changeManutenzione.visibility = View.GONE

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showPrenotaCampo() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
        prenota_campo.visibility = View.VISIBLE
        prenotazioni_pers.visibility = View.GONE
        contatti_id.visibility = View.GONE
        Contatti.visibility = View.GONE
        ChangeLogo.visibility = View.GONE
        cancellare_prenotazioni.visibility = View.GONE
        changeManutenzione.visibility = View.GONE


        data_button.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener{datePicker: DatePicker?, year: Int, month: Int, day: Int ->
                cal.set(Calendar.DAY_OF_MONTH,day)
                cal.set(Calendar.MONTH,month)
                cal.set(Calendar.YEAR,year)
                giornoTv.text = SimpleDateFormat("dd:MM").format(cal.time)
                println("prenotato il giorno : " + giornoTv.text.toString())
            }
            val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker: TimePicker?, hour: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE, minute)
                timeTv.text = SimpleDateFormat("HH:mm").format(cal.time)
                println("prenotato alle ore : " + timeTv.text.toString())
            }
            DatePickerDialog(this,dateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true).show()

            calcio_button.setOnClickListener {
                var nome = login_email.text.toString()
                println("prenotato alle ore : " + timeTv.text.toString() + " da parte di " + nome)
                var prenotazioni = Prenotazioni(timeTv.text.toString(),giornoTv.text.toString(),nome,"calcio")
                db.insertPrenotazioni(prenotazioni,nome)
                showPrenotazioniPersonali()

            }
        }

        /* PRENOTARE CAMPO DI CALCIOTTO */
        data_calciotto_button.setOnClickListener {
            val cal1 = Calendar.getInstance()
            val dateSetListener1 = DatePickerDialog.OnDateSetListener{datePicker: DatePicker?, year: Int, month: Int, day: Int ->
                cal1.set(Calendar.DAY_OF_MONTH,day)
                cal1.set(Calendar.MONTH,month)
                cal1.set(Calendar.YEAR,year)
                giornoCalciottoTv.text = SimpleDateFormat("dd:MM").format(cal1.time)
                println("prenotato il giorno : " + giornoCalciottoTv.text.toString())
            }
            val timeSetListener1 = TimePickerDialog.OnTimeSetListener{timePicker: TimePicker?, hour: Int, minute: Int ->
                cal1.set(Calendar.HOUR_OF_DAY,hour)
                cal1.set(Calendar.MINUTE, minute)
                timeCalciottoTv.text = SimpleDateFormat("HH:mm").format(cal1.time)
                println("prenotato alle ore : " + timeCalciottoTv.text.toString())
            }
            DatePickerDialog(this,dateSetListener1,cal1.get(Calendar.YEAR),cal1.get(Calendar.MONTH),cal1.get(Calendar.DAY_OF_MONTH)).show()
            TimePickerDialog(this,timeSetListener1,cal1.get(Calendar.HOUR_OF_DAY),cal1.get(Calendar.MINUTE),true).show()

            calciotto_button.setOnClickListener {
                var nome1 = login_email.text.toString()
                println("prenotato alle ore : " + timeCalciottoTv.text.toString() + " da parte di " + nome1)
                var prenotazioni1 = Prenotazioni(timeCalciottoTv.text.toString(),giornoCalciottoTv.text.toString(),nome1,"calciotto")
                db.insertPrenotazioni(prenotazioni1,nome1)

        //        var cancella = db.cancellaPrenotazioni(prenotazioni1.giorno,prenotazioni1.ora)
        //        for(i in 0..cancella.size-1)
        //            println("Prenotazione cancellata per :" + cancella.get(i).name_esterno)

                showPrenotazioniPersonali()

            }
        }
    }

    private fun showPrenotazioniPersonali() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
        prenota_campo.visibility = View.GONE
        prenotazioni_pers.visibility = View.VISIBLE
        contatti_id.visibility = View.GONE
        Contatti.visibility = View.GONE
        ChangeLogo.visibility = View.GONE
        cancellare_prenotazioni.visibility = View.GONE
        changeManutenzione.visibility = View.GONE

        testo = ""
        var contact = db.getOneName(login_email.text.toString(),login_password.text.toString())
        if (contact != null) {
            if(contact.name.equals("admin") && contact.password.equals("admin")) {
                var allPreno = db.prelevaPrenotazioniAdmin()
                for(i in 0..allPreno.size-1) {
                    testo += "\nCampo: " + allPreno.get(i).tipo + "\nGiorno: " + allPreno.get(i).giorno + " ore: " + allPreno.get(i).ora + " da:"+ allPreno.get(i).name_esterno+ "\n \n"
                }
            }
        }

        val tv_dynamic = TextView(this)
        tv_dynamic.textSize = 20f
        tv_dynamic.text = ""
        PrenotazionePers.addView(tv_dynamic)

        var results = db.prelevaPrenotazioni(login_email.text.toString())
        for (i in 0..results.size-1) {
            testo += "\nCampo: " + results.get(i).tipo + "\nGiorno: " + results.get(i).giorno + " ore: " + results.get(i).ora + " da: " + results.get(i).name_esterno+"\n \n"
            //tv_dynamic.text = "\nCampo: " + results.get(i).tipo + "\nGiorno: " + results.get(i).giorno + " ore: " + results.get(i).ora + "\n \n"
        }
        println(testo)
        prenotazione.setText(testo)

    }

    private fun showContatti() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
        prenota_campo.visibility = View.GONE
        prenotazioni_pers.visibility = View.GONE
        contatti_id.visibility = View.VISIBLE
        changeManutenzione.visibility = View.GONE
/*
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
            val location = LatLng(40.895872,14.5800886)
            googleMap.addMarker(MarkerOptions().position(location).title("Siamo qui"))
        })
*/

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun showCancella() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
        prenota_campo.visibility = View.GONE
        prenotazioni_pers.visibility = View.GONE
        contatti_id.visibility = View.GONE
        Contatti.visibility = View.GONE
        ChangeLogo.visibility = View.GONE
        cancellare_prenotazioni.visibility = View.VISIBLE
        changeManutenzione.visibility = View.GONE


        cancella_data.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener = DatePickerDialog.OnDateSetListener{datePicker: DatePicker?, year: Int, month: Int, day: Int ->
                cal.set(Calendar.DAY_OF_MONTH,day)
                cal.set(Calendar.MONTH,month)
                cal.set(Calendar.YEAR,year)
                giornoCancellaTv.text = SimpleDateFormat("dd:MM").format(cal.time)
               // println("prenotato il giorno : " + giornoTv.text.toString())
            }
            val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker: TimePicker?, hour: Int, minute: Int ->
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE, minute)
                timeCancellaTv.text = SimpleDateFormat("HH:mm").format(cal.time)
            //    println("prenotato alle ore : " + timeTv.text.toString())
            }
            DatePickerDialog(this,dateSetListener,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
            TimePickerDialog(this,timeSetListener,cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),true).show()
        }

        cancel_prenotazione.setOnClickListener {
            var cancellare = db.cancellaPrenotazioni(giornoCancellaTv.text.toString(),timeCancellaTv.text.toString())
            for (i in 0..cancellare.size-1) {
                println("posso cancellare la prenotazione di : " + cancellare.get(i).name_esterno)
                val testoCancella = "\nCampo: " + cancellare.get(i).tipo +"\nGiorno: " + giornoCancellaTv.text.toString() + " ore: " + timeCancellaTv.text.toString() + " da: " + cancellare.get(i).name_esterno
                //println(testoCancella)
                stringRemoveWord(testoCancella)

                showPrenotazioniPersonali()
            }
        }


    }

    private fun showCambioLogo(){
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
        prenota_campo.visibility = View.GONE
        prenotazioni_pers.visibility = View.GONE
        Contatti.visibility = View.GONE
        cancellare_prenotazioni.visibility = View.GONE
        ChangeLogo.visibility = View.VISIBLE
        changeManutenzione.visibility = View.GONE

        previewLogo.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //PERMISSION DENIED
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE);
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else {
                    //permission already granted
                    pickImageFromGallery();
                }
            }
            else {
                //system OS is < MARSHMALLOW
                pickImageFromGallery();
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(Build.VERSION_CODES.N)
    private fun showChangeManutenzione() {
        registration_layout.visibility = View.GONE
        login_layout.visibility = View.GONE
        home_ll.visibility = View.GONE
        prenota_campo.visibility = View.GONE
        prenotazioni_pers.visibility = View.GONE
        Contatti.visibility = View.GONE
        cancellare_prenotazioni.visibility = View.GONE
        ChangeLogo.visibility = View.GONE
        changeManutenzione.visibility = View.VISIBLE

        modifica.setOnClickListener {
            var riflettori = faretti.text.toString()
            println("Cambio i faretti in: " + riflettori)
            var manutenzione = Manutenzione(faretti.text.toString().toInt(),irrigazione.text.toString(),riscaldamento.text.toString())
            if(tipoCampo.equals("calcio"))
                pren_calcio.setText("Tipologia di campo: CALCIO\n Impianto di riscaldamento: "+ riscaldamento.text.toString()+ "\n Impianto di illuminazione:"+ faretti.text.toString()+ " riflettori\n Irrigazione: "+ irrigazione.text.toString() + "\n Stato del campo: naturale")
            else
                pren_calciotto.setText("Tipologia di campo: CALCIOTTO\n Impianto di riscaldamento: "+ riscaldamento.text.toString()+ "\n Impianto di illuminazione:"+ faretti.text.toString()+ " riflettori\n Irrigazione: "+ irrigazione.text.toString() + "\n Stato del campo: naturale")
            showPrenotaCampo()
        }
    }



    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type= "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                }
                else {
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageView.setImageURI(data?.data)
        }
    }

    /* CANCELLAZIONE PRENOTAZIONE */

    fun addText(text: String) : String {
        println("il testo è: "+ text)
        return text
    }

    fun stringRemoveWord(word: String) {

        //val text = "Programming is super fun!!! super super super :D"
        //val word = "super"
        var text = testo

        println("Original text is: \"%s\"".format(text))

        println()

        println("After remove the word \"%s\": \"%s\"".format(word, removeWord(text, word)))

        println()

        println("After remove the word with replace \"%s\": \"%s\"".format(word, removeWordWithReplace(text, word)))
        testo = removeWordWithReplace(testo,word)
    }

    fun removeWord(value: String, word: String): String {

        var result = ""

        var possibleMatch = ""

        var i = 0
        var j = 0

        while ( i in 0 until value.length ) {

            if ( value[i] == word[j] ) {

                if ( j == word.length - 1 ) { // match

                    possibleMatch = "" // discard word
                    j = 0
                }
                else {

                    possibleMatch += value[i]
                    j++
                }
            }
            else {

                result += possibleMatch
                possibleMatch = ""

                if ( j == 0 ) {

                    result += value[i]
                }
                else {

                    j = 0
                    i-- // re-test
                }
            }

            i++
        }

        return result
    }

    fun removeWordWithReplace(value: String, word: String): String {

        return value.replace(word, "")
    }


}

private fun <E> ArrayList<E>.add(element: String) {

}
