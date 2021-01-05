package appjpm4everyone.plantrasberrypi.ui.main

import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Color
import android.os.Bundle
import android.provider.BaseColumns
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet


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
    private var yValues: MutableList<Entry> = mutableListOf()
    private var pieValues: ArrayList<PieEntry> = ArrayList()
    private var barValues: ArrayList<BarEntry> = ArrayList()

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

        val leftAxis = binding.chart.axisLeft
        leftAxis.addLimitLine(upperLimit)
        leftAxis.axisMaximum = 100f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawLimitLinesBehindData(true)

        val rightAxis = binding.chart.axisRight
        rightAxis.addLimitLine(upperLimit)
        rightAxis.axisMaximum = 100f
        rightAxis.axisMinimum = 0f
        rightAxis.setDrawLimitLinesBehindData(true)

        val xAxis = binding.chart.xAxis
        xAxis.axisMinimum = 0f
        xAxis.axisMaximum = 20f
        xAxis.setDrawLimitLinesBehindData(true)

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
                    addEntryDistance()
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

        val set1: LineDataSet = LineDataSet(yValues, "Data set 1")
        set1.fillAlpha = 110
        set1.color = Color.RED
        set1.lineWidth = 3f
        set1.valueTextSize = 10f
        set1.valueTextColor = R.color.purple_toolbar

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1)

        val data: LineData = LineData(dataSets)
        binding.chart.data = data
        binding.chart.invalidate()

        if(count>19){
            //Clear data
            val auxValues : ArrayList<Entry> = ArrayList()
            yValues.reverse()
            for (i in 0..4) {
                auxValues.add(Entry(i.toFloat(), yValues[i].y))
            }
            count=5
            yValues.clear()
            for(i in auxValues.indices){
                yValues.add(auxValues[i])
            }
            yValues = auxValues
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


        val pieDataSet : PieDataSet = PieDataSet(pieValues, "")
        pieDataSet.sliceSpace = 1f
        pieDataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        if(random<=400f){
            colors.add(Color.BLUE)
        }else{
            colors.add(Color.GREEN)
        }
        colors.add(Color.LTGRAY)
        pieDataSet.colors = colors

        //Add Data
        val pieData: PieData = PieData(pieDataSet)
        //0f equals to hide :)
        pieData.setValueTextSize(0f)
        pieData.setValueTextColor(Color.WHITE)

        binding.chartPie.data = pieData
        binding.chartPie.invalidate()
    }

    private fun addEntryDistance(){
        binding.chartBar.isDragXEnabled = true
        binding.chartBar.setScaleEnabled(false)
        binding.chartBar.description.text = "Distance cm Data"
        binding.chart.setBackgroundColor(Color.WHITE)

        val labels: ArrayList<String> = ArrayList()
        labels.add("cm")
        val xAxis = binding.chartBar.xAxis
        xAxis.textColor = R.color.purple_toolbar
        xAxis.textSize = 15f
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)

        val axisLeft: YAxis = binding.chartBar.axisLeft
        axisLeft.granularity = 2f
        axisLeft.axisMinimum = 0f
        axisLeft.axisMaximum= 400f

        val axisRight: YAxis = binding.chartBar.axisRight
        axisRight.granularity = 2f
        axisRight.axisMinimum = 0f
        axisRight.axisMaximum= 400f

        //Simulate de distance sensor
        val random = (0..400).random().toFloat()
        barValues = ArrayList()
        barValues.add(BarEntry(5f, random))


        val barDataSet: BarDataSet = BarDataSet(barValues, "Distance cm")
        // add a lot of colors
        val colors = ArrayList<Int>()
        if(random<=120){
            colors.add(Color.RED)
        }else{
            colors.add(R.color.purple_toolbar)
        }
        //for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        colors.add(R.color.purple_toolbar)
        barDataSet.colors = colors
        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 15f

        val barData: BarData = BarData()
        barData.barWidth = 10f
        barData.addDataSet(barDataSet)
        binding.chartBar.data = barData
        binding.chartBar.invalidate()

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