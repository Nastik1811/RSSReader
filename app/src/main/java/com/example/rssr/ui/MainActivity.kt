package com.example.rssr.ui

import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rssr.utils.ConnectionReceiver
import com.example.rssr.R
import com.example.rssr.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {


    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ArticlesAdapter
    private lateinit var receiver: ConnectionReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this,
            R.layout.activity_main)
        setSupportActionBar(binding.myToolbar)

        receiver = ConnectionReceiver()
        registerReceiver(receiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )

        val recyclerView = binding.articlesList
        adapter = ArticlesAdapter(this) { url: String -> openArticle(url) }
        recyclerView.adapter = adapter

        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.layoutManager = LinearLayoutManager(this)
        }
        else
            recyclerView.layoutManager = GridLayoutManager(this, 2)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.getArticles().observe(this, Observer {
                articles -> adapter.setArticles(articles)
                binding.progressBar.visibility = View.GONE
                binding.swipeLayout.isRefreshing = false
        })

        binding.swipeLayout.setOnRefreshListener {
            adapter.clear()
            binding.swipeLayout.isRefreshing = true
            viewModel.setArticles()
            adapter.notifyDataSetChanged()
        }
    }

    private fun openArticle(link: String){
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("URL", link)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_change_url) {
            showUrlDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showUrlDialog(){
        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_change_url, null)
        val urlEditText: EditText = view.findViewById(R.id.et_change_uri)

        alertDialog.setView(view)

        alertDialog.setPositiveButton("Ok") { dialog, _ ->
            val url = urlEditText.text.toString()
            if (URLUtil.isHttpsUrl(url)){
                viewModel.changeUrl(url)
                viewModel.setArticles()
                adapter.notifyDataSetChanged()
            }
            else {
                Toast.makeText(this, "Invalid url. Try again", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        alertDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
