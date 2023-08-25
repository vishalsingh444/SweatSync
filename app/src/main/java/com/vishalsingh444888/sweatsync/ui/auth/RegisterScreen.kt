package com.vishalsingh444888.sweatsync.ui.auth

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vishalsingh444888.sweatsync.R
import kotlinx.coroutines.launch
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.vishalsingh444888.sweatsync.ui.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController,viewModel: AuthViewModel = hiltViewModel(),appViewModel: AppViewModel = hiltViewModel()) {

    var emailState by remember { mutableStateOf("") }
    var passwordState by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    var confirmPasswordState by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val state = viewModel.signUpState.collectAsState(initial = null)
    val googleSignInState = viewModel.googleSignInState.collectAsState()
    var displayName:String? by remember {
        mutableStateOf("User0001")
    }
    var profileUrl: String? by remember {
        mutableStateOf(null)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()){
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try{
                val result = account.getResult(ApiException::class.java)
                displayName = result?.displayName
                profileUrl = result?.photoUrl?.toString()
                val credential = GoogleAuthProvider.getCredential(result.idToken,null)
                viewModel.googleSignIn(credential = credential)

            }catch (it: ApiException){
                print(it)
            }

        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(id = R.string.register),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge
            )
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = stringResource(id = R.string.register_greeting),
//                fontSize = 28.sp,
//                modifier = Modifier.fillMaxWidth(),
//                style = MaterialTheme.typography.bodyLarge,
//                lineHeight = 30.sp
//            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.register_message),
                fontSize = 28.sp,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            OutlinedTextField(
                value = emailState,
                onValueChange = { emailState = it },
                label = { Text(text = "Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            OutlinedTextField(
                value = passwordState,
                onValueChange = { passwordState = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                label = { Text(text = "Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            painter = if (passwordVisibility) painterResource(id = R.drawable.visibility_48px) else painterResource(
                                id = R.drawable.visibility_off_48px
                            ),
                            contentDescription = if (passwordVisibility) "hide password" else "show password"
                        )
                    }
                }
            )
            OutlinedTextField(
                value = confirmPasswordState,
                onValueChange = { confirmPasswordState = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                label = { Text(text = "Confirm password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            painter = if (passwordVisibility) painterResource(id = R.drawable.visibility_48px) else painterResource(
                                id = R.drawable.visibility_off_48px
                            ),
                            contentDescription = if (passwordVisibility) "hide password" else "show password"
                        )
                    }
                }
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "Or")
            }
            GoogleCardComp(stringId = R.string.google_register, onClick = {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestProfile()
                    .requestIdToken(context.getString(R.string.web_client_id))
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context,gso)

                launcher.launch(googleSignInClient.signInIntent)
            })

        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Already have an account?")

                Text(text = "Login",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { navController.navigate("Login") }
                        .padding(start = 8.dp, end = 8.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    scope.launch {
                        if(passwordState==confirmPasswordState){
                            viewModel.registerUser(emailState,passwordState)
                        }else{
                            showToast(context, message = "Incorrect confirm Password.")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                enabled = (emailState.isNotEmpty() && passwordState.isNotEmpty() && confirmPasswordState.isNotEmpty()),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = Color.Black
                )

            ) {
                if(state.value== null || state.value?.isLoading==false){
                    Text(text = "Sign Up", fontSize = 16.sp)
                }
                else{
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
    LaunchedEffect(key1 = state.value?.isSuccess){
        scope.launch {
            if(state.value?.isSuccess?.isNotBlank()==true){
                navController.navigate("Home")
                val success = state.value?.isSuccess
                Toast.makeText(context,"$success",Toast.LENGTH_LONG).show()
            }
        }
    }
    LaunchedEffect(key1 = state.value?.isError){
        scope.launch {
            if(state.value?.isError?.isNotBlank()==true){
                val error = state.value?.isError
                Toast.makeText(context,"$error",Toast.LENGTH_LONG).show()
            }
        }
    }
    LaunchedEffect(key1 = googleSignInState.value.isSuccess){
        scope.launch {
            if(googleSignInState.value.isSuccess!=null){
                viewModel.updateUserDetailsToFireStore(displayName,profileUrl)
                appViewModel.updateUserDetails()
                navController.navigate("Home")
            }
        }
    }
}



fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun GoogleCardComp(@StringRes stringId: Int,onClick:()->Unit) {
    Card(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
    ) {
        Row(modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "register with google",
                modifier = Modifier.height(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(text = stringResource(id = stringId), color = Color.Black)
        }
    }
}

