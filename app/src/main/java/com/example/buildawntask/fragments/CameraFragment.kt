package com.example.buildawntask.fragments

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.camera.core.CameraSelector
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat

import com.example.buildawntask.R
import kotlinx.android.synthetic.main.fragment_camera.*

/**
 * A simple [Fragment] subclass.
 */

private const val PERMISSIONS_REQUEST_CODE = 10
class CameraFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, container, false)

    }

//    private fun startCamera() {
//        CameraX.unbindAll()
//
//        val preview = createPreviewUseCase()
//
//    }
//
//    private fun createPreviewUseCase(): Preview {
//
//    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                Toast.makeText(context, "Permission request granted", Toast.LENGTH_LONG).show()
                startCamera()
            }
        } else {
            Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            isRemoving
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()

            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                cameraProvider.unbindAll()

                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview
                )
                preview.setSurfaceProvider(view_finder.createSurfaceProvider(camera.cameraInfo))
            } catch(e: Exception) {
                Log.e(TAG, "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(activity))
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        view_camera.bindToLifecycle(this)
//    }
}
