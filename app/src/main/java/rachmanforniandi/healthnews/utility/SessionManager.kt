package rachmanforniandi.healthnews.utility

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context:Context) {
    var pref: SharedPreferences?= null
    var editor : SharedPreferences.Editor?= null
    val PREF_NAME ="LOGINREGISTER"

    companion object{
        var ISLOGIN ="isLogin"
    }

    var USERNAME="userName"
    var EMAIL = "email"

    init {
        pref = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
        editor = pref?.edit()
    }

    var login : Boolean?
        get() = pref?.getBoolean(ISLOGIN,false)
        set(login) {
            editor?.putBoolean(ISLOGIN,true)
            editor?.commit()
        }
    var username: String?
        get() = pref?.getString(USERNAME,"")
        set(username) {
            editor?.putString(USERNAME,username)
            editor?.commit()
        }

    var email: String?
        get() = pref?.getString(EMAIL,"")
        set(email) {
            editor?.putString(EMAIL,email)
            editor?.commit()
        }

    fun saveUserName(key:String,username:String){
        editor?.putString(key,username)
        editor?.commit()
    }


    fun saveUserEmail(key:String,email:String){
        editor?.putString(key,email)
        editor?.commit()
    }


    fun logOut(){
        editor?.clear()
        editor?.commit()
    }

}