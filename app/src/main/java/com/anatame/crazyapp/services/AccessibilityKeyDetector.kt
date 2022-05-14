package com.anatame.crazyapp.services

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction
import android.view.accessibility.AccessibilityWindowInfo
import com.anatame.crazyapp.CommonData
import com.anatame.crazyapp.EmoteMenu


class AccessibilityKeyDetector : AccessibilityService() {

    var isEmoteMenuVisible: Boolean = false
    var tray: AccessibilityNodeInfo? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent) {

        when(event.eventType){
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {
                event.source?.let{
                    if (event.source.contentDescription == "Toggle emoji keyboard") {
                        tray = null

                        val eventPackageName = event.packageName
                        val className = event.className
                        val source: AccessibilityNodeInfo? = event.source
                        val targetAppPackageName = "com.discord"
                        val targetViewId = "text_input"
                        val viewsToCheck =
                            rootInActiveWindow?.findAccessibilityNodeInfosByViewId("$targetAppPackageName:id/$targetViewId")
                                ?.getOrNull(0)

                        pasteText(viewsToCheck)
                    }
                }
            }

            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                handleEmoteMenu()
            }
        }
    }

    private fun handleEmoteMenu() {
        if (tray == null) {
            tray =
                rootInActiveWindow?.findAccessibilityNodeInfosByViewId("com.discord:id/expression_tray_container")
                    ?.getOrNull(0)
            if (tray == null) {
                if (isEmoteMenuVisible) {
                    println("Tray is closed")
                    closeEmoteMenu()
                    isEmoteMenuVisible = false
                }
            } else {
                println(tray)
                showEmoteMenu()
                isEmoteMenuVisible = true
            }
        }
    }


    fun closeEmoteMenu(){
        println("Emote Menu has been Closed")
        CommonData.emoteMenuStatus(EmoteMenu.Closed)
    }

    fun showEmoteMenu(){
        println("Emote Menu is now Open")
        CommonData.emoteMenuStatus(EmoteMenu.Open)
    }


    private fun pasteText(viewsToCheck: AccessibilityNodeInfo?) {
        val arguments = Bundle()
        arguments.putCharSequence(
            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
            CommonData.message
        )
        viewsToCheck?.performAction(AccessibilityAction.ACTION_SET_TEXT.id, arguments)
    }

    fun isKeyboardOpened(): Boolean {
        val windowInfoList = windows
        for (k in windowInfoList.indices) {
            if (windowInfoList[k].type == AccessibilityWindowInfo.TYPE_INPUT_METHOD) {
                Log.i("keyBoardStatus", "keyboard is opened!")
                println("keyboard is opened!")
                return true
            }
        }
        return false
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
