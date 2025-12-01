# 📱 Level-Up Gamer — Aplicación Móvil (Android / Jetpack Compose)

Level-Up Gamer es una aplicación móvil de e-commerce orientada al mundo gamer.  
Construida con **Kotlin + Jetpack Compose**, estilo **cyberpunk**, e integrada a una arquitectura completa de **microservicios en Spring Boot** desplegados en AWS (Auth, Products, Cart, Coupons, Payments, Bills, etc.).

---

## 🚀 Características Principales

### 🔐 Autenticación
- Registro y Login de usuarios con JWT.
- Validaciones de campos y manejo completo del estado de sesión.
- Persistencia del token para navegación segura.

### 🛒 Catálogo de Productos
- Listado dinámico desde el microservicio **Products Service**.
- Categorías: *Consolas, Juegos, Accesorios y Ropa*.
- Vista de detalle con imágenes desde **Amazon S3**.
- Estilo cyberpunk con efectos visuales animados.

### 🛍 Carrito de Compras
- Carrito sincronizado según usuario logueado.
- Aumentar/disminuir cantidad.
- Validación de stock en tiempo real.
- Bloqueo del botón “Agregar al Carrito” cuando no hay stock.

### 🎟 Cupones
- Aplicación de cupones con reglas del **Coupons Service**.
- Validaciones: expirado, no válido, inactivo.
- Cálculo automático del descuento y del total del carrito.

### 💳 Pagos
- Envío del carrito al microservicio **Payments Service**.
- Flujo completo de pago: validación → checkout → respuesta.
- Pantalla de Resultado de Compra con resumen final.

### 🎨 Interfaz y Estilo Cyberpunk
- Paleta neon: verde, cyan y púrpura.
- Efectos glow, sombras y transiciones futuristas.
- Animaciones personalizadas con Compose.

---

## 🧱 Arquitectura de la App

### 📦 Capas principales
- **UI (Jetpack Compose)**: pantallas, navegación y componentes.
- **ViewModels con StateFlow**: lógica + estados inmutables.
- **Repositorios**: comunicación con API.
- **Network Layer**: Retrofit + OkHttp.
- **DI con Hilt**: inyección de dependencias centralizada.

### 🗂 Paquetes
```
/ui
/viewmodel
/data/dto
/data/repository
/data/network
/navigation
```

---

## 🔌 Microservicios Consumidos

| Servicio | Puerto | Función |
|---------|--------|---------|
| Auth Service | 8081 | Login y registro con JWT |
| Product Service | 8085 | Productos y stock |
| Cart Service | 8082 | Gestión del carrito |
| Coupons Service | 8084 | Validación de cupones |
| Payments Service | 8083 | Flujo de pago completo |

---

## ⚙️ Tecnologías Usadas

### 🧩 Android
- **Kotlin**
- **Jetpack Compose**
- **StateFlow + ViewModel**
- **Navigation Compose**
- **Hilt (DI)**
- **Retrofit + OkHttp**
- **Coil**
- **Coroutines**

### ☁️ Backend (AWS)
- **EC2
- **S3 (Imágenes)**

---

## ▶️ Cómo Ejecutar la App

### 📌 **Requisitos**
- **Android Studio Flamingo o superior**
- **API Level mínimo requerido: 32 (Android 12)**  
  *(La app requiere API 32 para ejecutar correctamente con las dependencias actuales de Jetpack Compose.)*

### 📜 Pasos

1. Clonar el repositorio:
```bash
git clone https://github.com/tu-repo/project-aplicacion-movil.git
```

2. Abrir en Android Studio.

3. Crear archivo `local.properties` si no existe:
```
sdk.dir=C:\Users\TU_USUARIO\AppData\Local\Android\Sdk
```

4. Configurar variables de entorno en `BuildConfig` o `.env`:
```kotlin
const val AUTH_BASE_URL = "http://56.228.34.53:8081/api/auth"
const val PRODUCTS_BASE_URL = "http://56.228.34.53:8085/api/v1/products"
```

5. Ejecutar la app en un emulador o dispositivo en **API 32+**.

---

## 🧪 Testing
- Compose Test Rule para pantallas.
- Pruebas unitarias de ViewModels.
- Validación de estados.

---

## 📌 Pendientes / En Desarrollo
- Historial de pedidos.
- Perfil del usuario.
- Carrito offline.
- Notificaciones push.

---

## 👨‍💻 Autor
**CHAPSUI / Level-Up Gamer**  
Estudiante de Programación — Chile 🇨🇱  

---

## 📄 Licencia
Proyecto bajo licencia **MIT**.
