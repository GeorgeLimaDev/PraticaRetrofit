package com.example.retrofit_cep

import android.net.http.HttpException
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.retrofit_cep.data.Endereco
import com.example.retrofit_cep.service.RetrofitClient
import com.example.retrofit_cep.ui.theme.RetrofitcepTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitcepTheme {
                    ColetarDados()
                }
            }
        }
    }

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ColetarDados() {
    val context = LocalContext.current
    var scope = rememberCoroutineScope()

    var cep by remember {mutableStateOf("")}
    var erro by remember { mutableStateOf<String?>(null) }
    var endereco by remember { mutableStateOf<Endereco?>(null) }

    Column (horizontalAlignment = Alignment.CenterHorizontally){
        OutlinedTextField(value = cep, onValueChange = {cep = it}, label = {Text(text = "CEP")})
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            scope.launch (Dispatchers.IO){
                try {
                    endereco = RetrofitClient.enderecoService.getDetailsByCep(cep)
                    erro = null
                } catch (e: HttpException) {
                    erro = "Erro ao buscar dados. Verifique o CEP e tente novamente."
                    endereco = null
                } catch (e: Exception) {
                    erro = "Erro desconhecido."
                    endereco = null
                }
            }
        }) {
            Text("Pesquisar")
        }

        if (erro != null) {
            Text(text = erro!!, color = MaterialTheme.colorScheme.error)
        } else if (endereco != null) {
            Text("CEP: ${endereco!!.cep}")
            Text("Rua: ${endereco!!.logradouro}")
            Text("Complemento: ${endereco!!.complemento}")
            Text("Unidade: ${endereco!!.unidade}")
            Text("Bairro: ${endereco!!.bairro}")
            Text("Localidade: ${endereco!!.localidade}")
            Text("UF: ${endereco!!.uf}")
            Text("Estado: ${endereco!!.estado}")
            Text("Região: ${endereco!!.regiao}")
            Text("DDD: ${endereco!!.ddd}")
        }
    }
}