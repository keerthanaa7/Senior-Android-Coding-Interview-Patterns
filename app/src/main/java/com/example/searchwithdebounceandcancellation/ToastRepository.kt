package com.example.searchwithdebounceandcancellation

import com.google.gson.Gson
import kotlinx.coroutines.delay

class ToastRepository {

    suspend fun getMenu(): Toast {
        delay(2000) // Practice the "Async Loader" requirement
        return Gson().fromJson(TOAST_MENU_JSON, Toast::class.java)
    }
}