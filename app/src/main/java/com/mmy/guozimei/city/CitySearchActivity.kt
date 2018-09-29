package com.mmy.guozimei.city

import android.content.Context
import android.graphics.PixelFormat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import com.mmy.frame.AppComponent
import com.mmy.frame.base.mvp.IPresenter
import com.mmy.frame.base.mvp.IView
import com.mmy.frame.base.view.BaseActivity
import com.mmy.frame.data.bean.EventBean
import com.mmy.frame.data.bean.IBean
import com.mmy.guozimei.R
import kotlinx.android.synthetic.main.activity_city_search.*
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList

class CitySearchActivity : BaseActivity<IPresenter<IView>>(), AbsListView.OnScrollListener {

    private var isScroll = false
    override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (!isScroll) {
            return
        }

        if (mReady) {
            val text: String
            val name = allCity_lists?.get(firstVisibleItem)?.mName
            val pinyin = allCity_lists?.get(firstVisibleItem)?.mPinyin
            if (firstVisibleItem < 4) {
                text = name!!
            } else {
                text = PingYinUtil.converterToFirstSpell(pinyin)
                        .substring(0, 1).toUpperCase()
            }
            overlayView?.text = text
            overlayView?.visibility = View.VISIBLE
            handler?.removeCallbacks(overlayThread)
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000)
        }
    }

    override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL || scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
            isScroll = true
        }
    }

    //默认城市
    //    2002;//深圳
    //    2257;//重庆
    val DEFAULT_CITY_ID = 2002
    private var overlayView: TextView? = null // 对话框首字母textview
    private var sections: Array<String?>? = null// 存放存在的汉语拼音首字母
    private var adapter: BaseAdapter? = null
    private var resultListAdapter: ResultListAdapter? = null
    private var alphaIndexer: HashMap<String, Int>? = null// 存放存在的汉语拼音首字母和与之对应的列表位置
    private var overlayThread: OverlayThread? = null // 显示首字母对话框
    private var allCity_lists: ArrayList<City>? = null // 所有城市列表
    private var city_lists: ArrayList<City>? = null// 城市列表
    private var city_hot: ArrayList<City>? = null
    private var city_result: ArrayList<City>? = null
    private var city_history: ArrayList<City>? = null
    private var currentCity: String? = null // 用于保存定位到的城市
    private var locateProcess = 1 // 记录当前定位的状态 正在定位-定位成功-定位失败
    private var helper: CityDatabaseHelper? = null
    val SELECT_CITY = 1

    inner  class OverlayThread : Runnable {
        override fun run() {
            overlayView?.visibility = View.GONE
        }

    }

    override fun requestSuccess(any: IBean) {

    }

    override fun setupDagger(appComponent: AppComponent) {

    }

    override fun getLayoutID(): Any = R.layout.activity_city_search

    override fun initView() {
        setToolbar(getString(R.string.change_city), true)
        helper = CityDatabaseHelper(this)
        initOverlay()
    }

    override fun initData() {
        allCity_lists = ArrayList()
        city_hot = ArrayList()
        city_result = ArrayList()
        city_history = ArrayList()
        alphaIndexer = HashMap()
        overlayThread = OverlayThread()
        cityInit()
        setAdapter(allCity_lists!!, city_hot!!, city_history!!)
        locateProcess = 1
        list_view.adapter = adapter
        list_view.setOnScrollListener(this)
        resultListAdapter = ResultListAdapter(this, city_result!!)
        search_result.adapter = resultListAdapter
        search_result.setOnItemClickListener({ parent, view, position, id ->
            //  设置搜索结果,往数据库添加数据
            //                InsertCity(city_result.get(position).getName());
            //                SPUtils.put(CitySearchActivity.this, "LocationCity", city_result.get(position).getName());
            //                //点击搜索结果传值给首页定位结果
            //                EventBus.getDefault().post(SPUtils.get(CitySearchActivity.this, "LocationCity", "重庆市"));
            finish()
        })
    }

    private fun cityInit() {
        var city: City
        city = City("定位", "0") // 当前定位城市
        allCity_lists?.add(city)
        //        city = new City("最近", "1"); // 最近访问的城市
        //        allCity_lists.add(city);
        city = City("热门", "2") // 热门城市
        allCity_lists?.add(city)
        city = City("全部", "3") // 全部城市
        allCity_lists?.add(city)
        city_lists = getCityList()
        allCity_lists?.addAll(city_lists!!)
    }

    override fun initEvent() {
        sh.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                if (s.toString() == null || "" == s.toString()) {
                    letterListView.visibility = View.VISIBLE
                    list_view.visibility = View.VISIBLE
                    search_result.visibility = View.GONE
                    tv_noresult.visibility = View.GONE
                } else {
                    city_result?.clear()
                    letterListView.visibility = View.GONE
                    list_view.visibility = View.GONE
                    getResultCityList(s.toString())
                    if (city_result?.size!! <= 0) {
                        tv_noresult.visibility = View.VISIBLE
                        search_result.visibility = View.GONE
                    } else {
                        tv_noresult.visibility = View.GONE
                        search_result.visibility = View.VISIBLE
//                        resultListAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        letterListView.onTouchingLetterChangedListener = {s->
            if (alphaIndexer!![s] != null) {
                var position = alphaIndexer!![s]
                list_view.setSelection(position!!)
                overlayView?.text = s
                overlayView?.visibility = View.VISIBLE
                handler.removeCallbacks(overlayThread)
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1000)
            }
        }

        list_view.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            if (position >= 4) {
                mBus.post(EventBean(allCity_lists?.get(position)?.mName, SELECT_CITY))
                finish()
            }
        }

        search_result.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            finish()
        }
    }

    private fun setAdapter(list: List<City>, hotList: List<City>,
                           hisCity: List<City>) {
        adapter = ListAdapter(this, list, hotList, hisCity)
        list_view.adapter = adapter
    }

    private var mReady: Boolean = false
    // 初始化汉语拼音首字母弹出提示框
    private fun initOverlay() {
        mReady = true
        val inflater = LayoutInflater.from(this)
        overlayView = inflater.inflate(R.layout.overlay, null) as TextView
        overlayView?.visibility = View.INVISIBLE
        val lp = WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT)
        val windowManager = this
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(overlayView, lp)
    }


    private inner class ResultListAdapter(context: Context, results: ArrayList<City>) : BaseAdapter() {
        private val inflater: LayoutInflater
        private var results = ArrayList<City>()

        init {
            inflater = LayoutInflater.from(context)
            this.results = results
        }

        override fun getCount(): Int {
            return results.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val viewHolder: ViewHolder
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.citysearchlist_item, null)
                viewHolder = ViewHolder()
                viewHolder.name = convertView!!
                        .findViewById(R.id.name)
                convertView.tag = viewHolder
            } else {
                viewHolder = convertView.tag as ViewHolder
            }

            viewHolder.name!!.text = results[position].mName

            return convertView
        }

        internal inner class ViewHolder {
            var name: TextView? = null
        }
    }

    inner class ListAdapter(private val context: Context, private val list: List<City>,
                            private val hotList: List<City>, private val hisCity: List<City>) : BaseAdapter() {
        internal val inflater: LayoutInflater
        internal val VIEW_TYPE = 5

        private var holder: ViewHolder? = null

        init {

            this.inflater = LayoutInflater.from(context)
            alphaIndexer = HashMap()
            sections = arrayOfNulls<String>(list.size)
            for (i in list.indices) {
                // 当前汉语拼音首字母
                val currentStr = getAlpha(list[i].mPinyin)
                // 上一个汉语拼音首字母，如果不存在为" "
                val previewStr = if (i - 1 >= 0)
                    getAlpha(list[i - 1]
                            .mPinyin)
                else
                    " "
                if (previewStr != currentStr) {
                    val name = getAlpha(list[i].mPinyin)
                    alphaIndexer?.put(name, i)
                    sections!![i] = name
                }
            }
        }

        override fun getViewTypeCount(): Int {
            return VIEW_TYPE
        }

        override fun getItemViewType(position: Int): Int {
            return if (position < 3) position else 3
        }

        override fun getCount(): Int {
            return list.size
        }

        override fun getItem(position: Int): Any {
            return list[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            val city: TextView
            val viewType = getItemViewType(position)
            if (viewType == 0) { // 定位
                convertView = inflater.inflate(R.layout.frist_list_item, null)
                val locateHint = convertView!!
                        .findViewById<TextView>(R.id.locateHint)
                city = convertView.findViewById<TextView>(R.id.lng_city)
                city.setOnClickListener { v ->
                    if (locateProcess == 2) {
                        //搜索结果
                        InsertCity(city.text.toString() + "")
                        finish()
                    } else if (locateProcess == 3) {
                        locateProcess = 1
                        list_view.adapter = adapter
                        adapter?.notifyDataSetChanged()
                        currentCity = ""
                    }
                }
                val pbLocate = convertView
                        .findViewById<ProgressBar>(R.id.pbLocate)
                if (locateProcess == 1) { // 正在定位
                    locateHint.text = "正在定位"
                    city.visibility = View.GONE
                    pbLocate.visibility = View.VISIBLE
                } else if (locateProcess == 2) { // 定位成功
                    if (currentCity == null) {
                        locateHint.text = "未定位到城市,请重新定位"
                        locateHint.gravity = Gravity.CENTER_VERTICAL
                        locateHint.isClickable = false
                        pbLocate.visibility = View.GONE
                        city.visibility = View.GONE
                    } else {
                        locateHint.text = "当前定位城市"
                        city.visibility = View.VISIBLE
                        city.text = currentCity
                        pbLocate.visibility = View.GONE
                    }

                } else if (locateProcess == 3) {
                    locateHint.text = "未定位到城市,请选择"
                    city.visibility = View.VISIBLE
                    city.text = "重新选择"
                    pbLocate.visibility = View.GONE
                }
            }
            else if (viewType == 1) {
                convertView = inflater.inflate(R.layout.recent_city, null)
                val hotCity = convertView!!
                        .findViewById<GridView>(R.id.recent_city)
                hotCity.setOnItemClickListener { parent1, view, position1, id ->
                    //点击热门城市
                    InsertCity(city_hot?.get(position1)?.mName!!)
                    finish()
                }
                hotCity.adapter = HotCityAdapter(context, this.hotList)
                val hotHint = convertView
                        .findViewById<TextView>(R.id.recentHint)
                hotHint.text = "热门城市"
            } else if (viewType == 2) {
                convertView = inflater.inflate(R.layout.total_item, null)
            } else {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.citysearchlist_item, null)
                    holder = ViewHolder()
                    holder?.alpha = convertView!!
                            .findViewById(R.id.alpha)
                    holder?.name = convertView
                            .findViewById(R.id.name)
                    convertView.tag = holder
                } else {
                    holder = convertView.tag as ViewHolder
                }
                if (position >= 1) {
                    holder?.name!!.text = list[position].mName
                    val currentStr = getAlpha(list[position].mPinyin)
                    val previewStr = if (position - 1 >= 0)
                        getAlpha(list[position - 1].mPinyin)
                    else
                        " "
                    if (previewStr != currentStr) {
                        holder?.alpha!!.visibility = View.VISIBLE
                        holder?.alpha!!.text = currentStr
                    } else {
                        holder?.alpha!!.visibility = View.GONE
                    }
                }
            }
            return convertView!!
        }

        private inner class ViewHolder {
            internal var alpha: TextView? = null // 首字母标题
            internal var name: TextView? = null // 城市名字
        }
    }

    internal inner class HotCityAdapter(private val context: Context, private val hotCitys: List<City>) : BaseAdapter() {
        private val inflater: LayoutInflater = LayoutInflater.from(this.context)

        override fun getCount(): Int {
            return hotCitys.size
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            var convertView = convertView
            convertView = inflater.inflate(R.layout.item_city, null)
            val city = convertView.findViewById<TextView>(R.id.city)
            city.text = hotCitys[position].mName
            return convertView
        }
    }

    /**
     * 热门城市
     */
    fun hotCityInit() {
        val hotCitys = arrayOf("深圳市")
        for (s in hotCitys) {
            city_hot?.add(City(s, "2", findCityID(s)))
        }
    }

    fun InsertCity(name: String) {
        val db = helper?.getReadableDatabase()
        val cursor = db?.rawQuery("select * from recentcity where name = '"
                + name + "'", null)
        if (cursor?.count!! > 0) { //
            db.delete("recentcity", "name = ?", arrayOf(name))
        }
        db.execSQL("insert into recentcity(name, date) values('" + name + "', "
                + System.currentTimeMillis() + ")")
        db.close()
    }

    //查找城市ID,未找到默认返回重庆市
    fun findCityID(cityName: String): Int {
        val dbHelper = DBHelper(mFrameApp)
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("select cityID from city where name like \"%$cityName%\"", null)
        //取第一条
        var cityID = DEFAULT_CITY_ID
        if (cursor.moveToNext())
            cityID = cursor.getInt(0)
        cursor.close()
        db.close()
        return if (cityID == -1) DEFAULT_CITY_ID else cityID
    }

    private fun hisCityInit() {
        val db = helper?.getReadableDatabase()
        val cursor = db?.rawQuery(
                "select * from recentcity order by date desc limit 0, 3", null)
        while (cursor!!.moveToNext()) {
            city_history?.add(City(cursor.getString(1), "1", findCityID(cursor.getString(1))))
        }
        cursor.close()
        db.close()
    }

    @SuppressWarnings("unchecked")
    private fun getCityList(): ArrayList<City> {
        val dbHelper = DBHelper(this)
        var list = ArrayList<City>()
        //            dbHelper.createDataBase();
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery("select * from city", null)
        var city: City
        while (cursor.moveToNext()) {
            city = City(cursor.getString(1), cursor.getString(2))
            city.mCityId = Integer.parseInt(cursor.getString(3))
            list.add(city)
        }
        cursor.close()
        db.close()
        Collections.sort(list, comparator)
        return list
    }

    private fun getResultCityList(keyword: String) {
        val dbHelper = DBHelper(this)
        //            dbHelper.createDataBase();
        val db = dbHelper.getWritableDatabase()
        val cursor = db.rawQuery(
                "select * from city where name like \"%" + keyword
                        + "%\" or pinyin like \"%" + keyword + "%\"", null)
        var city: City
        Log.e("info", "length = " + cursor.getCount())
        while (cursor.moveToNext()) {
            city = City(cursor.getString(1), cursor.getString(2))
            city_result?.add(city)
        }
        cursor.close()
        db.close()
        Collections.sort(city_result, comparator)
    }

    /**
     * a-z排序
     */
    private var comparator =  Comparator<City>{ lhs, rhs ->
        val a = lhs.mPinyin?.substring(0, 1)
        val b = rhs.mPinyin?.substring(0, 1)
        val flag = a?.compareTo(b!!)
        return@Comparator if (flag == 0) {
            a.compareTo(b!!)
        } else {
            flag!!
        }
    }

    // 获得汉语拼音首字母
    private fun getAlpha(str: String?): String {
        if (str == null) {
            return "#"
        }
        if (str.trim { it <= ' ' }.length == 0) {
            return "#"
        }
        val c = str.trim { it <= ' ' }.substring(0, 1)[0]
        // 正则表达式，判断首字母是否是英文字母
        val pattern = Pattern.compile("^[A-Za-z]+$")
        return if (pattern.matcher(c + "").matches()) {
            (c + "").toUpperCase()
        } else if (str == "0") {
            "定位"
        }/* else if (str.equals("1")) {
    return "最近";
}*/
        else if (str == "1") {
            "热门"
        } else if (str == "2") {
            "全部"
        } else {
            "#"
        }
    }
}
