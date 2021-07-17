package com.matthias.dreamz.ui.screen.tags

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matthias.dreamz.data.model.TagInfo
import com.matthias.dreamz.data.model.TagType
import com.matthias.dreamz.datastore.FilterDataStoreManager
import com.matthias.dreamz.repository.TagInfoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val filterDataStoreManager: FilterDataStoreManager,
    private val tagInfoRepository: TagInfoRepository
) :
    ViewModel() {
    fun getTagsInfo(tagType: TagType) = tagInfoRepository.getTagsInfo(tagType)


    fun setFilter(tag: String, tagType: TagType) {
        viewModelScope.launch {
            when (tagType) {
                TagType.TAG -> filterDataStoreManager.setTag(tag)
                TagType.PEOPLE -> filterDataStoreManager.setPeople(tag)
            }
        }
    }
}