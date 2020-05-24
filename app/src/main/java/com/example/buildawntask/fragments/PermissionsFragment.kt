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

/**
 * The purpose of this fragment is to request permissions and, once granted, display the
 * camera fragment to the user.
 */

private const val PERMISSIONS_REQUEST_CODE = 10
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

class PermissionsFragment: Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!allPermissionsGranted(requireContext())) {
            // display camera permissions
            requestPermissions(PERMISSIONS_REQUIRED, PERMISSIONS_REQUEST_CODE)
        } else {
            // If permissions are granted, proceed to camera fragment
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(
                PermissionsFragmentDirections.actionPermissionsFragmentToCameraFragment()
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                // Take the user to the camera fragment if permission is granted
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(
                    PermissionsFragmentDirections.actionPermissionsFragmentToCameraFragment()
                )
            }
        } else {
            Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            isRemoving
        }
    }

    // Method used to check if all permissions are granted
    private fun allPermissionsGranted(context: Context) = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(
            context, it) == PackageManager.PERMISSION_GRANTED
    }
}