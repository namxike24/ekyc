package ai.ftech.ekyc.presentation.picture.confirm

import ai.ftech.dev.base.adapter.group.GroupAdapter
import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.observer
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import ai.ftech.ekyc.domain.model.EKYC_PHOTO_TYPE
import ai.ftech.ekyc.domain.model.PhotoConfirmDetailInfo
import ai.ftech.ekyc.domain.model.PhotoInfo
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ConfirmPictureActivity : FEkycActivity(R.layout.fekyc_confirm_picture_activity) {
    private lateinit var rvPhoto: RecyclerView
    private val viewModel by viewModels<ConfirmPictureViewModel>()
    private val adapter = GroupAdapter()

    override fun onPrepareInitView() {
        super.onPrepareInitView()
    }

    override fun onInitView() {
        super.onInitView()
        rvPhoto = findViewById(R.id.rvConfirmPicturePhotoList)

        rvPhoto.layoutManager = LinearLayoutManager(this)
        rvPhoto.adapter = adapter
        viewModel.getConfirmPhotoList()
    }

    override fun onObserverViewModel() {
        super.onObserverViewModel()

        observer(viewModel.photoList) {
            it?.forEach {photoConfirmDetailInfo->
                val groupData = ConfirmPictureGroup(photoConfirmDetailInfo)
                adapter.addGroupData(groupData)
            }

            adapter.notifyAllGroupChanged()
        }
    }
}
