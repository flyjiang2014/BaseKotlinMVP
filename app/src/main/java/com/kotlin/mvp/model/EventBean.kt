package com.kotlin.mvp.model

class EventBean(var event: Int) {
    var data: Any? = null

    constructor(event: Int, data: Any?) : this(event) {
        this.data = data
    }

    companion object {
        const val CHOOSE_BANK_EVENT = 100
        const val BANK_CARD_DELETE_EVENT = 101
        const val BANK_CARD_ADD_EVENT = 102
        const val PIC_DELETE_EVENT = 103
    }

}