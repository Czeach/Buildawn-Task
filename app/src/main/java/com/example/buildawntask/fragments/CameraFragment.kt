package com.example.buildawntask.fragments

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.buildawntask.MainActivity

import com.example.buildawntask.R
import kotlinx.android.synthetic.main.fragment_camera.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
* Camera fragment implements the camera operations
*/

class CameraFragment : Fragment() {

    private lateinit var container: ConstraintLayout
    private lateinit var viewFinder: PreviewView
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var broadcastManager: LocalBroadcastManager
    private lateinit var countDownTimer: CountDownTimer

    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    private var  camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var outputDirectory: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_camera, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        container = view as ConstraintLayout
        //initialize the camera preview
        viewFinder = container.findViewById(R.id.view_finder)

        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

//        outputDirectory = MainActivity.getOutputDirectory(requireContext())

        viewFinder.post {
            // Set up the camera and its use cases
            startCamera()

            Toast.makeText(requireContext(), "camera started", Toast.LENGTH_SHORT).show()

            startTimer()
        }
    }

    /** Initialize CameraX, and prepare to bind the camera use cases  */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {

            // initialize CameraProvider
            cameraProvider = cameraProviderFuture.get()

            // initialize and build Preview
            preview = Preview.Builder()
                .build()

            // Select front camera
            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()

//            val screenAspectRatio = aspect

            // Set up the capture use case to allow users to take photos.
            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
//                .setTargetAspectRatio()
                .build()

            // Must unbind all use cases before binding them
            cameraProvider?.unbindAll()

            try {
                // Bind and pass in desired use cases
                camera = cameraProvider?.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                // Attach the viewfinder's surface provider to preview use case
                preview?.setSurfaceProvider(viewFinder.createSurfaceProvider(camera?.cameraInfo))
            } catch (e: Exception) {
                Log.e(TAG, "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {

        // Get a stable reference of the modifiable image capture use case
        imageCapture.let {
            val photoFile = File(
                outputDirectory, SimpleDateFormat(FILENAME, Locale.US)
                    .format(System.currentTimeMillis()) + PHOTO_EXTENSION
            )

            val outputOptions = ImageCapture.OutputFileOptions
                .Builder(photoFile)
                .build()

            it?.takePicture(
                outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {

                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val savedUri = Uri.fromFile(photoFile)
                        Toast.makeText(requireContext(), "Photo captured successfully: $savedUri", Toast.LENGTH_SHORT).show()
                        Log.d("CameraFragment", "Photo captured successfully")
                    }

                    override fun onError(e: ImageCaptureException) {
                        Log.d(TAG, "Photo capture failed: ${e.message}")
                    }
                }
            )
        }
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onFinish() {
                Toast.makeText(requireContext(), "timer complete", Toast.LENGTH_LONG).show()
                takePhoto()
            }

            override fun onTick(millisUntilFinished: Long) {
                Toast.makeText(requireContext(), "seconds remaining: $millisUntilFinished", Toast.LENGTH_LONG).show()
                Log.d("CameraFragment", "seconds remaining: ${millisUntilFinished / 1000}")
            }

        }
        countDownTimer.start()
    }

    companion object {

        private const val TAG = "CameraXBasic"
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
//        private const val RATIO_4_3_VALUE = 4.0 / 3.0
//        private const val RATIO_16_9_VALUE = 16.0 / 9.0

        /** Helper function used to create a timestamped file */
        private fun createFile(baseFolder: File, format: String, extension: String) =
            File(baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension)
    }
}
