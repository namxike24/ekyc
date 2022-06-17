package ai.ftech.ekyc.presentation.picture.confirm

import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycFragment
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView

class ConfirmPictureFragment : FEkycFragment(R.layout.fekyc_confirm_picture_fragment) {

    private lateinit var tbvHeader: ToolbarView

    override fun onPrepareInitView() {
        super.onPrepareInitView()
    }

    override fun onInitView() {
        super.onInitView()
        tbvHeader = viewRoot.findViewById(R.id.tbvConfirmPictureHeader)

        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onLeftIconClick() {
                backFragment()
            }
        })
    }
}
