package com.simplemobiletools.voicerecorder.helpers

import android.content.Context
import android.util.Log
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import java.io.File


class FfmpegHelper(val context: Context) {
    val TAG = "FfmpegHelper"

    fun execute(filePath: String, callback: () -> Unit) {
        val tempFile = File(File(filePath).parent, "tmp-${File(filePath).name}")

        val cmd = mutableListOf<String>()
        cmd.add("-y")
        cmd.add("-i")
        cmd.add("'$filePath'")
        cmd.add("-ar")
        cmd.add("44100")
        cmd.add("-b:a")
        cmd.add("320k")
        cmd.add("-ac")
        cmd.add("1")
        cmd.add("'${tempFile.absolutePath}'")

        val joinToString = cmd.joinToString(" ")
        val session = FFmpegKit.execute(joinToString)
        if (ReturnCode.isSuccess(session.returnCode)) {
            Log.d(TAG, "execute: success")
            callback.invoke()
        } else if (ReturnCode.isCancel(session.returnCode)) {
            Log.d(TAG, "execute: cancel")
        } else {
            Log.d(TAG, String.format("Command failed with state %s and rc %s.%s", session.state, session.returnCode, session.failStackTrace))
        }
    }
}
