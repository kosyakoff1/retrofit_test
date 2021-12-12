package android.example.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var retService: AlbumService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retService = RetrofitInstance.getInstance().create(AlbumService::class.java)
        //getRequestWithQueryParameters()
        getRequestWithPathParameters()
        //uploadAlbum()
    }

    private fun getRequestWithQueryParameters() {

        val responseLiveData:LiveData<Response<Albums>> = liveData {
            val response = retService.getSortedAlbums(3)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val albumsList: MutableListIterator<AlbumsItem>? = it.body()?.listIterator()
            if (albumsList != null) {
                while (albumsList.hasNext()) {
                    val albumsItem = albumsList.next()
                    val result = " "+"Album id: ${albumsItem.id}"  + "\n" +
                            " "+"Album userId: ${albumsItem.userId}"  + "\n" +
                            " "+"Album title: ${albumsItem.title}"  + "\n\n\n"
                    findViewById<TextView>(R.id.text_view).append(result)
                }
            }
        })
    }

    private fun getRequestWithPathParameters() {
        val pathResponse: LiveData<Response<AlbumsItem>> = liveData {
            val album = retService.getAlbum(3)
            emit(album)
        }

        pathResponse.observe(this) {
            val title = it.body()?.title
            Toast.makeText(this, title, Toast.LENGTH_LONG).show()
        }
    }

    private fun uploadAlbum() {
        val album = AlbumsItem(0, "My title", 3)
        val postResponse : LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.uploadAlbum(album)
            emit(response)
        }

        postResponse.observe(this, {
            val receivedAlbumsItem = it.body()

            val result = " "+"Album id: ${receivedAlbumsItem?.id}"  + "\n" +
                    " "+"Album userId: ${receivedAlbumsItem?.userId}"  + "\n" +
                    " "+"Album title: ${receivedAlbumsItem?.title}"  + "\n\n\n"
            findViewById<TextView>(R.id.text_view).text = result
        })
    }
}