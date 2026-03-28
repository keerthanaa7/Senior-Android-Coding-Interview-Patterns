package com.example.searchwithdebounceandcancellation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.NavType.Companion.StringType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.searchwithdebounceandcancellation.ui.theme.SearchWithDebounceAndCancellationTheme

class MainActivity : ComponentActivity() {
    private val stockRepository = StockRepository()
    private val stockRepositoryRepo = StockRepositorySharedUIState()
    private val toastRepository = ToastRepository()
    private val menuRepository = MenuRepository()

    private val recursiveSearchViewModel: RecursiveSearchViewModel by viewModels {
        viewModelFactory {
            initializer {
                RecursiveSearchViewModel(menuRepository)
            }
        }
    }

    private val menuViewModel: MenuViewModel by viewModels {
        viewModelFactory {
            initializer {
                MenuViewModel(toastRepository)
            }
        }
    }

    private val menuViewModelOrderCart: MenuViewModelOrderCartState by viewModels {
        viewModelFactory {
            initializer {
                MenuViewModelOrderCartState(toastRepository)
            }
        }
    }
    private val stockViewModel: StockViewModel by viewModels{
        viewModelFactory {
            initializer {
                StockViewModel(stockRepository)
            }
        }
    }

    private val stockViewModelMultiStateFilter: StockViewModelMultiStateFilter by viewModels {
        viewModelFactory {
            initializer {
                StockViewModelMultiStateFilter(stockRepository)
            }
        }
    }

    private val stockViewModelLivePrice: StockViewModelLivePrice by viewModels {
        viewModelFactory {
            initializer {
                StockViewModelLivePrice(stockRepository)
            }
        }
    }

    private val imageUploadViewModel: ImageUploadViewModel by viewModels {
        viewModelFactory {
            initializer {
                ImageUploadViewModel()
            }
        }
    }

    private val stockViewModelPagination: StockViewModelPagination by viewModels {
        viewModelFactory {
            initializer {
                StockViewModelPagination(stockRepository)
            }
        }
    }

    private val stockViewModelSearch: StockViewModelDebounce by viewModels {
        viewModelFactory {
            initializer {
                StockViewModelDebounce(stockRepository)
            }
        }
    }

    private val sharedStockViewModel: SharedStockViewModel by viewModels {
        viewModelFactory {
            initializer {
                SharedStockViewModel(stockRepository)
            }
        }
    }
    private val sharedStockViewModelStockViewModelRepo: RepoSharedUIStockViewModel by viewModels {
        viewModelFactory {
            initializer {
                RepoSharedUIStockViewModel(stockRepositoryRepo)
            }
        }
    }
    private val stockViewMOdelOptimisticUI: StockViewModelOptimisticUI by viewModels {
        viewModelFactory {
            initializer {
                StockViewModelOptimisticUI(stockRepository)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SearchWithDebounceAndCancellationTheme {
               // StockScreen(stockViewModel)
               // MultiFilter(stockViewModelMultiStateFilter)
               // LiveScreen(stockViewModelLivePrice)
              //  PaginationScreen(stockViewModelPagination)
               // ImageUploadScreen(imageUploadViewModel)
             //   StockScreenSearch(stockViewModelSearch)
               // SharedViewModel(sharedStockViewModel)
              //  SharedRepoPattern(sharedStockViewModelRepo)
               // OptimisticUI(stockViewMOdelOptimisticUI)
               // MenuHierarchyActivityScreen(menuViewModel)
               // MenuLowThresholdScreen(menuViewModel)
               // MenuScreenOrderCart(menuViewModelOrderCart)
                RecursiveSearchScreen(recursiveSearchViewModel)
            }
        }
    }
}

//Recursive Menu Search: Implement a search function that finds a specific item deep within a
// nested "Menu Groups" data structure.
@Composable
fun RecursiveSearchScreen(viewModel: RecursiveSearchViewModel) {
    val query by viewModel.searchQuery.collectAsState()
    val result by viewModel.searchResult.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Recursive Menu Search", style = MaterialTheme.typography.headlineSmall)

        // Search Input
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            label = { Text("Search Item Name (e.g. 'Vanilla')") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        )

        Divider()

        // Result Display
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (result != null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("ITEM FOUND!", style = MaterialTheme.typography.labelLarge, color = Color.Green)
                    Text("Name: ${result!!.label}", style = MaterialTheme.typography.headlineMedium)
                    Text("Price: $${result!!.cost ?: "Inherited"}")
                    Text("Stock: ${result!!.stock}")
                }
            } else if (query.isNotEmpty()) {
                Text("No item matches '$query'", color = Color.Gray)
            } else {
                Text("Type to start searching through nested groups...")
            }
        }
    }
}

