## CalendarView 日历控件
### Calendar View 使用kotlin语言开发，支持单选，多选，按星期选，跨月份日期范围选择，样式设置
### 设置不可选择日期，设置只可选择某些日期

## 运行环境
> AS 版本： Android Studio Dolphin | 2021.3.1

> Android Gradle Plugin Version:  7.3.0

> Gradle Version:  7.5

## 示例
[Demo](https://github.com/hdev0225/CalendarView/blob/master/demo/demo.apk)

## 安装
> 1、添加JitPack仓库到根路径下的build.gradle
```java
allprojects {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
> 2、添加依赖到模块中的build.gradle
```java
dependencies {
    implementation 'com.github.hdev0225:CalendarView:v1.0.2'
}
```

## 日历功能简介
##### [简单应用](#简单示例)
##### [单选日历](#单选)
##### [多选日历](#多选)
##### [按星期选择日历](#按星期选择)
##### [日期区间选择](#按日期区间选择)
##### [设置样式](#设置日历样式)
##### [设置不可选择日期列表](#设置不可选择日期)
##### [设置只可选择某些日期列表](#设置只可选择某些日期)

### 简单示例
> 默认情况下：开始日期为1970-1-1，结束日期为手机时间，并选中当前时间
```xml
<com.hdev.calendar.view.SingleCalendarView
    android:id="@+id/calendar_view"
    android:layout_width="match_parent"
    android:layout_height="400dp"
    android:layout_margin="15dp"
    android:background="@drawable/bg" />
```

### 单选
![image](https://github.com/hdev0225/CalendarView/blob/master/preview/single.gif)

```xml
<com.hdev.calendar.view.SingleCalendarView
    android:id="@+id/calendar_view"
    android:layout_width="match_parent"
    android:layout_height="400dp"
    android:layout_margin="15dp"
    android:background="@drawable/bg" />
```

```java
val calendarView: SingleCalendarView = findViewById(R.id.calendar_view)
// 事件监听        
calendarView.setOnSingleDateSelectedListener {
_, dateInfo ->
Toast.makeText(this@SingleActivity, dateInfo.toString(), Toast.LENGTH_LONG).show()}
// 开始日期        
var startDate = DateInfo(2023, 1, 15)
// 结束日期        
val endDate = DateInfo(2023, 4, 15)
// 选中2023-1-1
val selectedDate = DateInfo(2023, 1, 1)
// 初始化，设置日期范围
calendarView.setDateRange(
    startDate.timeInMillis(),
    endDate.timeInMillis(),
    selectedDate.timeInMillis()
)
```

### 多选
![image](https://github.com/hdev0225/CalendarView/blob/master/preview/multi.gif)
```xml
<com.hdev.calendar.view.MultiCalendarView
    android:id="@+id/calendar_view"
    android:layout_width="wrap_content"
    android:layout_height="400dp"
    android:layout_marginLeft="10dp"
    android:layout_marginRight="10dp"
    android:background="@drawable/bg" />

```
```java
val dateList = mutableListOf<DateInfo>()
dateList.add(DateInfo(2023, 5, 3))
dateList.add(DateInfo(2023, 5, 8))
dateList.add(DateInfo(2023, 5, 5))

val calendarView: MultiCalendarView = findViewById(R.id.calendar_view)
calendarView.setOnMultiDateSelectedListener {
    _, clickedDate, dateList ->
}
// 设置选中日期列表
calendarView.selectedDateList = dateList
```

### 按星期选择
![image](https://github.com/hdev0225/CalendarView/blob/master/preview/week.gif)

```xml
<com.hdev.calendar.view.WeekCalendarView
    android:id="@+id/calendar_view"
    android:layout_width="match_parent"
    android:layout_height="420dp"
    android:layout_marginLeft="10dp"
    android:layout_marginRight="10dp" />
```

```java
// 开始日期
val startDate = DateInfo(2021, 12, 30)
// 结束日期        
val endDate = DateInfo(2023, 4, 15)
// 设置某一天，该星期会选中
val selectedDate = DateInfo(2022, 1, 1)

val calendarView: WeekCalendarView = findViewById(R.id.calendar_view)
// 事件监听        
calendarView.setOnDateRangeSelectedListener {
    _, selecteDate, startDate, endDate ->
        Toast.makeText(this@WeekActivity, "start: $startDate, end:$endDate", Toast.LENGTH_LONG).show()
}
```

### 按日期区间选择
![image](https://github.com/hdev0225/CalendarView/blob/master/preview/range.gif)
```xml
<com.hdev.calendar.view.RangeCalendarView
    android:id="@+id/calendar_view"
    android:layout_width="match_parent"
    android:layout_height="400dp"
    android:layout_marginLeft="10dp"
    android:layout_marginRight="10dp"
    android:background="#ffffff"
    app:header_view="com.hdev.calendar.view.DefaultRangeHeaderView"
    app:selected_bg_color="#0078D7"
    app:selected_range_bg_color="#800078D7" />
```

```java
val calendarView: RangeCalendarView = findViewById(R.id.calendar_view)
// 设置日期范围        
calendarView.setSelectedDateRange(DateInfo(2023, 2, 21), DateInfo(2023, 5, 13))
// 事件监听
calendarView.setOnDateRangeSelectedListener {
    _, // 日历控件
    _, // 选中的日期
    startDate: DateInfo, // 开始日期
    endDate: DateInfo -> // 结束日期
    Toast.makeText(this@RangeActivity, "${startDate.format()}---${endDate.format()}", Toast.LENGTH_SHORT).show()
}
```

### 设置日历样式
![image](https://github.com/hdev0225/CalendarView/blob/master/preview/style.gif)

```xml
<com.hdev.calendar.view.SingleCalendarView
    android:id="@+id/calendar_view"
    android:layout_width="350dp"
    android:layout_height="335dp"
    android:layout_marginLeft="10dp"
    android:layout_marginRight="10dp"
    android:background="@drawable/bg"
    app:day_font_size="16sp"
    app:default_color="#000000"
    app:default_dim_color="#AAAAAA"
    app:header_view="com.calendar.app.view.CustomHeaderView"
    app:selected_bg_color="#0078D7"
    app:selected_day_color="#EFEFEF"
    app:week_title_color="#ff0000"
    app:week_title_font_size="12sp"
    app:week_title_label="M、T、W、T、F、S、S"
    app:weekend_color="#F96A31" />
```
> header_view：头部，包名+类型，需要继承BaseHeaderView类，自定义头部

> day_font_size：日期字体大小，eg: 16sp

> week_title_color: 星期标题颜色，类型：颜色码或者颜色引用, eg:#ffff00或者R.color.red;

> week_title_font_size：星期标题字体大小， eg: 12sp;

> week_title_label：星期标题，以分号分割，如：一、二、三、四、五、六、日。起始星期为星期一;

> default_color：默认文字颜色，类型：颜色码或者颜色引用, eg:#ffff00或者R.color.red;

> default_dim_color：默认文字灰色，类型：颜色码或者颜色引用, eg:#ffff00或者R.color.red;

> weekend_color：周未字体颜色，类型：颜色码或者颜色引用, eg:#ffff00或者R.color.red;

> selected_bg_color：选中背景色，类型：颜色码或者颜色引用, eg:#ffff00或者R.color.red;

> selected_day_color：选中日子颜色，类型：颜色码或者颜色引用, eg:#ffff00或者R.color.red;

> selected_range_bg_color：选中区间背景色，开始与结束之间的日期(不包括开始和结束日期), eg:#ffff00或者R.color.red;

> selected_day_dim_color：范围选择或者按星期选择的日历时，该日期不能选择，但在范围或者星期之间, eg:#ffff00或者R.color.red;


### 设置不可选择日期
```java
val dateList = mutableListOf<DateInfo>()
dateList.add(DateInfo(2023, 4, 8))
dateList.add(DateInfo(2023, 4, 11))
dateList.add(DateInfo(2023, 3, 31))
val calendarView: WeekCalendarView = findViewById(R.id.calendar_view)
// setup un-clickable date
calendarView.unClickableDateList = dateList
```
### 设置只可选择某些日期
```java
val dateList = mutableListOf<DateInfo>()
dateList.add(DateInfo(2023, 4, 8))
dateList.add(DateInfo(2023, 4, 11))
dateList.add(DateInfo(2023, 3, 31))
val calendarView: WeekCalendarView = findViewById(R.id.calendar_view)
calendarView.clickableDateList = dateList
```

## CalendarView方法说明
#### 通用方法
> unClickableDateList 设置不可点击的日期列表

> clickableDateList 设置只能点击的日期列表

> setDateRange 设置日期范围，参数：开始日期，结束日期，当前选中的日期

#### 单先日历
> setSelectedDate 设置选中某一天，如：点击按钮后，跳转到某一个日期

#### 多选日历
> selectedDateList 设置选中的日期列表/获取选中的日期列表

#### 事件监听
```java
/**
 * 单选接口回调
 */
OnSingleDateSelectedListener(
    view: SingleCalendarView, // 日历控件
    selectedDate: DateInfo // 选中的日期
)

/**
 * 按星期选，区域选择接口回调
 */
OnDateRangeSelectedListener(
    view: BaseCalendarView, // 日历控件
    selectedDate: DateInfo, // 选中的日期
    startDate: DateInfo, // 开始日期
    endDate: DateInfo // 结束日期
)

/**
 * 多选
 */
OnMultiDateSelectedListener(
    view: MultiCalendarView, // 日历控件
    clickedDate: DateInfo, // 当前当点的日期，选中或者取消选中
    dateList: List<DateInfo>, // 日期列表
) 
```

## DateInfo类说明
> 提供将dateInfo转calendar

> 将calendar转成dateInfo

> 将dateInfo转毫秒


## BaseHeaderView说明
> 自定义头部，需要继承BaseHeaderView，更新标题，显示或者隐藏上下面，然后在xml布局文件中设置header_view，如下
```xml
app:header_view="com.calendar.app.view.CustomHeaderView"
```

```java
/**
 * 获取布局id，如：R.layout.default_header_view
 */
protected abstract fun getLayoutId(): Int

/**
 * 更新标题
 */
open fun updateTitle(year: Int, month: Int) {}

/**
 * 处理上、下按钮
 * @param hasPrev 是否有上一页
 * @param hasNext 是否有下一页
 */
open fun handlePrevNext(hasPrev: Boolean, hasNext: Boolean) {}
```

## IDateRange 接口说明
> 日期范围选择RangeCalendarView，如果自定义headerView实现该接口，可更新headerView日期范围，可参考DefaultRangeHeaderView类
```java
class DefaultRangeHeaderView(
        context: Context
) : DefaultHeaderView(context), IDateRange {

private lateinit var dateRangeLabel: TextView

    override fun dateRange(startDate: DateInfo?, endDate: DateInfo?) {
        if (startDate == null && endDate == null) {
            dateRangeLabel.text = ""
        } else if (startDate != null && endDate != null) {
            dateRangeLabel.text = "${startDate.format()} -- ${endDate.format()}"
        } else if (startDate != null) {
            dateRangeLabel.text = startDate.format()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.default_range_header_view
    }

    override fun init() {
        super.init()
        dateRangeLabel = findViewById(R.id.date_range_label)
    }
}
```


