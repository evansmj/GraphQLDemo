package com.oldgoat5.graphqldemo.language

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oldgoat5.CountryLanguageQuery
import com.oldgoat5.graphqldemo.R
import com.oldgoat5.graphqldemo.common.GraphQLRecyclerAdapter
import com.oldgoat5.graphqldemo.common.GraphQLRecyclerViewHolder
import com.oldgoat5.graphqldemo.common.ViewModelActivity
import com.oldgoat5.graphqldemo.common.observers.RecyclerAdapterObserver
import com.oldgoat5.graphqldemo.common.observers.VisibilityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LanguageActivity : ViewModelActivity<LanguageViewModel>() {

    override val viewModel: LanguageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.language_activity)

        val languageRecyclerView = findViewById<RecyclerView>(R.id.language_recycler_view)
        val progressBar = findViewById<ProgressBar>(R.id.language_progress_bar)

        supportActionBar?.title = "Languages"

        val languageAdapter = LanguageAdapter()

        languageRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        languageRecyclerView.adapter = languageAdapter

        val loadingObserver = VisibilityObserver(progressBar)
        val recyclerAdapterObserver = RecyclerAdapterObserver(languageAdapter)

        lifecycleScope.launch {
            viewModel.getLanguageState().collect {
                loadingObserver.onNext(it.status == Status.LOADING)
                it.data?.countries?.let { items -> recyclerAdapterObserver.onNext(items) }
            }
        }
    }
}

class LanguageAdapter : GraphQLRecyclerAdapter<CountryLanguageQuery.Country?, LanguageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.country_item, parent, false)
        return LanguageViewHolder(view)
    }

}

class LanguageViewHolder(itemView: View) : GraphQLRecyclerViewHolder<CountryLanguageQuery.Country?>(itemView) {

    private var countryTextView: TextView = itemView.findViewById(R.id.country_text_view)

    override fun onBind(item: CountryLanguageQuery.Country?) {
        item?.let { countryTextView.text = item.name }
    }
}