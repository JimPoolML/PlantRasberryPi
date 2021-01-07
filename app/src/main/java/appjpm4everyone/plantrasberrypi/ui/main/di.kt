package appjpm4everyone.plantrasberrypi.ui.main

import appjpm4everyone.data.repository.DogsRepository
import appjpm4everyone.data.repository.RasberryPiRepository
import appjpm4everyone.usescases.dogs.UseCaseDogs
import appjpm4everyone.usescases.rasberrypi.UseCaseRasberryPi
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class MainActivityModule {

    @Provides
    fun MainViewModelProvider(useCaseDogs: UseCaseDogs, useCaseRasberryPi: UseCaseRasberryPi): MainViewModel{
        return MainViewModel(
            useCaseDogs, useCaseRasberryPi
        )
    }

    @Provides
    fun dogsNamesProvider(dogsRepository: DogsRepository) =
        UseCaseDogs(dogsRepository)

    @Provides
    fun rasberryPiNamesProvider(rasberryPiRepository: RasberryPiRepository) =
        UseCaseRasberryPi(rasberryPiRepository)
}

@Subcomponent(modules = [(MainActivityModule::class)])
interface MainActivityComponent {
    val mainViewModel: MainViewModel
}