package rachmanforniandi.healthnews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import rachmanforniandi.healthnews.R

class AddUpdateDataActivity : AppCompatActivity() {

    companion object {
        private const val CAMERA = 1

        //Step 1: Add the constant for Gallery.
        // START
        private const val GALLERY = 2
        // END
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_data)
    }
}