//Order Cart State: A list of items with "+" and "-" buttons that updates a persistent "Total
// Amount" at the bottom of the screen. add tip and calculate total
@Composable
fun MenuScreenOrderCart(viewModel: MenuViewModelOrderCartState) {
    val state by viewModel.uiState.collectAsState()
    val cart by viewModel.cart.collectAsState()
    val subtotal by viewModel.subtotal.collectAsState()
    val tipRate by viewModel.tipRate.collectAsState()
    val grandTotal by viewModel.grandTotal.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.weight(1f)) {
            when (val s = state) {
                is MenuStateOrderCart.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is MenuStateOrderCart.Error -> Text(s.message)
                is MenuStateOrderCart.Success -> {
                    LazyColumn {
                        s.data.categories.forEach { category ->
                            item {
                                Text(
                                    text = category.name,
                                    modifier = Modifier.fillMaxWidth().background(Color.LightGray).padding(8.dp)
                                )
                            }
                            items(category.items) { item ->
                                val price = viewModel.getEffectivePrice(category, item)
                                val qty = cart[item.id] ?: 0
                                val bgColor = if (item.inventory < 5) Color.Red.copy(alpha = 0.1f) else Color.Transparent

                                Row(
                                    modifier = Modifier.fillMaxWidth().background(bgColor).padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(Modifier.weight(1f)) {
                                        Text(item.name)
                                        Text("$${String.format("%.2f", price)}")
                                    }
                                    Button(onClick = { viewModel.updateQuantity(item.id, -1) }) { Text("-") }
                                    Text("$qty", modifier = Modifier.padding(8.dp))
                                    Button(onClick = { viewModel.updateQuantity(item.id, 1) }) { Text("+") }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- TIP AND TOTAL SECTION ---
        if (state is MenuStateOrderCart.Success) {
            Column(Modifier.fillMaxWidth().padding(16.dp)) {
                Divider()
                Text("Select Tip:", style = MaterialTheme.typography.labelLarge)

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    listOf(0.15, 0.20, 0.25).forEach { rate ->
                        Button(
                            onClick = { viewModel.updateTip(rate) },
                            // Simple visual check for selected button
                            border = if (tipRate == rate) BorderStroke(2.dp, Color.Black) else null
                        ) {
                            Text("${(rate * 100).toInt()}%")
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
                Text("Subtotal: $${String.format("%.2f", subtotal)}")
                Text("Tip: $${String.format("%.2f", subtotal * tipRate)}")
                Text(
                    text = "GRAND TOTAL: $${String.format("%.2f", grandTotal)}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF2E7D32) // Simple green for the final total
                )
            }
        }
    }
}
// Inventory Threshold Alert: Display a list of items and highlight rows in red where the quantity
// is below a specific "Low Stock" threshold.
@Composable
fun MenuLowThresholdScreen(viewModel: MenuViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        when (val s = state) {
            is MenuState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is MenuState.Error -> {
                Text(text = s.message, color = Color.Red, modifier = Modifier.padding(16.dp))
            }
            is MenuState.Success -> {
                Text(
                    text = s.data.restaurantName,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    s.data.categories.forEach { category ->
                        // Category Header
                        item {
                            Surface(
                                color = Color.LightGray,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = category.name,
                                    modifier = Modifier.padding(8.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Items with Inventory Alert
                        items(category.items) { item ->
                            val finalPrice = viewModel.getEffectivePrice(category, item)

                            // FEATURE: Inventory Threshold Alert logic
                            val isLowStock = item.inventory < 5
                            val rowBackgroundColor = if (isLowStock) Color(0xFFFFCDD2) else Color.Transparent

                            Surface(color = rowBackgroundColor) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = item.name)
                                        if (isLowStock) {
                                            Text(
                                                text = "Low Stock: ${item.inventory}",
                                                color = Color.Red,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                    Text(
                                        text = "$${String.format("%.2f", finalPrice)}",
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            // Add a thin divider between items for clarity
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}

// Menu Hierarchy Inheritance: Calculate item prices by traversing a nested Category/Item tree
// (if an item's price is null, inherit from the parent).
@Composable
fun MenuHierarchyActivityScreen(viewModel: MenuViewModel) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        when (val s = state) {
            is MenuState.Loading -> CircularProgressIndicator()
            is MenuState.Error -> Text(text = s.message, color = Color.Red)
            is MenuState.Success -> {
                Text(text = s.data.restaurantName)

                LazyColumn {
                    s.data.categories.forEach { category ->
                        item {
                            // Category Row
                            Surface(color = Color.LightGray, modifier = Modifier.fillMaxWidth()) {
                                Text(text = category.name, modifier = Modifier.padding(8.dp))
                            }
                        }
                        items(category.items) { item ->
                            // Item Row with Inheritance
                            val finalPrice = viewModel.getEffectivePrice(category, item)
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = item.name)
                                Text(text = "$${finalPrice}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OptimisticUI(stockViewModel: StockViewModelOptimisticUI) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "PortfolioScreen") {

        // --- PORTFOLIO LIST SCREEN ---
        composable(route = "PortfolioScreen") {
            PortfolioScreenOptimistic(stockViewModel) { ticker ->
            }
        }
    }
}

@Composable
fun SharedRepoPattern(stockViewModel: RepoSharedUIStockViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "PortfolioScreen") {

        // --- PORTFOLIO LIST SCREEN ---
        composable(route = "PortfolioScreen") {
            PortfolioScreenSharedRepo(stockViewModel) { ticker ->
                // NAVIGATE: Use the unique ticker as the ID
                navController.navigate("StockDetail/${ticker}")
            }
        }

        // --- STOCK DETAIL SCREEN ---
        composable(
            route = "StockDetail/{ticker}",
            arguments = listOf(
                navArgument("ticker") { type = NavType.StringType }
            )
        ) { backstack ->
            val ticker = backstack.arguments?.getString("ticker") ?: ""

            // RENDER: Pass the ticker to the detail screen
            // The detail screen will use this ticker to find the stock in the repo-synced list
            StockDetailScreenSharedUIRepoPattern(
                ticker = ticker,
                viewModel = stockViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}


@Composable
fun SharedViewModel(stockViewModel: SharedStockViewModel){
    val navController = rememberNavController()

    NavHost(navController, startDestination = "PortfolioScreen") {
        composable(route= "PortfolioScreen"){
            PortfolioScreenSharedUIState(stockViewModel) { stockname ->
                navController.navigate("StockDetailScreenShared/${stockname}")
            }
        }
        composable(route = "StockDetailScreenShared/{stockname}", arguments = listOf(navArgument("stockname"){type = StringType})){
                backstack ->
            val stockname = backstack.arguments?.getString("stockname")
            StockDetailScreenShared(stockname, stockViewModel, onBack = { navController.popBackStack()})
        }
    }
}

@Composable
fun ImageUploadScreen(viewModel: ImageUploadViewModel) {
    val uploadList by viewModel.uploads.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        androidx.compose.material3.Button(
            onClick = { viewModel.startUploads(listOf("vacation.jpg", "selfie.png", "document.pdf")) },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text("Simulate Multi-Upload")
        }

        LazyColumn(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
           items(uploadList, key = {it.id}){
               item -> UploadRow(item)
           }
        }
    }
}

@Composable
fun UploadRow(item: UploadItem) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
        ) {
            Text(text = item.fileName)
            Text(
                text = when(item.state) {
                    UploadState.COMPLETED -> "Done"
                    UploadState.UPLOADING -> "${(item.progress * 100).toInt()}%"
                    else -> "Waiting"
                },
                color = if (item.state == UploadState.COMPLETED) Color.Green else Color.Gray
            )
        }

        androidx.compose.material3.LinearProgressIndicator(
            progress = { item.progress },
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            color = if (item.state == UploadState.COMPLETED) Color.Green else Color.Blue
        )
    }
}

@Composable
fun PaginationScreen(stockViewModel: StockViewModelPagination) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "PortfolioScreenPagination") {
        composable(route = "PortfolioScreenPagination") {
            // This is the updated UI we built in the previous step
            PortfolioScreenPagination(stockViewModel) { stockname ->
                navController.navigate("StockDetailScreenPagination/${stockname}")
            }
        }
        composable(
            route = "StockDetailScreenPagination/{stockname}",
            arguments = listOf(navArgument("stockname") { type = StringType })
        ) { backstack ->
            val stockname = backstack.arguments?.getString("stockname")
            // Reusing your existing Detail Screen or creating a generic one
            StockDetailScreen(stockname)
        }
    }
}

@Composable
fun LiveScreen(stockViewModel: StockViewModelLivePrice){
    val navController = rememberNavController()

    NavHost(navController, startDestination = "PortfolioScreenLive") {
        composable(route= "PortfolioScreenLive"){
            PortfolioScreenLive(stockViewModel) { stockname ->
                navController.navigate("StockDetailScreenLive/${stockname}")
            }

        }
        composable(route = "StockDetailScreenLive/{stockname}", arguments = listOf(navArgument("stockname"){type = StringType})){
                backstack ->
            val stockname = backstack.arguments?.getString("stockname")
            StockDetailScreenLive(stockname, stockViewModel)
        }
    }
}

@Composable
fun MultiFilter(stockViewModel: StockViewModelMultiStateFilter){
    val navController = rememberNavController()

    NavHost(navController, startDestination = "PortfolioScreen") {
        composable(route= "PortfolioScreen"){
            PortfolioScreenMultiState (stockViewModel) { stockname ->
                navController.navigate("StockDetailScreen/${stockname}")
            }

        }
        composable(route = "StockDetailScreen/{stockname}", arguments = listOf(navArgument("stockname"){type = StringType})){
                backstack ->
            val stockname = backstack.arguments?.getString("stockname")
            StockDetailScreen(stockname)
        }
    }
}

@Composable
fun StockScreenSearch(stockViewModel: StockViewModelDebounce){
    val navController = rememberNavController()

    NavHost(navController, startDestination = "PortfolioScreenSearch") {
        composable(route= "PortfolioScreenSearch"){
            PortfolioScreenSearch(stockViewModel) { stockname ->
                navController.navigate("StockDetailScreen/${stockname}")
            }

        }
        composable(route = "StockDetailScreen/{stockname}", arguments = listOf(navArgument("stockname"){type = StringType})){
                backstack ->
            val stockname = backstack.arguments?.getString("stockname")
            StockDetailScreen(stockname)
        }
    }

}

@Composable
fun StockScreen(stockViewModel: StockViewModel){
    val navController = rememberNavController()

    NavHost(navController, startDestination = "PortfolioScreen") {
        composable(route= "PortfolioScreen"){
            PortfolioScreen(stockViewModel) { stockname ->
                navController.navigate("StockDetailScreen/${stockname}")
            }
        }
        composable(route = "StockDetailScreen/{stockname}", arguments = listOf(navArgument("stockname"){type = StringType})){
            backstack ->
            val stockname = backstack.arguments?.getString("stockname")
            StockDetailScreen(stockname)
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SearchWithDebounceAndCancellationTheme {
        Greeting("Android")
    }
}