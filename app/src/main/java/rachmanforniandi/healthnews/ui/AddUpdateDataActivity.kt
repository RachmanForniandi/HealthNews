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
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
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
import com.rachmanforniandi.mahasiswacrudapp.model.read.ItemDataRead
import kotlinx.android.synthetic.main.activity_add_update_data.*
import kotlinx.android.synthetic.main.custom_dialog_image_selection.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import rachmanforniandi.healthnews.R
import rachmanforniandi.healthnews.viewmodels.InsertViewModel
import rachmanforniandi.healthnews.viewmodels.RegisterViewModel
import rachmanforniandi.healthnews.viewmodels.UpdateViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDataActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_update_data)

        viewModel1 = ViewModelProvider(this).get(InsertViewModel::class.java)
        viewModel2 = ViewModelProvider(this).get(UpdateViewModel::class.java)

        val bundleData = intent.getSerializableExtra("detailNews")as ItemDataRead?

        iv_add_image.onClick {
            customOptionImageInputSelectionDialog()
        }



        if (bundleData == null){

            btnAction.text ="Tambah Berita"

            btnAction.onClick {
               /* corePresenter.partToAddData(etNik.text.toString(),
                    etNama.text.toString(), etJurusan.text.toString(), etFakultas.text.toString(), etNoHp.text.toString(), etAlamat.text.toString(), etHobi.text.toString())*/

            }

        }else{
            btnAction.text ="Update"

            val item = bundleData as ItemDataRead?
            /*etNama.setText(item?.nama)
            etNik.setText(item?.nik)
            etJurusan.setText(item?.jurusan)
            etFakultas.setText(item?.fakultas)
            etNoHp.setText(item?.noHp)
            etAlamat.setText(item?.alamat)
            etHobi.setText(item?.hobi)*/

            btnAction.onClick {

            }
        }


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


}