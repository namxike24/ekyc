package ai.ftech.ekyc.presentation.picture.confirm

import ai.ftech.dev.base.extension.observer
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.FEkycActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ConfirmPictureActivity : FEkycActivity(R.layout.fekyc_confirm_picture_activity) {
    private lateinit var rvPhoto: RecyclerView
    private val viewModel by viewModels<ConfirmPictureViewModel>()
    private val adapter by lazy {
        ConfirmPictureAdapter()
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
            adapter.setDataList(it)
        }
    }
}
