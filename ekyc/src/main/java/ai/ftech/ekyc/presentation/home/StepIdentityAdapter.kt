package ai.ftech.ekyc.presentation.home

import ai.ftech.base.extension.hide
import ai.ftech.base.extension.show
import ai.ftech.ekyc.R
import ai.ftech.ekyc.common.getAppString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StepIdentityAdapter : RecyclerView.Adapter<StepIdentityAdapter.StepVH>() {

    private var dataList: List<String>? = null

    init {
        dataList = listOf(
            getAppString(R.string.fekyc_home_step_1),
            getAppString(R.string.fekyc_home_step_2),
            getAppString(R.string.fekyc_home_step_3),
            getAppString(R.string.fekyc_home_step_4),
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fekyc_step_identity_item, parent, false)
        return StepVH(view)
    }

    override fun onBindViewHolder(holder: StepVH, position: Int) {
        val step = dataList?.get(position)
        if (step != null) {
            holder.onBind(step)
        }
    }

    override fun getItemCount(): Int = dataList?.size ?: 0

    inner class StepVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tvTitle: TextView
        private var vTop: View
        private var vBottom: View
        private val FIRST_POSITION = 0
        private val LAST_POSITION = dataList?.size?.minus(1)

        init {
            tvTitle = itemView.findViewById(R.id.tvStepIdentityItmText)
            vTop = itemView.findViewById(R.id.vStepIdentityItmTop)
            vBottom = itemView.findViewById(R.id.vStepIdentityItmBottom)
        }

        fun onBind(step: String) {
            if (adapterPosition == FIRST_POSITION) {
                vTop.hide()
            } else {
                vTop.show()
            }

            if (adapterPosition == LAST_POSITION) {
                vBottom.hide()
            } else {
                vBottom.show()
            }

            tvTitle.text = step
        }
    }
}
