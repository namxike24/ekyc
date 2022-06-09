package ai.ftech.ekyc.presentation.home

import ai.ftech.dev.base.extension.getAppString
import ai.ftech.ekyc.R
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
            getAppString(R.string.fekyc_home_step_4)
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ftech_step_identity_item, parent, false)
        return StepVH(view)
    }

    override fun onBindViewHolder(holder: StepVH, position: Int) {
        val step = dataList?.get(position)
        holder.onBind(step.toString())
    }

    override fun getItemCount(): Int = dataList?.size ?: 0

    inner class StepVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tvTitle: TextView

        init {
            tvTitle = itemView.findViewById(R.id.tvStepIdentityItmText)
        }

        fun onBind(step: String) {
            tvTitle.text = step
        }

    }
}
