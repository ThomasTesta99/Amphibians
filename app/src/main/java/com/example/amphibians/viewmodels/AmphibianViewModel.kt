package com.example.amphibians.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.amphibians.AmphibianPhotoViewerApplication
import com.example.amphibians.data.AmphibianPhotoRepository
import com.example.amphibians.network.AmphibianPhoto
import kotlinx.coroutines.launch

class AmphibianViewModel(
    private val amphibianPhotoRepository: AmphibianPhotoRepository
) : ViewModel() {
    var amphibianUiState : AmphibianUIState by mutableStateOf(AmphibianUIState.Loading)
        private set

    init{
        getAmphibianPhotos()
    }

    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AmphibianPhotoViewerApplication)
                val amphibianPhotoRepository = application.container.amphibianPhotoRepository
                AmphibianViewModel(amphibianPhotoRepository = amphibianPhotoRepository)
            }
        }
    }

    private fun getAmphibianPhotos(){
        viewModelScope.launch {
            amphibianUiState = try{
                val result = amphibianPhotoRepository.getAmphibianPhotos()
                AmphibianUIState.Success(result)
            }catch (e:Exception){
                Log.d("Amphibian View Model", "getAmphibianPhotos: ${e.message}")
                AmphibianUIState.Error
            }
        }
    }
}

sealed interface AmphibianUIState{
    data class Success(val photos: List<AmphibianPhoto>) : AmphibianUIState
    object Error : AmphibianUIState
    object Loading : AmphibianUIState

}