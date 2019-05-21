package com.baobomb.nlautouitestsample

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_first.*
import kotlinx.android.synthetic.main.list_item.view.*

class FirstFragment : Fragment() {
    companion object {
        val LOG_TAG = FirstFragment::class.java.simpleName
    }

    private val adapter: MySampleRecyclerAdapter by lazy { MySampleRecyclerAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_sample.layoutManager = LinearLayoutManager(this.context)
        rv_sample.adapter = adapter
        adapter.onItemClickListener = object : MySampleRecyclerAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.d(LOG_TAG, "$position click")
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }
}

class MySampleRecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

class MySampleRecyclerAdapter : RecyclerView.Adapter<MySampleRecyclerItemViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    var onItemClickListener: OnItemClickListener? = null

    override fun getItemCount(): Int = 100

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySampleRecyclerItemViewHolder {
        return MySampleRecyclerItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: MySampleRecyclerItemViewHolder, position: Int) {
        holder.itemView.tv_title.text = "Title : $position"
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }
}