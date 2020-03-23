package com.example.campo

class Manutenzione {
    var riflettori : Int = 0
    var irrigazione : String = ""
    var riscaldamento : String = ""
    var id : Int = 0

    constructor(riflettori:Int,irrigazione:String,riscaldamento:String) {
        this.riflettori = riflettori
        this.irrigazione = irrigazione
        this.riscaldamento = riscaldamento
    }
    constructor()
}