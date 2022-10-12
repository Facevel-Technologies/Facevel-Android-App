package com.facevel.inc.app.utils

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.facevel.inc.app.R


object NavigationAnimations {
    fun getAlphaAnimation() =
        NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
            .build()


    /*   fun getSlideUpAnimation() =
           NavOptions.Builder()
               .setEnterAnim(R.anim.slide_up)
               .setExitAnim(R.anim.fade_out)
               .setPopEnterAnim(R.anim.fade_in)
               .setPopExitAnim(R.anim.slide_down)
               .build()*/
}

/*  findNavController().safeNavigate(
                R.id.action_termuxDownloadInstallFragment_to_termuxFragment,
                null,
                NavigationAnimations.getAlphaAnimation()
            )*/

fun NavController.safeNavigate(action: Int, arguments: Bundle?, options: NavOptions) {
    /*
    * We have to check if destination is the same as the current fragment
    *  */
    val destinationId = currentDestination?.getAction(action)?.destinationId
    val currentDestinationId = this.currentDestination?.id
    if (destinationId != currentDestinationId && destinationId != null) {
        navigate(action, arguments, options)
    }
}
