package com.katyrin.searchtext.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.katyrin.searchtext.data.ResultItem
import com.katyrin.searchtext.databinding.ItemResultBinding

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var results: List<ResultItem> = listOf()

    inner class SearchViewHolder(private val itemBinding: ItemResultBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(result: ResultItem) {
            itemBinding.resultText.text = result.textResult
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemResultBinding.inflate(layoutInflater, parent, false)
        return SearchViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size

    fun filterResult(newResults: List<ResultItem>) {
        results = newResults
        notifyDataSetChanged()
    }
}