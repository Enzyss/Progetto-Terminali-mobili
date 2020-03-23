package com.example.campo

class User{
    var id : Int = 0
    var name : String = ""
    var password : String = ""
    var ora : String = ""
    var giorno : String = ""

    constructor(name:String,password:String) {
        this.name = name
        this.password = password
    }

    constructor(ora:String,giorno:String,anno:String) {
        this.ora = ora
        this.giorno = giorno
    }

    constructor()
}