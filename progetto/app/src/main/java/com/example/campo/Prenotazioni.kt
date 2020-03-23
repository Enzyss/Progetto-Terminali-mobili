package com.example.campo

class Prenotazioni{
    var id : Int = 0
    var ora : String = ""
    var giorno : String = ""
    var name_esterno : String = ""
    var tipo : String = ""

    constructor(ora:String,giorno:String,name_esterno:String,tipo:String) {
        this.ora = ora
        this.giorno = giorno
        this.name_esterno = name_esterno
        this.tipo = tipo
    }

    constructor()
}