package com.kunalfarmah.apps.readerapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kunalfarmah.apps.readerapp.App
import com.kunalfarmah.apps.readerapp.model.BookResponse
import com.kunalfarmah.apps.readerapp.model.MBook
import com.kunalfarmah.apps.readerapp.nav.ScreenNames


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(modifier: Modifier = Modifier,
             loading: Boolean = false,
             isCreateAccount: Boolean = false,
             onDone: (String, String, Boolean, (String)->Unit) -> Unit  = {email, pwd, isLogin, setError -> {}}
             ) {
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }

    val login = remember {
        mutableStateOf(!isCreateAccount)
    }

    val error = remember {
        mutableStateOf("")
    }
    val btnText = if (login.value) "Login" else "Register"
    val optionText = if (login.value) "New Here? Register" else "Already A User? Login"

    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current

    val valid = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .fillMaxWidth()
        .height(500.dp)
        .verticalScroll(rememberScrollState())

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        if(!login.value)
            Text(text = "Please enter a valid email and password that is at least 6 characters", modifier = Modifier.padding(10.dp))

        Spacer(modifier = Modifier.height(10.dp))
        EmailInput(
            emailState = email,
            enabled = true,
            onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            }
        )
        PasswordInput(
            passwordState = password,
            enabled = true,
            passwordVisibility = passwordVisibility,
            modifier = Modifier.focusRequester(passwordFocusRequest),
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                onDone(email.value.trim(), password.value.trim(), login.value){
                    error.value = it
                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        SubmitButton(
            textId = btnText,
            loading = loading,
            validInputs = valid
        ){
            onDone(email.value.trim(), password.value.trim(), login.value){
                error.value = it
            }
        }
        if(error.value.isNotEmpty()){
            ErrorText(text = error.value)
        }
        ClickableText(text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color.Black.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold
                )
            ) {
                append(optionText.split("?")[0])
            }
            append("? ")
            withStyle(
                style = SpanStyle(
                    color = Color.Blue,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(optionText.split("?")[1].trim())
            }
        }, modifier = Modifier.padding(10.dp), style = TextStyle(textAlign = TextAlign.Center)) {
            login.value = !login.value
        }
    }
}


@Composable
fun EmailInput(
    modifier: Modifier = Modifier,
    emailState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    InputField(
        valueState = emailState,
        modifier = modifier,
        labelId = labelId,
        enabled = enabled,
        imeAction = imeAction,
        keyboardType = KeyboardType.Email,
        onAction = onAction,
        trailingIcon = null
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    passwordState: MutableState<String>,
    labelId: String = "Password",
    enabled: Boolean = true,
    passwordVisibility: MutableState<Boolean>,
    imeAction: ImeAction = ImeAction.Done,
    onAction: KeyboardActions = KeyboardActions.Default
) {
    val visualTransformation = if(passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()
    InputField(
        valueState = passwordState,
        modifier = modifier,
        labelId = labelId,
        enabled = enabled,
        isSingleLine = true,
        imeAction = imeAction,
        keyboardType = KeyboardType.Password,
        onAction = onAction,
        visualTransformation = visualTransformation,
        trailingIcon = {PasswordVisibility(passwordVisibility = passwordVisibility)}
    )
}

@Composable
fun PasswordVisibility(passwordVisibility: MutableState<Boolean>){
    val visible = passwordVisibility.value
    IconButton(
        onClick = {
            passwordVisibility.value = !visible
        }
    ){
        Icons.Default.Close
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String = "Email",
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon:  @Composable() (() -> Unit)?
) {

    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        enabled = enabled,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction ?: KeyboardActions.Default,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon
    )
}

@Composable
fun SubmitButton(textId:String, loading:Boolean, validInputs: Boolean, onClick: () -> Unit){
    Button(onClick = onClick, modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth(),
        enabled = !loading && validInputs,
        shape = CircleShape
    ) {
        if(loading) CircularProgressIndicator(modifier = Modifier.size(25.dp))
        else Text(textId, modifier = Modifier.padding(5.dp))
    }
}

@Composable
fun ErrorText(text: String){
    Text(text = text, modifier = Modifier.padding(5.dp), style = TextStyle(color = Color.Red))
}

@Composable
fun TitleSection(modifier: Modifier = Modifier, label: String) {
    Surface(modifier = modifier.padding(start = 5.dp, top= 1.dp)){
        Text(
            text = label,
            style = TextStyle(fontSize = 19.sp, fontStyle = FontStyle.Normal, textAlign = TextAlign.Left)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String = "A Reader",
    backIcon: ImageVector?= null,
    showProfile: Boolean = true,
    navController: NavController = NavController(
        App.context
    ),
    handleBackPress: ()->Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title, color = Color.Red.copy(0.7f),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
                modifier = Modifier.padding(start = 10.dp)
            )
        },
        navigationIcon = {
            if (showProfile) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(25.dp)
                )
            }
            else if(backIcon != null){
                Icon(imageVector = backIcon, contentDescription = "arroback",
                    modifier = Modifier.clickable { handleBackPress.invoke() })
            }
        },
        actions = {
            if (showProfile) {
                IconButton(onClick = {
                    Firebase.auth.signOut()
                    navController.navigate(ScreenNames.LoginScreen.name)
                }) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }

    )
}


