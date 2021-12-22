package com.oldgoat5.graphqldemo.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.oldgoat5.CountryLanguageQuery
import com.oldgoat5.graphqldemo.R
import com.oldgoat5.graphqldemo.api.countrylanguage.CountryLanguageInteractor
import com.oldgoat5.graphqldemo.common.GraphQLRecyclerAdapter
import com.oldgoat5.graphqldemo.common.GraphQLRecyclerViewHolder
import com.oldgoat5.graphqldemo.common.LazyDiffCallback
import com.oldgoat5.graphqldemo.common.ViewModelActivity
import com.oldgoat5.graphqldemo.common.observers.RecyclerAdapterObserver
import com.oldgoat5.graphqldemo.common.observers.VisibilityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LanguageActivity : ViewModelActivity<LanguageViewModel>() {

    override val viewModel: LanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.language_activity)

        val languageRecyclerView = findViewById<RecyclerView>(R.id.language_recycler_view)
        val progressBar = findViewById<ProgressBar>(R.id.language_progress_bar)
        val sortSwitch = findViewById<SwitchCompat>(R.id.sort_switch)

        actionBar?.title = "Languages"

        sortSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.setReverseSortEnabled(
                isChecked
            )
        }

        val languageAdapter = LanguageAdapter()
        languageRecyclerView.adapter = languageAdapter

        val loadingObserver = VisibilityObserver(progressBar)
        val recyclerAdapterObserver = RecyclerAdapterObserver(languageAdapter)

        lifecycleScope.launch {
            viewModel.getLanguageLoadingState().collect {
                loadingObserver.onNext(it == CountryLanguageInteractor.Status.LOADING)
            }
        }

        lifecycleScope.launch {
            viewModel.getLanguages().collect {
                it.let { items -> recyclerAdapterObserver.onNext(items) }
            }
        }
    }
}

class LanguageAdapter :
    GraphQLRecyclerAdapter<CountryLanguageQuery.Country?, LanguageViewHolder, LanguageAdapter.LanguageDiffCallback<CountryLanguageQuery.Country?>>() {

    override val diffCallback: LanguageDiffCallback<CountryLanguageQuery.Country?> = LanguageDiffCallback()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        return LanguageViewHolder(view)
    }

    class LanguageDiffCallback<T : CountryLanguageQuery.Country?> : LazyDiffCallback<T>() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition]?.code == newList[newItemPosition]?.code
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition]?.code == newList[newItemPosition]?.code
                    && oldList[oldItemPosition]?.name == newList[newItemPosition]?.name
                    && oldList[oldItemPosition]?.languages == newList[newItemPosition]?.languages
        }
    }
}

class LanguageViewHolder(itemView: View) : GraphQLRecyclerViewHolder<CountryLanguageQuery.Country?>(itemView) {

    private var countryTextView: TextView = itemView.findViewById(R.id.country_text_view)
    private var languageTextView: TextView = itemView.findViewById(R.id.language_text_view)

    override fun onBind(item: CountryLanguageQuery.Country?) {
        item?.let { countryTextView.text = item.name }
        item?.languages?.let {
            languageTextView.visibility = View.VISIBLE
            languageTextView.text = item.languages.map { it?.name }.joinToString("\n")
        }
    }
}