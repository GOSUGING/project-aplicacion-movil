package com.example.levelup.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.levelup.viewmodel.AdminUsersViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AdminUsersScreen(
    navController: NavController,
    vm: AdminUsersViewModel = hiltViewModel()
) {
    val state by vm.ui.collectAsState()

    val neon = Color(0xFF39FF14)
    val neonRed = Color(0xFFFF003C)
    val bg = Color(0xFF050505)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp)
    ) {

        // 🔥 TITULO
        Text(
            "GESTIÓN DE USUARIOS",
            color = neon,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        // 🔥 BOTÓN VOLVER AL PANEL ADMIN
        Button(
            onClick = {
                navController.navigate("admin_profile") {
                    popUpTo("admin_users") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = neon),
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
            Spacer(Modifier.width(8.dp))
            Text("Volver al Panel Admin", color = Color.Black)
        }

        Spacer(Modifier.height(20.dp))

        // LOADING
        if (state.loading) {
            CircularProgressIndicator(color = neon)
            return
        }

        // ERROR
        if (state.error != null) {
            Text(state.error!!, color = Color.Red)
        }

        // LISTA DE USUARIOS
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(state.users) { user ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, neon),
                    colors = CardDefaults.cardColors(bg)
                ) {

                    Column(Modifier.padding(16.dp)) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = neon)
                            Spacer(Modifier.width(8.dp))
                            Text(user.name, color = Color.White)
                        }

                        Text("Email: ${user.email}", color = Color.Gray)
                        Text("Rol: ${user.role}", color = neon)

                        Spacer(Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            // Cambiar rol
                            OutlinedButton(
                                onClick = { vm.toggleRole(user.id) },
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    width = 2.dp,
                                    brush = SolidColor(neon)
                                )
                            ) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = neon)
                                Spacer(Modifier.width(6.dp))
                                Text("Cambiar Rol", color = neon)
                            }

                            // Eliminar usuario
                            OutlinedButton(
                                onClick = { vm.deleteUser(user.id) },
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    width = 2.dp,
                                    brush = SolidColor(neonRed)
                                )
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, tint = neonRed)
                                Spacer(Modifier.width(6.dp))
                                Text("Eliminar", color = neonRed)
                            }
                        }
                    }
                }
            }
        }
    }
}
