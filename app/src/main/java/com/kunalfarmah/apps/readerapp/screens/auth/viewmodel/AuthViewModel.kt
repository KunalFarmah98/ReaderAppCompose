package com.kunalfarmah.apps.readerapp.screens.auth.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.kunalfarmah.apps.readerapp.model.MUser
import com.kunalfarmah.apps.readerapp.screens.auth.data.LoadingState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AuthViewModel() : ViewModel() {
    val TAG = "AuthViewModel"
    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        setError: (String) -> Unit,
        home: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // save user
                            val displayName = it.result.user?.email?.split("@")?.get(0) ?: "user"
                            createUser(displayName)
                            //Navigate to Home
                            home()
                        } else {
                            setError("Could not create account")
                            Log.d(TAG, "signInWithEmailAndPassword: ${it.result.toString()}")
                        }
                    }
                    .addOnFailureListener { }
            } catch (e: Exception) {
                setError("Could not create account")
                Log.d(TAG, "createUserWithEmailAndPassword: ${e.message}")
            }
        }
    }

    fun signInWithEmailAndPassword(
        email: String,
        password: String,
        setError: (String) -> Unit,
        home: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            //Navigate to Home
                            home()
                        } else {
                            setError("Account Does Not Exist")
                            Log.d(TAG, "signInWithEmailAndPassword: ${it.result.toString()}")
                        }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "signInWithEmailAndPassword: ${it.message}")
                    }
            } catch (e: Exception) {
                setError("Account Does Not Exist")
                Log.d(TAG, "signInWithEmailAndPassword: ${e.message}")
            }
        }
    }

    private fun createUser(name: String) {
        val uuid = auth.currentUser?.uid
        val user = MUser(uid = uuid.toString(), name = name, avatarUrl = "", quote = "", profession = "", id = null).toMap()
        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }
}