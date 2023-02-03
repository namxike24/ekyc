package ai.ftech.fekyc.presentation.picture.confirm


import ai.ftech.fekyc.base.extension.observer
import ai.ftech.fekyc.base.extension.setOnSafeClick
import ai.ftech.fekyc.R
import ai.ftech.fekyc.base.adapter.group.GroupAdapter
import ai.ftech.fekyc.common.FEkycActivity
import ai.ftech.fekyc.common.widget.toolbar.ToolbarView
import ai.ftech.fekyc.domain.model.ekyc.PhotoConfirmDetailInfo
import ai.ftech.fekyc.domain.model.ekyc.PhotoInfo
import ai.ftech.fekyc.presentation.info.EkycInfoActivity
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
                onBackPressed()
            }
        })

        btnContinue.setOnSafeClick {
            finish()
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
                        viewModel.setSelectedIndex(item)
                        replaceFragment(ConfirmPictureFragment())
                    }
                }
            }
            adapter.addGroupData(groupData)
        }
        adapter.notifyAllGroupChanged()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
