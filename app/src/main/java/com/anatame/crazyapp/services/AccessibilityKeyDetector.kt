package com.anatame.crazyapp.services

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.anatame.crazyapp.MessageData


class AccessibilityKeyDetector : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        when(event.eventType){
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                if(event.source.contentDescription == "Toggle emoji keyboard"){
                    val eventPackageName = event.packageName
                    val className = event.className
                    val source: AccessibilityNodeInfo? = event.source
                    val targetAppPackageName = "com.discord"
                    val targetViewId = "text_input"
                    val viewsToCheck = rootInActiveWindow?.findAccessibilityNodeInfosByViewId("$targetAppPackageName:id/$targetViewId")?.getOrNull(0)

                    val arguments = Bundle()
                    arguments.putCharSequence(
                        AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                        MessageData.message
                    )
                    viewsToCheck?.performAction(AccessibilityAction.ACTION_SET_TEXT.id, arguments)
                }
            }
        }
    }


    override fun onInterrupt() {
        //whatever
    }

    override fun onServiceConnected() {
    }

    override fun startService(service: Intent?): ComponentName? {
        return super.startService(service)
    }


}
