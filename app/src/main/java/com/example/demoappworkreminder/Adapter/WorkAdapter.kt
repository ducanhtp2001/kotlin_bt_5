package com.example.demoappworkreminder.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.demoappworkreminder.Model.Work
import com.example.demoappworkreminder.R

class WorkAdapter(val context: Context, val layoutId: Int, val workList: ArrayList<Work>): BaseAdapter() {
    override fun getCount(): Int {
        return workList.size
    }

    override fun getItem(position: Int): Any {
        return workList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return workList.get(position).id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View
        var viewHolder: WorkViewHolder?

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(layoutId, null)
            viewHolder = WorkViewHolder(view)
            view.setTag(viewHolder)
        } else {
            view = convertView
            viewHolder = view.tag as WorkViewHolder
        }

        viewHolder.txtId.text = workList.get(position).id.toString()
        viewHolder.txtContent.text = workList.get(position).content
        viewHolder.txtDate.text = workList.get(position).date

        return view
    }

    inner class WorkViewHolder(view: View) {
        lateinit var txtId : TextView
        lateinit var txtContent : TextView
        lateinit var txtDate : TextView

        init {
            txtId = view.findViewById(R.id.txtId)
            txtContent = view.findViewById(R.id.txtContent)
            txtDate = view.findViewById(R.id.txtDate)
        }
    }
}