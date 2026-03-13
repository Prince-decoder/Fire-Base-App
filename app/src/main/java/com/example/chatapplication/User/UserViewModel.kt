package com.example.chatapplication.User

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapplication.Injection
import com.example.chatapplication.Results
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch



enum class AuthState {
    LOADING,
    LOGGED_IN,
    LOGGED_OUT
}
class UserViewModel: ViewModel() {

    private val _auth = FirebaseAuth.getInstance()
    private val _authstate = MutableStateFlow(AuthState.LOADING)
    val authstate = _authstate.asStateFlow()
    val isLoggedIn : Boolean get() =_authstate.value == AuthState.LOGGED_IN


    private val _userrepo: UserRepository
    init {
        _auth.addAuthStateListener{
                firebaseAuth ->
            if(firebaseAuth.currentUser != null)
            {
                _authstate.value= AuthState.LOGGED_IN
            }
            else{
                _authstate.value = AuthState.LOGGED_OUT
            }
        }

        _userrepo= UserRepository(
            _auth,
            Injection.instance()
        )
    }

    private val _authResult = MutableLiveData<Results<Boolean>>()
    val authRESULT: LiveData<Results<Boolean>> = _authResult

    private val _user= MutableLiveData<UserDetails?>()
    val user: LiveData<UserDetails?> get() = _user


    fun signUP(fName:String,lName: String,email:String,pass: String)
    {
        viewModelScope.launch {
            _authResult.value= Results.Loading
            _authResult.value= _userrepo.Signup(fName,lName,email,pass)
        }
    }

    fun signIN(email: String,pa: String)
    {
        viewModelScope.launch {
            _authResult.value= Results.Loading
            _authResult.value= _userrepo.signIn(email,pa)
        }
    }

    fun logOUT()
    {
        viewModelScope.launch {
            _auth.signOut()
            _userrepo.Logout()

            _user.value =null
            _authResult.value = Results.Success(false)

        }
    }
    fun getuser()
    {
        viewModelScope.launch {
            _authResult.value= Results.Loading

            when(val result = _userrepo.getCurrentUser())
            {
                is Results.Success ->{
                    _user.value= result.data
                }
                is Results.error ->
                {
                    _authResult.value = Results.error(result.e)
                }
                is Results.Loading->
                {
                }
            }
        }
    }
}