package ai.ftech.ekyc.presentation.picture.confirm

import ai.ftech.dev.base.adapter.group.GroupAdapter
import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.observer
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.ekyc.PhotoConfirmDetailInfo
import ai.ftech.ekyc.domain.model.ekyc.PhotoInfo
import ai.ftech.ekyc.presentation.dialog.ConfirmDialog
import ai.ftech.ekyc.presentation.info.EkycInfoActivity
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ConfirmPictureActivity : FEkycActivity(R.layout.fekyc_confirm_picture_activity) {
    private lateinit var tbvHeader: ToolbarView
    private lateinit var rvPhoto: RecyclerView
    private lateinit var btnContinue: Button
    private val viewModel by viewModels<ConfirmPictureViewModel>()
    private val adapter = GroupAdapter()

    override fun onInitView() {
        super.onInitView()
        tbvHeader = findViewById(R.id.tbvConfirmPictureHeader)
        rvPhoto = findViewById(R.id.rvConfirmPicturePhotoList)
        btnContinue = findViewById(R.id.btnConfirmPictureContinues)

        rvPhoto.layoutManager = LinearLayoutManager(this)
        rvPhoto.adapter = adapter
        viewModel.getConfirmPhotoList()


        tbvHeader.setListener(object : ToolbarView.IListener {
            override fun onLeftIconClick() {
                showConfirmDialog()
            }
        })


        btnContinue.setOnSafeClick {
            navigateTo(EkycInfoActivity::class.java)
        }

    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()
        observer(viewModel.photoConfirmDetailInfoList) {
            createConfirmPictureGroup(it)
        }
    }

    override fun getContainerId() = R.id.flconfirmPictureFrame

    private fun createConfirmPictureGroup(list: MutableList<PhotoConfirmDetailInfo>?) {
        list?.forEach { photoConfirmDetailInfo ->
            val groupData = ConfirmPictureGroup(photoConfirmDetailInfo).apply {
                this.listener = object : ConfirmPictureGroup.IListener {
                    override fun onClickItem(item: PhotoInfo) {
                        Log.d(TAG, "onClickItem: ${item.uploadType?.name}")
                        replaceFragment(ConfirmPictureFragment())
                    }
                }
            }
            adapter.addGroupData(groupData)
        }
        adapter.notifyAllGroupChanged()
    }
}