@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = { onTap() }, shape = RoundedCornerShape(50.dp),
        containerColor = Color(0xFF92CBDF)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add a book",
            tint = Color.White
        )

    }
}

@Preview
@Composable
fun ListCard(book: MBook = MBook(""), onPressDetails: (String) -> Unit = {}) {
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp
    Card(
        shape = RoundedCornerShape(39.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 10.dp,
            disabledElevation = 0.dp
        ),
        modifier = Modifier
            .padding(16.dp)
            .height(265.dp)
            .width(202.dp)
            .clickable {
                onPressDetails.invoke("")
            },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.width(screenWidth.dp - spacing * 2).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = rememberImagePainter(data = book.photoUrl),
                    contentDescription = "book image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.width(50.dp))
                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "fav",
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                    BookRating(book.rating ?: 0.0)
                }
            }
            Text(
                text = book.title ?: "Book Title",
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(text = if(book.authors != null) book.authors.toString() else "Authors: All...", modifier = Modifier.padding(4.dp), fontSize = 16.sp)
            Row(modifier = Modifier.align(Alignment.End), verticalAlignment = Alignment.Bottom) {
                RoundedButton(label = if(book.startedReading!=null) "Reading" else "Not Started", radius = 70)
            }
        }
    }
}

@Composable
fun BookRating(score: Double = 4.5) {
    Surface(
        modifier = Modifier
            .height(70.dp)
            .padding(4.dp), shape = RoundedCornerShape(56.dp),
        shadowElevation = 6.dp, color = Color.White
    ) {
        Column(modifier = Modifier.padding(4.dp), verticalArrangement = Arrangement.Center) {
            Icon(imageVector = Icons.Filled.Star, contentDescription = "Star")
            Text(text = score.toString(), style = MaterialTheme.typography.bodyMedium)
        }

    }
}

@Preview
@Composable
fun RoundedButton(label: String = "Reading", radius: Int = 29, onPress: () -> Unit = {}) {
    Surface(
        modifier = Modifier.clip(
            RoundedCornerShape(
                bottomEndPercent = radius,
                topStartPercent = radius
            )
        ), color = Color(0xFF92CBDF)
    ) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .heightIn(40.dp)
                .clickable { onPress() },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }
    }
}

@Composable
fun CircularLoader(){
    Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
    }
}

@Composable
fun BookRow(book: BookResponse.Item, navController: NavController){
    Card(modifier = Modifier
        .clickable {
            navController.navigate(ScreenNames.BookDetailsScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(145.dp)
        .padding(1.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp, pressedElevation = 10.dp)) {
        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){
            Image(painter = rememberImagePainter(data=book.volumeInfo?.imageLinks?.thumbnail),
                contentDescription = "book image",
                Modifier
                    .width(80.dp)
                    .fillMaxHeight(1f)
                    .padding(end = 10.dp))
            Column() {
                Text(text = book.volumeInfo?.title.toString(), overflow = TextOverflow.Ellipsis)
                Text(text = "Authors: ${book.volumeInfo?.authors}", overflow = TextOverflow.Clip, fontStyle = FontStyle.Italic)
                Text(text = "Published on: ${book.volumeInfo?.publishedDate}", overflow = TextOverflow.Clip, fontStyle = FontStyle.Italic)
                if(book.volumeInfo?.publisher!=null)
                    Text(text = "Publisher: ${book.volumeInfo?.publisher}", overflow = TextOverflow.Clip, fontStyle = FontStyle.Italic)
                if(book.volumeInfo?.categories!=null)
                    Text(text = "${book.volumeInfo?.categories}", overflow = TextOverflow.Clip, fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Composable
fun BookRow(book: MBook, navController: NavController?){
    Card(modifier = Modifier
        .clickable {
            navController?.navigate(ScreenNames.BookDetailsScreen.name + "/${book.id}")
        }
        .fillMaxWidth()
        .height(145.dp)
        .padding(1.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp, pressedElevation = 10.dp)) {
        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically){
            Image(painter = rememberImagePainter(data=book.photoUrl),
                contentDescription = "book image",
                Modifier
                    .width(80.dp)
                    .fillMaxHeight(1f)
                    .padding(end = 10.dp))
            Column() {
                Text(text = book.title.toString(), overflow = TextOverflow.Ellipsis)
                Text(text = "Authors: ${book.authors}", overflow = TextOverflow.Clip, fontStyle = FontStyle.Italic)
                Text(text = "Published on: ${book.publishedDate}", overflow = TextOverflow.Clip, fontStyle = FontStyle.Italic)
                if(book.publisher!=null)
                    Text(text = "Publisher: ${book.publisher}", overflow = TextOverflow.Clip, fontStyle = FontStyle.Italic)
                if(book.categories!=null)
                    Text(text = "${book.categories}", overflow = TextOverflow.Clip, fontStyle = FontStyle.Italic)
            }
        }
    }
}