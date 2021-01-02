package appjpm4everyone.plantrasberrypi.ui.main

import appjpm4everyone.data.repository.DogsRepository
import appjpm4everyone.usescases.dogs.UseCaseDogs
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class MainActivityModule {

    @Provides
    fun MainViewModelProvider(useCaseDogs: UseCaseDogs): MainViewModel{
        return MainViewModel(
            useCaseDogs
        )
    }

    @Provides
    fun dogsNamesProvider(dogsRepository: DogsRepository) =
        UseCaseDogs(dogsRepository)
}

@Subcomponent(modules = [(MainActivityModule::class)])
interface MainActivityComponent {
    val mainViewModel: MainViewModel
}