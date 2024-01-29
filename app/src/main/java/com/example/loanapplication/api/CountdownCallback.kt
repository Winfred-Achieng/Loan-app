package com.example.loanapplication.api


interface CountdownCallback {
    fun onTick(millisUntilFinished: Long)
    fun onFinish()
}
