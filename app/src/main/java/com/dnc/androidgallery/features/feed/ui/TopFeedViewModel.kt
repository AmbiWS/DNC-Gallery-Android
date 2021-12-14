package com.dnc.androidgallery.features.feed.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dnc.androidgallery.base.BaseViewModel
import com.dnc.androidgallery.core.extensions.mutable
import com.dnc.androidgallery.features.feed.domain.TopFeedInteractor
import kotlinx.coroutines.withContext

class TopFeedViewModel(
    private val interactor: TopFeedInteractor
) : BaseViewModel() {

    val pagesCount: LiveData<Int> = MutableLiveData()

    fun loadPagesTotal() {
        launch(dispatcher = ioContext) {
            val pagesTotal = interactor.getTotalPages()
            withContext(mainContext) {
                pagesCount.mutable().value = pagesTotal
            }
        }
    }
}
