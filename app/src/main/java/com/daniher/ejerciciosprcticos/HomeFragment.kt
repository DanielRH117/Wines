package com.daniher.ejerciciosprcticos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.daniher.ejerciciosprcticos.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

class HomeFragment : BaseFragment(), OnClickListener {

    private lateinit var adapter: WineListAdapter
    private lateinit var service: WineService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        setUpRecyclerView()
        setUpRetrofit()
        setUpSwipeRefresh()
    }

    private fun setUpAdapter() {
        adapter = WineListAdapter()
        adapter.setOnClickListener(this)
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(3, RecyclerView.VERTICAL)
            adapter = this@HomeFragment.adapter
        }
    }

    private fun setUpSwipeRefresh() {
        binding.srlWines.setOnRefreshListener {
            adapter.submitList(listOf())
            getWines()
        }
    }

    private fun setUpRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(WineService::class.java)
    }

    override fun getWines() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val serverOk = Random.nextBoolean()
                val wines = if (serverOk) service.getRedsWines() else listOf()
                withContext(Dispatchers.Main) {
                    if (wines.isNotEmpty()) {
                        showRecyclerView(true)
                        showNoDataView(false)
                        adapter.submitList(wines)
                    } else {
                        showRecyclerView(false)
                        showNoDataView(true)
                    }
                }
            } catch (e: Exception) {
                showMsg((R.string.common_request_fail))
            } finally {
                showProgress(false)
            }
        }
    }



    override fun onResume() {
        super.onResume()
        showProgress(true)
        getWines()
    }

    /*
    * OnClickListener
    * */
    override fun onLongClick(wine: Wine) {
        val options = resources.getStringArray(R.array.array_dialog_add_options)
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_add_fav_title)
            .setItems(options){_, index ->
                when(index){
                    0-> addToFavorite(wine)
                }

            }
            .show()
    }

    private fun addToFavorite(wine: Wine) {
        lifecycleScope.launch(Dispatchers.IO) {
            wine.isFavourite = true
            val result = WineApplication.database.wineDao().addWine(wine)
            if(result != -1L){
                showMsg(R.string.room_save_success)
            }else {
                showMsg(R.string.room_save_fail)
            }
        }
    }

    override fun onFavorite(wine: Wine) {

    }
}