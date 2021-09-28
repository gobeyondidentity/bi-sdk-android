package com.beyondidentity.embedded.embeddedui.ui

import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun FragmentManager.clearFragments(fragmentsTags: Set<String>) {
    this.fragments.forEach { fragment ->
        if (fragmentsTags.contains(fragment.tag)) {
            (fragment as? BottomSheetDialogFragment)?.dismiss()
        }
    }
}
