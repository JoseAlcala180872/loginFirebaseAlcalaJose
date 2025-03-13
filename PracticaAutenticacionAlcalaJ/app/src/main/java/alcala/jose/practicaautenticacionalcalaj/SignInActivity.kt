package alcala.jose.practicaautenticacionalcalaj

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        val email: EditText = findViewById(R.id.etrEmail)
        val password: EditText = findViewById(R.id.etrPassword)
        val confirmPassword: EditText = findViewById(R.id.etrConfirmPassword)
        val errorTv: TextView = findViewById(R.id.tvrError)
        val button: Button = findViewById(R.id.btnRegister)

        errorTv.visibility = View.INVISIBLE

        button.setOnClickListener {
            if (email.text.isEmpty() || password.text.isEmpty() || confirmPassword.text.isEmpty()) {
                errorTv.text = "Todos los campos deben ser llenados."
                errorTv.visibility = View.VISIBLE
            } else if (password.text.toString() != confirmPassword.text.toString()) {
                errorTv.text = "Las contraseñas no coinciden."
                errorTv.visibility = View.VISIBLE
            } else {
                errorTv.visibility = View.INVISIBLE
                signIn(email.text.toString(), password.text.toString())
            }
        }
    }

    private fun signIn(email: String, password: String) {
        Log.d("INFO", "email: $email, password: $password")
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("INFO", "signInWithEmail:success")
                val user = auth.currentUser
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish() // Finaliza la actividad actual
            } else {
                Log.w("ERROR", "signInWithEmail:failure", task.exception)
                Toast.makeText(
                    baseContext,
                    "El registro falló: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}