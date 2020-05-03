package ru.evtukhov.android.homework6

import android.os.Bundle
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import io.ktor.client.request.get
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import ru.evtukhov.android.homework6.adapter.PostAdapter
import ru.evtukhov.android.homework6.client.Api
import ru.evtukhov.android.homework6.posts.Post
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @io.ktor.util.KtorExperimentalAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch {
            val list = withContext(Dispatchers.IO) {
                Api.client.get<List<Post>>(Api.urlJson)
            }
            with(itemList) {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = PostAdapter(list)
            }
            progress_circular.visibility = GONE
            Toast.makeText(this@MainActivity, "Length: ${list.size}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
