
# BindApp

[![](https://jitpack.io/v/kimjoohyoung/BindApp.svg)](https://jitpack.io/#kimjoohyoung/BindApp)

* [Arguments Bindings](#arguments-bindings)
* [Preferences Bindings](#preferences-bindings)

## Arguments Bindings
android activity와 Fragment의 argument binding

```kotlin
@ArgBuilder
class TestActivity : AppCompatActivity() {

    @Arg
    var arg1 = 0

    @Arg
    var arg2 = 10
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        bindExtra()
    }
...
}
```

```kotlin
@ArgBuilder
class TestActivity : AppCompatActivity() {

    @Arg
    var arg1 by BindArgument(0)

    @Arg
    var arg2 by BindArgument(10)
...
}
```
```kotlin
@ArgBuilder
class MainFragment : Fragment(){
    @Arg
    lateinit var stringVar11 : String

    @Arg
    var intVar11 : Int = 0

    @Arg
    var intVar12 : Int = 0
}
```

**@ArgBuilder**

  * Fragment : 아래의 fun을 generate
    - {Fragment}.Companion.newInstance(...) :  Fragment에 companion이 있을 경우 
    - {Fragment}_newInstance(...) :  Fragment에 companion이 없을 경우 
    - bindArgument() : argument bind
  
  * Activity : 아래의 fun을 generate
    - Activity.launchTestActivity(...,requestCode: Int = -1)
    - Fragment.launchTestActivity(...,requestCode: Int = -1)
    - Context.launchTestActivity(...)
    - bindExtra() : argument bind
    
**@Arg**
 
**BindArgument**

  bindArgument()나 bindExtra()를 사용하지 않아도 자동 bind

## Preferences Bindings
```kotlin
object Setting : SharedPreferenceExt() {
    var prefInt by BindPreference(10)
    var prefString by BindPreference("Sample")
}
```

```kotlin
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Setting.init(this) //PreferenceManager.getDefaultSharedPreferences(this)
    }
}
```

```kotlin
class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        Setting.init(this, "Setting") // this.getSharedPreferences("Setting", MODE_PRIVATE)
    }
}
```

