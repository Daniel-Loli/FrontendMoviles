package com.example.susti
import androidx.compose.ui.zIndex
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.text.input.KeyboardType

// Data classes

data class CaracteristicasPredio(
    val area_predio: Int,
    val num_casas: Int,
    val num_areas_comunes: Int,
    val area_comunes: Int,
    val num_administradores: Int,
    val num_vigilantes: Int,
    val num_personal_limpieza: Int,
    val num_jardineros: Int
)

data class Solicitud(
    val fecha_solicitud: String,
    val servicio: String,
    val dni: String,
    val predio: String,
    val caracteristicas: CaracteristicasPredio
)

data class Solicitante(
    val apellido_paterno: String,
    val apellido_materno: String,
    val nombres: String,
    val telefono: String,
    val correo: String,
    val predios: List<String>
)

data class Servicio(
    val id_servicio: Int,
    val descripcion: String,
    val precio: Double
)

// Retrofit API interface
interface ApiService {
    @GET("solicitante/{dni}")
    fun obtenerSolicitante(@Path("dni") dni: String): Call<Solicitante>

    @POST("registrar")
    fun registrarSolicitud(@Body solicitud: Solicitud): Call<Map<String, String>>

    @GET("servicios")
    fun obtenerServicios(): Call<List<Servicio>>
}

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8000"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SolicitudApp()
            }
        }
    }

    @Composable
    fun SolicitudApp() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        var fechaSolicitud by remember { mutableStateOf(currentDate) }
        var servicio by remember { mutableStateOf("") }
        var dni by remember { mutableStateOf("") }
        var predio by remember { mutableStateOf("") }

        val prediosList = remember { mutableStateListOf<String>() }
        val serviciosList = remember { mutableStateListOf<Servicio>() }
        var areaPredio by remember { mutableStateOf("") }
        var numCasas by remember { mutableStateOf("") }
        var numAreasComunes by remember { mutableStateOf("") }
        var areaComunes by remember { mutableStateOf("") }
        var numAdministradores by remember { mutableStateOf("") }
        var numVigilantes by remember { mutableStateOf("") }
        var numPersonalLimpieza by remember { mutableStateOf("") }
        var numJardineros by remember { mutableStateOf("") }

        var apellidoPaterno by remember { mutableStateOf("") }
        var apellidoMaterno by remember { mutableStateOf("") }
        var nombres by remember { mutableStateOf("") }
        var telefono by remember { mutableStateOf("") }
        var correo by remember { mutableStateOf("") }

        var loading by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            consultarServicios { servicios ->
                if (servicios != null) {
                    serviciosList.clear()
                    serviciosList.addAll(servicios)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Solicitud de Cotización",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Selección de servicio
            Text("Seleccionar Servicio", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Box {
                OutlinedTextField(
                    value = servicio,
                    onValueChange = {},
                    label = { Text("Servicio") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (serviciosList.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                    ) {
                        serviciosList.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { servicio = item.descripcion }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (servicio == item.descripcion),
                                    onClick = { servicio = item.descripcion }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "${item.descripcion} - \$${item.precio}")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Datos del solicitante
            Text("Datos del Solicitante", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = dni,
                onValueChange = { dni = it },
                label = { Text("N Documento") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    loading = true
                    consultarSolicitante(dni) { solicitante ->
                        if (solicitante != null) {
                            apellidoPaterno = solicitante.apellido_paterno
                            apellidoMaterno = solicitante.apellido_materno
                            nombres = solicitante.nombres
                            telefono = solicitante.telefono
                            correo = solicitante.correo
                            prediosList.clear()
                            prediosList.addAll(solicitante.predios)
                        }
                        loading = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar")
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (loading) {
                CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
            }

            // Mostrar datos del solicitante
            OutlinedTextField(
                value = apellidoPaterno,
                onValueChange = {},
                label = { Text("Apellido Paterno") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = apellidoMaterno,
                onValueChange = {},
                label = { Text("Apellido Materno") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = nombres,
                onValueChange = {},
                label = { Text("Nombres") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = telefono,
                onValueChange = {},
                label = { Text("Teléfono") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = correo,
                onValueChange = {},
                label = { Text("Correo") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Datos del predio
            Text("Datos del Predio", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Box {
                OutlinedTextField(
                    value = predio,
                    onValueChange = {},
                    label = { Text("Predio") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )

                if (prediosList.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .zIndex(1f) // Asegura que la lista esté sobre otros elementos
                    ) {
                        prediosList.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { predio = item }
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (predio == item),
                                    onClick = { predio = item }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = item)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Características del predio
            Text("Características del Predio", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = areaPredio,
                onValueChange = { areaPredio = it },
                label = { Text("Área del Predio (m²)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = numCasas,
                onValueChange = { numCasas = it },
                label = { Text("Número de Casas") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = numAreasComunes,
                onValueChange = { numAreasComunes = it },
                label = { Text("Número de Áreas Comunes") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = areaComunes,
                onValueChange = { areaComunes = it },
                label = { Text("Área de Áreas Comunes (m²)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = numAdministradores,
                onValueChange = { numAdministradores = it },
                label = { Text("Número de Administradores") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = numVigilantes,
                onValueChange = { numVigilantes = it },
                label = { Text("Número de Vigilantes") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = numPersonalLimpieza,
                onValueChange = { numPersonalLimpieza = it },
                label = { Text("Número de Personal de Limpieza") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = numJardineros,
                onValueChange = { numJardineros = it },
                label = { Text("Número de Jardineros") },
                modifier = Modifier.fillMaxWidth()
            )


            // Más campos para características...
            // Similar a los campos anteriores...

            Spacer(modifier = Modifier.height(16.dp))

            // Botones para registrar y cancelar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    val caracteristicas = CaracteristicasPredio(
                        area_predio = areaPredio.toIntOrNull() ?: 0,
                        num_casas = numCasas.toIntOrNull() ?: 0,
                        num_areas_comunes = numAreasComunes.toIntOrNull() ?: 0,
                        area_comunes = areaComunes.toIntOrNull() ?: 0,
                        num_administradores = numAdministradores.toIntOrNull() ?: 0,
                        num_vigilantes = numVigilantes.toIntOrNull() ?: 0,
                        num_personal_limpieza = numPersonalLimpieza.toIntOrNull() ?: 0,
                        num_jardineros = numJardineros.toIntOrNull() ?: 0
                    )

                    val solicitud = Solicitud(
                        fecha_solicitud = fechaSolicitud,
                        servicio = servicio,
                        dni = dni,
                        predio = predio,
                        caracteristicas = caracteristicas
                    )

                    registrarSolicitud(solicitud)
                }) {
                    Text("Registrar")
                }
                Button(onClick = { /* Lógica para cancelar */ }) {
                    Text("Cancelar")
                }
            }
        }
    }

    private fun registrarSolicitud(solicitud: Solicitud) {
        val call = RetrofitInstance.apiService.registrarSolicitud(solicitud)

        call.enqueue(object : Callback<Map<String, String>> {
            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("API Error", t.message.orEmpty())
            }

            override fun onResponse(call: Call<Map<String, String>>, response: Response<Map<String, String>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Solicitud registrada exitosamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun consultarSolicitante(dni: String, callback: (Solicitante?) -> Unit) {
        val call = RetrofitInstance.apiService.obtenerSolicitante(dni)

        call.enqueue(object : Callback<Solicitante> {
            override fun onFailure(call: Call<Solicitante>, t: Throwable) {
                callback(null)
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("API Error", t.message.orEmpty())
            }

            override fun onResponse(call: Call<Solicitante>, response: Response<Solicitante>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun consultarServicios(callback: (List<Servicio>?) -> Unit) {
        val call = RetrofitInstance.apiService.obtenerServicios()

        call.enqueue(object : Callback<List<Servicio>> {
            override fun onFailure(call: Call<List<Servicio>>, t: Throwable) {
                callback(null)
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                Log.e("API Error", t.message.orEmpty())
            }

            override fun onResponse(call: Call<List<Servicio>>, response: Response<List<Servicio>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    callback(null)
                    Toast.makeText(this@MainActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}
