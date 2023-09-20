package com.example.mynotes

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.nfc.cardemulation.HostNfcFService
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mynotes.dataBase.NotesDataBase
import com.example.mynotes.databinding.FragmentCreateNoteBinding
import com.example.mynotes.databinding.NotesBottomSheetBarBinding
import com.example.mynotes.entities.NotesBuilder
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date


class CreateNoteFragment : BaseFragment() {
    private lateinit var binding: FragmentCreateNoteBinding
    private lateinit var currentDate: String
    private lateinit var imgUri: Uri
    private var uriString = ""
    private lateinit var noteBottomSheetDialog: BottomSheetDialog
    private var backgroundItemNote = "lightBlack"
    val someActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                imgUri = result?.data?.data!!
                binding.imgNote.setImageURI(imgUri)
                uriString = getPathFromUri(imgUri)!!
                noteBottomSheetDialog.dismiss()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateNoteBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        binding.tvDatTime.text = currentDate
        binding.showBottomSheetDialog.setOnClickListener {
            noteBottomSheet()
        }
        binding.btnTick.setOnClickListener {
            saveNote()
        }
        binding.btnBack.setOnClickListener {
            replaceFragment(HomeFragment(),false)
        }

    }

    private fun saveNote() {
        if(binding.edtNoteTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"Note title is required",Toast.LENGTH_SHORT).show()
        }

        if (binding.edtSubNoteTitle.text.isNullOrEmpty()){
            Toast.makeText(context,"Note sub title is required",Toast.LENGTH_SHORT).show()
        }
        if (binding.edtDesc.text.isNullOrEmpty()){
            Toast.makeText(context,"Note Description is required",Toast.LENGTH_SHORT).show()
        }

        launch {
            val note = NotesBuilder()
                .setTitle(binding.edtNoteTitle.text.toString())
                .setSubTitle(binding.edtSubNoteTitle.text.toString())
                .setContent(binding.edtDesc.text.toString())
                .setDateTime(currentDate!!)
                .setNoteColor(backgroundItemNote)
                .setImgPath(uriString)
                .build()
            context?.let { NotesDataBase.getDatabase(it).noteDao().insertNote(note) }
        }
        replaceFragment(HomeFragment(),false)
    }

    private fun noteBottomSheet(){
        val dialogBinding = NotesBottomSheetBarBinding.inflate(layoutInflater)
        noteBottomSheetDialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogThem)
        noteBottomSheetDialog.setContentView(dialogBinding.root)
        dialogBinding.colorMagenta.setOnClickListener {
            backgroundItemNote = "Magenta"
            binding.colorNoteItem.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.magenta))
        }

        dialogBinding.colorOrange.setOnClickListener {
            backgroundItemNote = "Orange"
            binding.colorNoteItem.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.orange))
        }

        dialogBinding.colorTomato.setOnClickListener {
            backgroundItemNote = "Tomato"
            binding.colorNoteItem.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.Tomato))
        }

        dialogBinding.colorSalmon.setOnClickListener {
            backgroundItemNote = "Salmon"
            binding.colorNoteItem.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.salmon))
        }

        dialogBinding.colorYellow.setOnClickListener {
            backgroundItemNote = "Yellow"
            binding.colorNoteItem.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.yellow))
        }
        dialogBinding.btnAddImg.setOnClickListener {
            checkPermissionGallry()
        }
        noteBottomSheetDialog.show()
    }

    private fun replaceFragment(fragment: Fragment, isTransition: Boolean){
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        if (isTransition){
            fragmentTransaction.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransaction.replace(R.id.fragmentLayout,fragment).commit()
    }


    private fun checkPermissionGallry() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_MEDIA_IMAGES)
            .withListener(
                object : PermissionListener {
                    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                        gallery()
                    }

                    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                        Toast.makeText(
                            requireContext(),
                            "bạn chưa cấp quyền",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        permissionRationaleShouldBeShown()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: PermissionRequest?,
                        p1: PermissionToken?
                    ) {
                        permissionRationaleShouldBeShown()
                    }

                }
            ).onSameThread().check()
    }

    private fun permissionRationaleShouldBeShown() {
        AlertDialog.Builder(requireContext())
            .setMessage("Bạn chưa cấp quyền cho máy ảnh hãy đến cài đặt")
            .setPositiveButton("đến cài đặt") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            .setNegativeButton("thoát") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath:String? = null
        var cursor = requireActivity().contentResolver.query(contentUri,null,null,null,null)
        if (cursor == null){
            filePath = contentUri.path
        }else{
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }
    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        someActivityForResult.launch(intent)
    }

}