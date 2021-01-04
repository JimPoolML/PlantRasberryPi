package appjpm4everyone.plantrasberrypi.ui.main

import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Color
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import appjpm4everyone.plantrasberrypi.R
import appjpm4everyone.plantrasberrypi.databinding.ActivityMainBinding
import appjpm4everyone.plantrasberrypi.ui.base.BaseActivity
import appjpm4everyone.plantrasberrypi.utils.app
import appjpm4everyone.plantrasberrypi.utils.custom.CustomProgressBar
import appjpm4everyone.plantrasberrypi.utils.getViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var component: MainActivityComponent
    private val viewModel by lazy { getViewModel { component.mainViewModel } }
    private val progressBar = CustomProgressBar()

    //To RecyclerView
    private lateinit var dogsAdapter: DogsAdapter

    private lateinit var list: List<String>
    private lateinit var mAdapter: SimpleCursorAdapter

    //Chart
    private val TAG = "MainActivity"
    private lateinit var mChar: LineChart
    private lateinit var thread: Thread
    private var plotData: Boolean = true
    private var set : ILineDataSet? = null
    private var count : Int = 0
    private var yValues: ArrayList<Entry> = ArrayList()
    private var pieValues: ArrayList<PieEntry> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //dagger injection
        component = app.component.plus(MainActivityModule())
        initUI()
        viewModel.modelChooseBusiness.observe(this, Observer(::updateUi))
    }

    private fun initUI() {
        binding.searchBreed.clearFocus()
        initChar()



        list = resources.getStringArray(R.array.dog_list).toList()

        val from = arrayOf("dogsFound")
        val to = intArrayOf(android.R.id.text1)
        mAdapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1,
            null,
            from,
            to,
            android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )



        binding.searchBreed.suggestionsAdapter = mAdapter


        binding.searchBreed.setOnSuggestionListener(object :
            androidx.appcompat.widget.SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return true
            }

            override fun onSuggestionClick(position: Int): Boolean {
                val cursor = mAdapter.getItem(position) as Cursor
                val txt = cursor.getString(cursor.getColumnIndex("dogsFound"))
                binding.searchBreed.setQuery(txt, false)
                viewModel.searchDogByName(txt.toLowerCase())
                hideKeyboardFrom(this@MainActivity)
                return true
            }
        })

        binding.searchBreed.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                populateAdapter(newText);
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                if (list.contains(query)) {
                    viewModel.searchDogByName(query.toLowerCase())
                } else {
                    showDogsResponseError(getString(R.string.no_found))
                }
                hideKeyboardFrom(this@MainActivity)
                return false
            }

        })
    }

    private fun initChar() {

        binding.chart.isDragXEnabled = true
        binding.chart.setScaleEnabled(false)
        binding.chart.description.text = "Real time temperature Data"
        binding.chart.setBackgroundColor(Color.WHITE)


        var upperLimit : LimitLine = LimitLine(60f, "Danger")
        upperLimit.lineWidth = 4f
        upperLimit.enableDashedLine(10f,10f, 0f)
        upperLimit.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
        upperLimit.textSize = 15f

        var leftAxis = binding.chart.axisLeft
        leftAxis.addLimitLine(upperLimit)
        leftAxis.axisMaximum = 100f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawLimitLinesBehindData(true)

        var rightAxis = binding.chart.axisRight
        rightAxis.addLimitLine(upperLimit)
        rightAxis.axisMaximum = 100f
        rightAxis.axisMinimum = 0f
        rightAxis.setDrawLimitLinesBehindData(true)
        startPlot()
    }

    private fun startPlot() {
        Thread (Runnable {
            while (true) {
                plotData = true
                try {
                    Thread.sleep(1000)
                    addEntryTemp()
                    addEntryHum()
                    Log.e("Main", "test")
                }catch (e: InterruptedException){
                    e.printStackTrace()
                }
            }
        }).start()
    }

    private fun addEntryTemp() {

        //Simulate de temperature sensor
        val random = (0..100).random()
        yValues.add(Entry(count.toFloat(), random.toFloat()))

        var set1: LineDataSet = LineDataSet(yValues, "Data set 1")
        set1.fillAlpha = 110
        set1.color = Color.RED
        set1.lineWidth = 3f
        set1.valueTextSize = 10f
        set1.valueTextColor = R.color.purple_toolbar

        var dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1)

        var data: LineData = LineData(dataSets)
        binding.chart.data = data
        binding.chart.invalidate()

        if(count>19){
            //Clear data
            /*val auxValues : ArrayList<Entry> = ArrayList()
            yValues.reverse()
            for (i in 0..4) {
                auxValues.add(Entry(i.toFloat(), yValues[i].y))
            }*/
            count=0
            yValues = ArrayList()
            //yValues = auxValues
        }else{
            count += 1
        }

    }

    private fun addEntryHum(){
        binding.chartPie.setUsePercentValues(false)
        binding.chartPie.description.text = "Relative Humidity"
        binding.chartPie.setExtraOffsets(5f, 10f, 5f, 5f)

        binding.chartPie.dragDecelerationFrictionCoef = 0.99f //When the pie chart rotation is enabled
        binding.chartPie.isDrawHoleEnabled = true
        binding.chartPie.isRotationEnabled = false

        binding.chartPie.setHoleColor(Color.WHITE)
        binding.chartPie.transparentCircleRadius = 60f
        binding.chartPie
        binding.chartPie.setDrawCenterText(true)
        binding.chartPie.setDrawSliceText(false)

        //Simulate de RH sensor
        var random = (0..1000).random().toFloat()
        pieValues = ArrayList()
        pieValues.add(PieEntry((random/10), "RH"))
        binding.chartPie.centerText = (random/10).toString()+"%"
        binding.chartPie.setCenterTextSize(20f)
        binding.chartPie.setCenterTextColor(Color.BLACK)
        random = 1000 - random
        pieValues.add(PieEntry((random/10), ""))

        val description =  Description()
        description.text = "Relative humidity"
        description.textSize = 15f
        binding.chartPie.description = description


        var pieDataSet : PieDataSet = PieDataSet(pieValues, "")
        pieDataSet.sliceSpace = 1f
        pieDataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        colors.add(Color.BLUE)
        colors.add(Color.LTGRAY)
        pieDataSet.colors = colors

        //Add Data
        var pieData: PieData = PieData(pieDataSet)
        //0f equals to hide :)
        pieData.setValueTextSize(0f)
        pieData.setValueTextColor(Color.WHITE)

        binding.chartPie.data = pieData
        binding.chartPie.invalidate()

    }



    private fun createSet(): ILineDataSet? {
        var set: LineDataSet = LineDataSet(null, "Data set 1")
        set.axisDependency = YAxis.AxisDependency.LEFT
        set.lineWidth = 3F
        set.color = Color.RED
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.cubicIntensity = 0.2f
        return set
    }

    private fun getDataSet(): IBarDataSet? {
        val dataSets = ArrayList<Any>()
        val valueSet1 = ArrayList<Any>()
        val v1e1 = BarEntry(110.000f, 0.0f) // Jan
        valueSet1.add(v1e1)
        val v1e2 = BarEntry(40.000f, 1.0f) // Feb
        valueSet1.add(v1e2)
        val v1e3 = BarEntry(60.000f, 2.0f) // Mar
        valueSet1.add(v1e3)
        val v1e4 = BarEntry(30.000f, 3.0f) // Apr
        valueSet1.add(v1e4)
        val v1e5 = BarEntry(90.000f, 4.0f) // May
        valueSet1.add(v1e5)
        val v1e6 = BarEntry(100.000f, 5.0f) // Jun
        valueSet1.add(v1e6)
        val valueSet2 = ArrayList<Any>()
        val v2e1 = BarEntry(150.000f, 0.0f) // Jan
        valueSet2.add(v2e1)
        val v2e2 = BarEntry(90.000f, 1.0f) // Feb
        valueSet2.add(v2e2)
        val v2e3 = BarEntry(120.000f, 2.0f) // Mar
        valueSet2.add(v2e3)
        val v2e4 = BarEntry(60.000f, 3.0f) // Apr
        valueSet2.add(v2e4)
        val v2e5 = BarEntry(20.000f, 4.0f) // May
        valueSet2.add(v2e5)
        val v2e6 = BarEntry(80.000f, 5.0f) // Jun
        valueSet2.add(v2e6)
        val barDataSet1 = BarDataSet(valueSet1 as List<BarEntry>, "Brand 1")
        barDataSet1.color = Color.rgb(0, 155, 0)
        val barDataSet2 = BarDataSet(valueSet2 as List<BarEntry>, "Brand 2")
        barDataSet2.setColors(*ColorTemplate.COLORFUL_COLORS)
        //dataSets = ArrayList()
        dataSets.add(barDataSet1)
        dataSets.add(barDataSet2)
        return dataSets as IBarDataSet?
    }

    // You must implements your logic to get data using OrmLite
    private fun populateAdapter(query: String) {
        val c = MatrixCursor(arrayOf(BaseColumns._ID, "dogsFound"))
        for (i in list.indices) {
            if (list[i].toLowerCase()
                    .startsWith(query.toLowerCase())
            ) c.addRow(arrayOf<Any>(i, list[i]))
        }
        mAdapter.changeCursor(c)
    }

    private fun updateUi(uiModel: MainViewModel.UiModel) {
        if (uiModel is MainViewModel.UiModel.Loading) progressBar.show(this) else progressBar.hideProgress()
        when (uiModel) {
            is MainViewModel.UiModel.ShowDogsError -> showDogsResponseError(uiModel.errorStatus)
            is MainViewModel.UiModel.ShowEmptyList -> showDogsResponseError(getString(R.string.empty_list))
            is MainViewModel.UiModel.ShowDogList -> showDogsSuccess(uiModel.dogsList)
        }
        hideKeyboardFrom(this)
    }

    //To easily test
    private fun getArrayString(): Array<String?>? {
        return applicationContext.resources.getStringArray(R.array.dog_list)
    }

    private fun showDogsSuccess(dogsList: List<String>) {
        dogsAdapter = DogsAdapter(dogsList)
        binding.rvDogs.setHasFixedSize(true)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = dogsAdapter
        val dividerItemDecoration = DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL)
        binding.rvDogs.addItemDecoration(dividerItemDecoration)
        hideKeyboardFrom(this)
    }

    private fun showDogsResponseError(errorStatus: String) {
        hideKeyboardFrom(this)
        showShortSnackError(this, errorStatus)
    }
}