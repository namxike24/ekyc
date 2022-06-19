package ai.ftech.ekyc.presentation.picture.confirm

import ai.ftech.dev.base.adapter.group.GroupAdapter
import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.observer
import ai.ftech.dev.base.extension.setOnSafeClick
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.common.widget.toolbar.ToolbarView
import ai.ftech.ekyc.domain.model.PhotoConfirmDetailInfo
import ai.ftech.ekyc.domain.model.PhotoInfo
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

    override fun onPrepareInitView() {
        super.onPrepareInitView()
    }

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
                val dialog = ConfirmDialog.Builder()
                    .setTitle(getAppString(R.string.fekyc_confirm_notification_title))
                    .setContent(getAppString(R.string.fekyc_confirm_notification_content))
                    .setLeftTitle(getAppString(R.string.fekyc_confirm_exit))
                    .setRightTitle(getAppString(R.string.fekyc_confirm_stay))
                    .build()
                dialog.listener = object : ConfirmDialog.IListener {
                    override fun onLeftClick() {
                        finish()
                    }

                    override fun onRightClick() {
                        dialog.dismissDialog()
                    }
                }
                dialog.showDialog(supportFragmentManager, dialog::class.java.simpleName)
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

    private fun createConfirmPictureGroup(list: MutableList<PhotoConfirmDetailInfo>?) {
        list?.forEach { photoConfirmDetailInfo ->
            val groupData = ConfirmPictureGroup(photoConfirmDetailInfo).apply {
                this.listener = object : ConfirmPictureGroup.IListener {
                    override fun onClickItem(item: PhotoInfo) {
                        Log.d(TAG, "onClickItem: ${item.ekycType?.name}")
                        replaceFragment(ConfirmPictureFragment())
                    }
                }
            }
            adapter.addGroupData(groupData)
        }
        adapter.notifyAllGroupChanged()
    }

    override fun getContainerId() = R.id.flconfirmPictureFrame
}
