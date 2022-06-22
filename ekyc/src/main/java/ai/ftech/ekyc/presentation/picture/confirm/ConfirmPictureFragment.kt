package ai.ftech.ekyc.presentation.picture.confirm

import ai.ftech.dev.base.extension.observer
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycFragment
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager

class ConfirmPictureFragment : FEkycFragment(R.layout.fekyc_confirm_picture_fragment) {
    private lateinit var tbvHeader: ToolbarView
    private lateinit var vpPhoto: ViewPager
    private lateinit var ivBack: ImageView
    private lateinit var ivNext: ImageView

    private val activityViewModel by activityViewModels<ConfirmPictureViewModel>()
    private val adapter = PreviewPhotoAdapter()

    override fun onDestroy() {
        super.onDestroy()
        activityViewModel.clearSelected()
    }

    override fun onPrepareInitView() {
        super.onPrepareInitView()
    }

    override fun onInitView() {
        super.onInitView()
        tbvHeader = viewRoot.findViewById(R.id.tbvConfirmPictureHeader)
        vpPhoto = viewRoot.findViewById(R.id.vpConfirmPicture)
        ivBack = viewRoot.findViewById(R.id.ivConfirmPictureBack)
        ivNext = viewRoot.findViewById(R.id.ivConfirmPictureNext)

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onLeftIconClick() {
                backFragment()
            }
        })

        vpPhoto.apply {
            offscreenPageLimit = this@ConfirmPictureFragment.adapter.count
            post {
                currentItem = activityViewModel.getSelectedIndex()
            }
            adapter = this@ConfirmPictureFragment.adapter
        }


        ivBack.setOnSafeClick {
            var index = vpPhoto.currentItem
            if (index >= 0) {
                vpPhoto.currentItem = --index
            }
        }

        ivNext.setOnSafeClick {
            var index = vpPhoto.currentItem
            if (index <= adapter.count) {
                vpPhoto.currentItem = ++index
            }
        }
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        observer(activityViewModel.photoInfoList) {
            adapter.dataList = it
        }
    }
}
