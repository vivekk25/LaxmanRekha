package com.vivek.laxmanrekha

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.login.widget.LoginButton
import java.util.*
import com.facebook.CallbackManager
import com.facebook.FacebookException
import com.facebook.AccessToken
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import org.json.JSONException
import com.facebook.GraphRequest

class LoginActivity : AppCompatActivity() {
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        // reference to all views
        var loginBtn = findViewById(R.id.btn_login) as? Button
        var fbLinkBtn = findViewById(R.id.facebook_signup_link) as LoginButton
        var registrationBtn = findViewById(R.id.registration_link) as? TextView
        // Creating CallbackManager
        callbackManager = CallbackManager.Factory.create();
        // Registering CallbackManager with the LoginButton
        fbLinkBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val accessToken = loginResult.accessToken
                useLoginInformation(accessToken)
                startActivity()
            }

            override fun onCancel() {}
            override fun onError(error: FacebookException) {}
        })

        fbLinkBtn.setReadPermissions(Arrays.asList("email", "public_profile"));


        // login button on-click listener
        loginBtn?.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }

        // facebook button on-click listener
        fbLinkBtn?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // facebook button on-click listener
        registrationBtn?.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

    private fun startActivity(){
        val intent = Intent(this, MapActivity::class.java)
        // start your next activity
        startActivity(intent)
    }

    private fun useLoginInformation(accessToken:AccessToken) {
    /**
     * Creating the GraphRequest to fetch user details
     * 1st Param - AccessToken
     * 2nd Param - Callback (which will be invoked once the request is successful)
     */
          val request = GraphRequest.newMeRequest(accessToken
    ) { `object`, response ->
        //OnCompleted is invoked once the GraphRequest is successful
        try {
            val name = `object`.getString("name")
            val email = `object`.getString("email")
            val image = `object`.getJSONObject("picture").getJSONObject("data").getString("url")
        } catch (e:JSONException) {
            e.printStackTrace()
        }
    }
            // We set parameters to the GraphRequest using a Bundle.
          val parameters = Bundle()
    parameters.putString("fields", "id,name,email,picture.width(200)")
            request.parameters = parameters
     // Initiate the GraphRequest
          request.executeAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }

    public override fun onStart() {
        super.onStart()
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null) {
            useLoginInformation(accessToken)
        }
    }
}