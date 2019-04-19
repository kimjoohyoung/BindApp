
# BindLibrary 

A collection of useful binding methods for AndroidX

* [Arguments Bindings](#arguments-bindings)
* [Preferences Bindings](#preferences-bindings)

## Dependency
Latest version : [![](https://jitpack.io/v/kimjoohyoung/BindLibrary.svg)](https://jitpack.io/#kimjoohyoung/BindLibrary)
```groovy
dependencies {
    implementation 'com.github.kimjoohyoung.BindLibrary:bindlibrary:0.955'
    implementation 'com.github.kimjoohyoung.BindLibrary:BindAnnotation:0.955'
    kapt 'com.github.kimjoohyoung.BindLibrary:BindProcessor:0.955'
}
```


## Arguments Bindings
Activity/Fragment Argument Binding with @ArgBuilder, @Arg annotation

1. annotate `Activity`/`Fragment` with `@ArgBuilder`, generate newInstance,bindArgument function
2. annotate Fields with `@Arg`
3. In the Fragment/Activity onCreate(Bundle) method you have to call bindArgument() to read the arguments and set the values.
4. Fields with @Arg type is BindArgument, you don't call bindArgument()


For Example(Activity):
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
        bindArgument()
    }
    ...
}
```
```kotlin
launchTestActivity(11,12)
launchTestActivity(11,12, requestCode)
```

For Example(Activity, BindArgument) : 
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
For Example(Fragment) : 
```kotlin
@ArgBuilder
class MainFragment : Fragment(){
    @Arg
    lateinit var stringVar11 : String

    @Arg
    var intVar11 : Int = 0

    @Arg
    var intVar12 : Int = 0
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
        ...
        bindArgument()
    }
}
```
For Example(Fragment, Inheritance) : 
```kotlin
open class MainFragment : Fragment(){
    @Arg
    lateinit var stringVar11 : String

    @Arg
    var intVar11 : Int = 0

    @Arg
    var intVar12 : Int = 0
}
```
```kotlin
@ArgBuilder
class MainFragment2 : MainFragment(){
    @Arg
    var intVar21  by BindArgument(0)

    @Arg
    var stringVar21  by BindArgument("Test")

	override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
        ...
        bindArgument()
    }
}
```
```kotlin
MainFragment2_newInstance("11", 11,12,21,"21")
```
**@ArgBuilder**

  * Fragment : 아래의 fun을 generate
    - `{Fragment}.Companion.newInstance`(...) :  Fragment에 companion이 있을 경우 
    - `{Fragment}_newInstance`(...) :  Fragment에 companion이 없을 경우 
    - bindArgument() : argument bind
  
  * Activity : 아래의 fun을 generate
    - Activity.`launchTestActivity`(...,requestCode: Int = -1)
    - Fragment.`launchTestActivity`(...,requestCode: Int = -1)
    - Context.`launchTestActivity`(...)
    - bindArgument() : argument bind
   

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
        Setting.init(this) // PreferenceManager.getDefaultSharedPreferences(this)
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
```kotlin
val value = Setting.prefInt
Setting.prefInt = value + 10
```

