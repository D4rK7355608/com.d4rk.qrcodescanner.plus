package com.d4rk.qrcodescanner.plus.ui.history
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import com.d4rk.qrcodescanner.plus.databinding.FragmentBarcodeHistoryListBinding
import com.d4rk.qrcodescanner.plus.di.barcodeDatabase
import com.d4rk.qrcodescanner.plus.extension.orZero
import com.d4rk.qrcodescanner.plus.feature.barcode.BarcodeActivity
import com.d4rk.qrcodescanner.plus.model.Barcode
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BarcodeHistoryListFragment : Fragment(), BarcodeHistoryAdapter.Listener {
    private lateinit var _binding: FragmentBarcodeHistoryListBinding
    private val binding get() = _binding
    companion object {
        private const val PAGE_SIZE = 20
        private const val TYPE_ALL = 0
        private const val TYPE_FAVORITES = 1
        private const val TYPE_KEY = "TYPE_KEY"
        fun newInstanceAll(): BarcodeHistoryListFragment {
            return BarcodeHistoryListFragment().apply {
                arguments = Bundle().apply {
                    putInt(TYPE_KEY, TYPE_ALL)
                }
            }
        }
        fun newInstanceFavorites(): BarcodeHistoryListFragment {
            return BarcodeHistoryListFragment().apply {
                arguments = Bundle().apply {
                    putInt(TYPE_KEY, TYPE_FAVORITES)
                }
            }
        }
    }
    private val disposable = CompositeDisposable()
    private val scanHistoryAdapter = BarcodeHistoryAdapter(this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBarcodeHistoryListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        loadHistory()
    }
    override fun onBarcodeClicked(barcode: Barcode) {
        BarcodeActivity.start(requireActivity(), barcode)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }
    private fun initRecyclerView() {
        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = scanHistoryAdapter
        }
    }

    private fun loadHistory() {
        val pagingSourceFactory = when (arguments?.getInt(TYPE_KEY).orZero()) {
            TYPE_ALL -> { -> barcodeDatabase.getAll() }
            TYPE_FAVORITES -> { -> barcodeDatabase.getFavorites() }
            else -> return
        }

        val pager = Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        )

        viewLifecycleOwner.lifecycleScope.launch {
            pager.flow
                .cachedIn(viewLifecycleOwner.lifecycleScope)
                .collectLatest { pagingData ->
                    scanHistoryAdapter.submitData(pagingData) // FIXME: Unresolved reference: submitData
                }
        }
    }
}