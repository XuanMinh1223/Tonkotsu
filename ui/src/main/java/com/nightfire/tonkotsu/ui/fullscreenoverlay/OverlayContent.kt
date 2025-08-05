package com.nightfire.tonkotsu.ui.fullscreenoverlay

import com.nightfire.tonkotsu.core.domain.model.AnimeReview
import com.nightfire.tonkotsu.core.domain.model.Image
import com.nightfire.tonkotsu.core.domain.model.Video

sealed class OverlayContent {

    /**
     * Content for displaying a gallery of images with navigation.
     * @param images The list of Image domain models in the gallery.
     * @param initialIndex The index of the image that was initially clicked.
     */
    data class ImageGalleryFullScreen(val images: List<Image>, val initialIndex: Int) : OverlayContent()

    /**
     * Content for displaying a video, typically embedded.
     * @param video The Video domain model.
     * @param title An optional title for the video.
     */
    data class VideoFullScreen(val videos: List<Video>, val title: String?,  val initialIndex: Int = 0) : OverlayContent()

    /**
     * Content for displaying a gallery of reviews with navigation.
     * @param reviews The list of ReviewData models in the gallery.
     * @param initialIndex The index of the review that was initially clicked.
     */
    data class ReviewFullScreen(
        val reviews: List<AnimeReview>,
        val initialIndex: Int
    ) : OverlayContent()

}