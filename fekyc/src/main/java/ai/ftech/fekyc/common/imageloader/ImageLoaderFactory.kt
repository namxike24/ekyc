package ai.ftech.fekyc.common.imageloader

object ImageLoaderFactory {
    private val imageLoader = GlideImageLoaderImpl()

    fun glide(): IImageLoader {
        return imageLoader
    }
}
