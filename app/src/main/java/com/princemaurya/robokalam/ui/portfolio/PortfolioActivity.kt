package com.princemaurya.robokalam.ui.portfolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.princemaurya.robokalam.R
import com.princemaurya.robokalam.data.db.AppDatabase
import com.princemaurya.robokalam.data.db.PortfolioEntity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PortfolioActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: PortfolioAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: ExtendedFloatingActionButton
    private lateinit var formContainer: View
    private lateinit var listContainer: View
    private lateinit var skillsContainer: LinearLayout
    private lateinit var btnAddSkill: MaterialButton

    // Form views
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCollege: TextInputLayout
    private lateinit var tilProjectTitle: TextInputLayout
    private lateinit var tilProjectDescription: TextInputLayout
    private lateinit var btnSave: MaterialButton

    private var currentPortfolio: PortfolioEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_portfolio)

        db = AppDatabase.getDatabase(this)

        // Set up toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        initViews()
        setupRecyclerView()
        setupClickListeners()
        observePortfolios()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        fabAdd = findViewById<ExtendedFloatingActionButton>(R.id.fabAdd)
        formContainer = findViewById(R.id.formContainer)
        listContainer = findViewById(R.id.listContainer)
        skillsContainer = findViewById(R.id.skillsContainer)
        btnAddSkill = findViewById(R.id.btnAddSkill)

        tilName = findViewById(R.id.tilName)
        tilCollege = findViewById(R.id.tilCollege)
        tilProjectTitle = findViewById(R.id.tilProjectTitle)
        tilProjectDescription = findViewById(R.id.tilProjectDescription)
        btnSave = findViewById(R.id.btnSave)
    }

    private fun setupRecyclerView() {
        adapter = PortfolioAdapter(
            onEditClick = { portfolio -> showEditForm(portfolio) },
            onDeleteClick = { portfolio -> showDeleteConfirmation(portfolio) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupClickListeners() {
        fabAdd.setOnClickListener {
            showAddForm()
        }

        btnSave.setOnClickListener {
            if (validateInputs()) {
                savePortfolio()
            }
        }

        btnAddSkill.setOnClickListener {
            addSkillField()
        }
    }

    private fun observePortfolios() {
        lifecycleScope.launch {
            db.portfolioDao().getAllPortfolios().collectLatest { portfolios ->
                adapter.submitList(portfolios)
                updateEmptyState(portfolios.isEmpty())
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        findViewById<View>(R.id.emptyState).visibility = if (isEmpty) View.VISIBLE else View.GONE
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun showAddForm() {
        currentPortfolio = null
        clearForm()
        showForm(true)
        supportActionBar?.title = getString(R.string.add_portfolio)
    }

    private fun showEditForm(portfolio: PortfolioEntity) {
        currentPortfolio = portfolio
        fillForm(portfolio)
        showForm(true)
        supportActionBar?.title = getString(R.string.edit)
    }

    private fun showForm(show: Boolean) {
        formContainer.visibility = if (show) View.VISIBLE else View.GONE
        listContainer.visibility = if (show) View.GONE else View.VISIBLE
        fabAdd.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun fillForm(portfolio: PortfolioEntity) {
        tilName.editText?.setText(portfolio.name)
        tilCollege.editText?.setText(portfolio.college)
        setSkills(portfolio.skills.split(","))
        tilProjectTitle.editText?.setText(portfolio.projectTitle)
        tilProjectDescription.editText?.setText(portfolio.projectDescription)
    }

    private fun clearForm() {
        tilName.editText?.text?.clear()
        tilCollege.editText?.text?.clear()
        skillsContainer.removeAllViews()
        repeat(3) { addSkillField() }
        tilProjectTitle.editText?.text?.clear()
        tilProjectDescription.editText?.text?.clear()
    }

    private fun addSkillField() {
        val skillView = LayoutInflater.from(this).inflate(R.layout.item_skill_input, skillsContainer, false)
        val btnRemove = skillView.findViewById<MaterialButton>(R.id.btnRemoveSkill)

        btnRemove.setOnClickListener {
            if (skillsContainer.childCount > 3) {
                skillsContainer.removeView(skillView)
            } else {
                Toast.makeText(this, R.string.min_skills_required, Toast.LENGTH_SHORT).show()
            }
        }

        skillsContainer.addView(skillView)
    }

    private fun getSkills(): List<String> {
        val skills = mutableListOf<String>()
        for (i in 0 until skillsContainer.childCount) {
            val skillView = skillsContainer.getChildAt(i)
            val tilSkill = skillView.findViewById<TextInputLayout>(R.id.tilSkill)
            val skill = tilSkill.editText?.text.toString().trim()
            if (skill.isNotEmpty()) {
                skills.add(skill)
            }
        }
        return skills
    }

    private fun setSkills(skills: List<String>) {
        skillsContainer.removeAllViews()
        skills.forEach { skill ->
            addSkillField()
            val lastIndex = skillsContainer.childCount - 1
            val skillView = skillsContainer.getChildAt(lastIndex)
            val tilSkill = skillView.findViewById<TextInputLayout>(R.id.tilSkill)
            tilSkill.editText?.setText(skill)
        }
        // Ensure minimum 3 skills fields
        while (skillsContainer.childCount < 3) {
            addSkillField()
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        val emptyFieldError = getString(R.string.error_empty_fields)

        if (tilName.editText?.text.toString().trim().isEmpty()) {
            tilName.error = emptyFieldError
            isValid = false
        }
        if (tilCollege.editText?.text.toString().trim().isEmpty()) {
            tilCollege.error = emptyFieldError
            isValid = false
        }
        
        // Validate skills
        val skills = getSkills()
        if (skills.size < 3) {
            Toast.makeText(this, R.string.min_skills_required, Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (tilProjectTitle.editText?.text.toString().trim().isEmpty()) {
            tilProjectTitle.error = emptyFieldError
            isValid = false
        }
        if (tilProjectDescription.editText?.text.toString().trim().isEmpty()) {
            tilProjectDescription.error = emptyFieldError
            isValid = false
        }

        return isValid
    }

    private fun savePortfolio() {
        val portfolio = PortfolioEntity(
            id = currentPortfolio?.id ?: 0,
            name = tilName.editText?.text.toString().trim(),
            college = tilCollege.editText?.text.toString().trim(),
            skills = getSkills().joinToString(","),
            projectTitle = tilProjectTitle.editText?.text.toString().trim(),
            projectDescription = tilProjectDescription.editText?.text.toString().trim()
        )

        lifecycleScope.launch {
            if (currentPortfolio == null) {
                db.portfolioDao().insertPortfolio(portfolio)
            } else {
                db.portfolioDao().updatePortfolio(portfolio)
            }
            showForm(false)
            supportActionBar?.title = getString(R.string.my_portfolios)
            Toast.makeText(this@PortfolioActivity, R.string.portfolio_saved, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteConfirmation(portfolio: PortfolioEntity) {
        AlertDialog.Builder(this)
            .setMessage(R.string.delete_confirmation)
            .setPositiveButton(R.string.yes) { _, _ ->
                lifecycleScope.launch {
                    db.portfolioDao().deletePortfolio(portfolio)
                    Toast.makeText(this@PortfolioActivity, R.string.portfolio_deleted, Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        if (formContainer.visibility == View.VISIBLE) {
            showForm(false)
            supportActionBar?.title = getString(R.string.my_portfolios)
            return true
        }
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (formContainer.visibility == View.VISIBLE) {
            showForm(false)
            supportActionBar?.title = getString(R.string.my_portfolios)
        } else {
            super.onBackPressed()
        }
    }
} 