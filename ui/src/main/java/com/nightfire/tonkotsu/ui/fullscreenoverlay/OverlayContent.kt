package com.nightfire.tonkotsu.ui.fullscreenoverlay

import com.nightfire.tonkotsu.core.domain.model.Image
import com.nightfire.tonkotsu.core.domain.model.Video

sealed class OverlayContent {
    /**
     * Content for displaying a single image.
     * @param image The Image domain model.
     */
    data class ImageFullScreen(val image: Image) : OverlayContent()

    /**
     * Content for displaying a video, typically embedded.
     * @param video The Video domain model.
     * @param title An optional title for the video.
     */
    data class VideoFullScreen(val video: Video, val title: String?) : OverlayContent()

    /**
     * Content for displaying a full text review.
     * @param title The title of the review.
     * @param content The full text content of the review.
     * @param author The author of the review.
     */
    data class ReviewFullScreen(val title: String, val content: String, val author: String) : OverlayContent()

}