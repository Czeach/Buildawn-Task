package com.example.buildawntask.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.buildawntask.R

private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

class PermissionsFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!allPermissionsGranted(requireContext())) {
            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        } else {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(
                PermissionsFragmentDirections.actionPermissionsFragmentToCameraFragment()
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(
                    PermissionsFragmentDirections.actionPermissionsFragmentToCameraFragment()
                )
                Toast.makeText(context, "Permission request granted", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            isRemoving
        }
    }

    private fun allPermissionsGranted(context: Context) = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(
            context, it) == PackageManager.PERMISSION_GRANTED
    }
}