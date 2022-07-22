package com.zahar2.andr.connections

import com.zahar2.andr.data.Video
import com.zahar2.andr.data.toVideo
import io.ktor.server.websocket.*

class VideoConnection: Connection<Video>() {

    private var videoNow = Video.NONE
    var onVideoEnd: (suspend () -> Unit)? = null

    override suspend fun onNewConnection(session: DefaultWebSocketServerSession) {
        session.send(videoNow)
    }

    override fun String.toType(): Video = toVideo()

    override suspend fun update(data: Video) {
        videoNow = data
        onVideoEnd?.invoke()
    }

    suspend fun change(video: Video) {
        if (this.videoNow == video) return
        videoNow = video
        broadcast(videoNow)
    }
}