package ai.ftech.ekyc.presentation.home

import ai.ftech.dev.base.extension.getAppString
import ai.ftech.dev.base.extension.hide
import ai.ftech.dev.base.extension.show
import ai.ftech.ekyc.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StepIdentityAdapter : RecyclerView.Adapter<StepIdentityAdapter.StepVH>() {

    private var dataList: List<StepDisplay>? = null

    init {
        dataList = listOf(
            StepDisplay(getAppString(R.string.fekyc_home_step_1), isFirstItem = true),
            StepDisplay(getAppString(R.string.fekyc_home_step_2)),
            StepDisplay(getAppString(R.string.fekyc_home_step_3)),
            StepDisplay(getAppString(R.string.fekyc_home_step_4), isLastItem = true),
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ftech_step_identity_item, parent, false)
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

        init {
            tvTitle = itemView.findViewById(R.id.tvStepIdentityItmText)
            vTop = itemView.findViewById(R.id.vStepIdentityItmTop)
            vBottom = itemView.findViewById(R.id.vStepIdentityItmBottom)
        }

        fun onBind(step: StepDisplay) {
            if (step.isFirstItem) {
                vTop.hide()
            } else {
                vTop.show()
            }
            if (step.isLastItem) {
                vBottom.hide()
            } else {
                vBottom.show()
            }
            tvTitle.text = step.content
        }
    }

    class StepDisplay(
        val content: String,
        val isFirstItem: Boolean = false,
        val isLastItem: Boolean = false
    )
}
