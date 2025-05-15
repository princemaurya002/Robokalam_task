package com.princemaurya.robokalam.ui.portfolio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.princemaurya.robokalam.R
import com.princemaurya.robokalam.data.db.PortfolioEntity

class PortfolioAdapter(
    private val onEditClick: (PortfolioEntity) -> Unit,
    private val onDeleteClick: (PortfolioEntity) -> Unit
) : ListAdapter<PortfolioEntity, PortfolioAdapter.PortfolioViewHolder>(PortfolioDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_portfolio, parent, false)
        return PortfolioViewHolder(view)
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PortfolioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvCollege: TextView = itemView.findViewById(R.id.tvCollege)
        private val tvSkills: TextView = itemView.findViewById(R.id.tvSkills)
        private val tvProjectTitle: TextView = itemView.findViewById(R.id.tvProjectTitle)
        private val tvProjectDescription: TextView = itemView.findViewById(R.id.tvProjectDescription)
        private val btnEdit: MaterialButton = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: MaterialButton = itemView.findViewById(R.id.btnDelete)

        fun bind(portfolio: PortfolioEntity) {
            tvName.text = portfolio.name
            tvCollege.text = portfolio.college
            tvSkills.text = portfolio.skills.split(",").joinToString(", ")
            tvProjectTitle.text = portfolio.projectTitle
            tvProjectDescription.text = portfolio.projectDescription

            btnEdit.setOnClickListener { onEditClick(portfolio) }
            btnDelete.setOnClickListener { onDeleteClick(portfolio) }
        }
    }

    private class PortfolioDiffCallback : DiffUtil.ItemCallback<PortfolioEntity>() {
        override fun areItemsTheSame(oldItem: PortfolioEntity, newItem: PortfolioEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PortfolioEntity, newItem: PortfolioEntity): Boolean {
            return oldItem == newItem
        }
    }
} 