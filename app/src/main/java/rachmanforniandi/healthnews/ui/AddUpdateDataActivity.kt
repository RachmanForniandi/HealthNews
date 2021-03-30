package rachmanforniandi.healthnews.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.rachmanforniandi.mahasiswacrudapp.model.insert.ResponseInsert
import com.rachmanforniandi.mahasiswacrudapp.model.read.ItemDataRead
import com.rachmanforniandi.mahasiswacrudapp.model.update.ResponseUpdate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_add_update_data.*
import kotlinx.android.synthetic.main.custom_dialog_image_selection.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import rachmanforniandi.healthnews.BuildConfig
import rachmanforniandi.healthnews.R
import rachmanforniandi.healthnews.viewmodels.InsertViewModel
import rachmanforniandi.healthnews.viewmodels.RegisterViewModel
import rachmanforniandi.healthnews.viewmodels.UpdateViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDataActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val CAMERA = 1

        //Step 1: Add the constant for Gallery.
        // START
        private const val GALLERY = 2
        // END

        private const val IMG_DIRECTORY ="ImgDir"
    }

    private var mImgPath:String =""
    private lateinit var viewModel1: InsertViewModel
    private lateinit var viewModel2:UpdateViewModel
    private lateinit var bundleData:ItemDataRead

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_data)

        viewModel1 = ViewModelProvider(this).get(InsertViewModel::class.java)
        viewModel2 = ViewModelProvider(this).get(UpdateViewModel::class.java)

        bundleData = intent.getSerializableExtra("detailNews")as ItemDataRead

        iv_add_image.setOnClickListener(this)
        btnAction.setOnClickListener(this)

    }

    private fun customOptionImageInputSelectionDialog() {
        val optionTakeImageDialog= Dialog(this)
        optionTakeImageDialog.setContentView(R.layout.custom_dialog_image_selection)

        txt_option_camera.setOnClickListener {
            //Toast.makeText(this,"Option Camera Selected",Toast.LENGTH_SHORT).show()
            Dexter.withContext(this@AddUpdateDataActivity)
                .withPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    //Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {
                            if (report.areAllPermissionsGranted()){
                                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                startActivityForResult(intent, CAMERA)
                                //Toast.makeText(this@AddUpdateDishActivity,"Option Camera Selected",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?,
                        token: PermissionToken?) {
                        showRationalDialogForPermissions()
                    }
                }).onSameThread()
                .check()
            optionTakeImageDialog.dismiss()
        }

        txt_option_gallery.setOnClickListener {
            //Toast.makeText(this,"Option Gallery Selected",Toast.LENGTH_SHORT).show()

            Dexter.withContext(this@AddUpdateDataActivity)
                .withPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                        startActivityForResult(galleryIntent, GALLERY)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        Toast.makeText(this@AddUpdateDataActivity,"You have denied the storage permission to select image.",
                            Toast.LENGTH_SHORT).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                        showRationalDialogForPermissions()
                    }
                }).onSameThread()
                .check()
            optionTakeImageDialog.dismiss()
        }
        optionTakeImageDialog.show()
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton("GO TO SETTINGS"){ _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel"){
                    dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){

            if (requestCode == CAMERA){
                data?.extras.let{
                    val thumbnail: Bitmap = data?.extras?.get("data") as Bitmap
                    //mBinding.imgDishInput.setImageBitmap(thumbnail)
                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .into(iv_display_image)

                    mImgPath = saveImageToInternalStorage(thumbnail)

                    Log.i("imgPath", mImgPath)

                    iv_add_image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                }
            }else if (requestCode == GALLERY){
                data?.extras.let{
                    val selectedPhotoUri= data?.data
                    iv_display_image.setImageURI(selectedPhotoUri)

                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e("TAG","Error loading image",e)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let{
                                    val bitmap: Bitmap = resource.toBitmap()
                                    mImgPath = saveImageToInternalStorage(bitmap)
                                    Log.i("imagePath",mImgPath)
                                }
                                return false
                            }
                        })
                        .into(iv_display_image)

                    iv_display_image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                }
            }else if (resultCode == Activity.RESULT_CANCELED){
                Log.e("Canceled","Canceled")
            }
        }
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap):String{
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMG_DIRECTORY, Context.MODE_PRIVATE)

        file = File(file,"${UUID.randomUUID()}.jpg")

        try {
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return file.absolutePath
    }

    override fun onClick(view: View?) {
        if (view != null){
            when(view.id){
                R.id.iv_add_image->{
                    customOptionImageInputSelectionDialog()
                    return
                }

                R.id.btnAction->{
                    if (bundleData == null){

                        btnAction.text ="Submit News"

                        btnAction.onClick {
                            val title:RequestBody = etTitle.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val contentNews:RequestBody = etContent_news.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val author:RequestBody = etAuthor.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

                            val fileInsert = File(mImgPath)
                            val requestFile:RequestBody = fileInsert.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val bodyInsert:MultipartBody.Part = MultipartBody.Part.createFormData("image",fileInsert.name,requestFile)
                            viewModel1.insertDataNews(title,contentNews,author,bodyInsert)
                        }
                        observeInsertData()

                    }else{
                        btnAction.text ="Update News"

                        etTitle.setText(bundleData.title)
                        etContent_news.setText(bundleData.content_news)
                        etAuthor.setText(bundleData.author)

                        Glide.with(this)
                                .load(BuildConfig.URL_IMAGE_DATA + bundleData.image)
                                .error(R.drawable.place_holder)
                                .into(iv_display_image)

                        btnAction.onClick {
                            val fileUpdate = File(mImgPath)
                            val requestFile:RequestBody = fileUpdate.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                            val bodyUpdate:MultipartBody.Part = MultipartBody.Part.createFormData("image",fileUpdate.name,requestFile)

                            viewModel2.updateDataNews((bundleData.id ?:"".toRequestBody("multipart/form-data".toMediaTypeOrNull())) as RequestBody,
                                    etTitle.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    etContent_news.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                                    etAuthor.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),bodyUpdate)

                        }

                        observeUpdateData()
                    }
                }

            }
        }
    }

    private fun observeInsertData() {
        viewModel1.isLoading.observe(this,{loadingProcessInsert(it)})
        viewModel1.insertResponder.observe(this,{showResponseInsert(it)})
        viewModel1.apiError.observe(this,{errorResponseInsert(it)})
        viewModel1.isEmptyInput.observe(this, { showIsEmptyForInsert(it) })
    }




    private fun observeUpdateData() {
        viewModel2.isLoading.observe(this,{loadingProcessUpdate(it)})
        viewModel2.updateResponder.observe(this,{showResponseUpdate(it)})
        viewModel2.apiError.observe(this,{errorResponseUpdate(it)})
        viewModel2.isEmptyInput.observe(this, { showIsEmptyForUpdate(it) })
    }

    private fun showIsEmptyForUpdate(it: Boolean) {
        if (it)toast("Mohon isi semua input semua informasi yang ada untuk menambahkan data")
    }

    private fun showIsEmptyForInsert(it: Boolean) {
        if (it)toast("Mohon isi semua input semua informasi yang ada untuk mengubah data")
    }

    private fun loadingProcessInsert(it: Boolean?) {
        if (it == true) {
            progress_processing_data.visibility =View.VISIBLE
        } else {
            progress_processing_data.visibility = View.GONE
        }
    }
    private fun showResponseInsert(it: ResponseInsert?) {
        if (it?.message.equals("Data Berhasil Ditambahkan.")){
            startActivity<MainActivity>()
            finish()
        }else{
            toast(it?.message ?:"")
        }
    }
    private fun errorResponseInsert(it: Throwable?) {
        toast(it?.message ?: "")
    }
    private fun loadingProcessUpdate(it: Boolean?) {
        if (it == true) {
            progress_processing_data.visibility =View.VISIBLE
        } else {
            progress_processing_data.visibility = View.GONE
        }
    }
    private fun showResponseUpdate(it: ResponseUpdate?) {
        if (it?.message.equals("Data Berhasil diperbarui.")){
            startActivity<MainActivity>()
            finish()
        }else{
            toast(it?.message ?:"")
        }

    }
    private fun errorResponseUpdate(it: Throwable?) {
        toast(it?.message ?: "")
    }

}