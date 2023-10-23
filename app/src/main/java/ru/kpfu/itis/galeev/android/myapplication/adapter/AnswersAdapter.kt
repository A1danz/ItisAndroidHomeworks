package ru.kpfu.itis.galeev.android.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.galeev.android.myapplication.databinding.QuestionItemBinding
import ru.kpfu.itis.galeev.android.myapplication.fragments.QuestionFragment
import ru.kpfu.itis.galeev.android.myapplication.model.AnswerData
import ru.kpfu.itis.galeev.android.myapplication.utils.RecyclerViewViewPagerAdapter

class AnswersAdapter(
    val answers : List<AnswerData>
) : RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder>() {

    val items : List<AnswerData> = answers

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        return AnswerViewHolder(
            viewBinding = QuestionItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        holder.bindItem(items[position])
    }


    inner class AnswerViewHolder(private val viewBinding : QuestionItemBinding)
        : RecyclerView.ViewHolder(viewBinding.root) {

            fun bindItem(data : AnswerData) {
                with(viewBinding) {
                    answerOption.text = data.answer
                    answerOption.isEnabled = !data.isChosen
                    answerOption.isChecked = data.isChosen
                }
            }
        init {
            with(viewBinding) {
                answerOption.setOnClickListener {
                    with(RecyclerViewViewPagerAdapter) {
                        chosenAnswerInViewPagerFragments[viewPagerPosition] = adapterPosition
                        println(" DEBUG TAG 23.10 ${chosenAnswerInViewPagerFragments} adapter $viewPagerPosition")
                    }

                    items[adapterPosition].isChosen = true
                    for(index in 0 until items.size) {
                        if (index != adapterPosition) {
                            items[index].isChosen = false
                        }
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

}