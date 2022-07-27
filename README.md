# backstackpressedmanager
Backstackpressedmanager is a library to handle back action across fragments.

But from a while these functionality is bundled in Android Framework.
https://developer.android.com/reference/androidx/activity/OnBackPressedDispatcher
https://developer.android.com/guide/navigation/navigation-custom-back

## Installation

Library is installed by putting aar file into libs folder:

```
module/libs (ex. app/libs)
```

and adding the aar dependency to your `build.gradle` file:
```groovy
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.0-alpha05'
    implementation files("libs/backstackpressedmanager-1.0.1.aar")
}
```
## Setup
### Activity

```kotlin
class MainActivity : AppCompatActivity(), ActivityAdapterProvider {
    private val activityAdapter = ActivityAdapter()
    private lateinit var backStackChangedListener: BackStackChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // restore state of backstackpressedmanager
        activityAdapter.onCreate(this, savedInstanceState)

        // register listener to thack backstack in logcat
        backStackChangedListener = BackStackPressedManagerDebugLog(activityAdapter.backStackPressedManager)
        activityAdapter.backStackPressedManager.addOnBackStackChangedListener(backStackChangedListener)
        
        // show current backstack
        backStackChangedListener.onBackStackChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        
        // unregister logging listener
        activityAdapter.backStackPressedManager.removeOnBackStackChangedListener(backStackChangedListener)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        
        // save state of backstackpressedmanager
        activityAdapter.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if (!activityAdapter.onBackPressed())
            super.onBackPressed()
    }

    override fun getActivityAdapter(): ActivityAdapter {
        return activityAdapter
    }
}
```

### Fragment

```kotlin
class MainFragment : Fragment(), FragmentAdapterProvider {
    override lateinit var fragmentAdapter: FragmentAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentAdapter = FragmentAdapter(this)
        fragmentAdapter.onAttach()
    }

    override fun onDetach() {
        super.onDetach()
        fragmentAdapter.onDetach()
    }
}
```

## Usage
### Fragment

```kotlin
// create action
val action = object : OnBackPressedListener {

    override fun onBackPressed(): Boolean {
        val handled: Boolean
        if (binding.floatingActionMenu.isOpened) {
            binding.floatingActionMenu.close()
            handled = true
        } else {
            handled = false
        }
        return handled
    }
}

// Register action:
fragmentAdapter.putRunnable(
    KEY, // action key
    action
)

// usage
binding.floatingActionMenu.setOnMenuToggleListener { opened ->
    if (opened)
        fragmentAdapter.backStackPressedManager.addToBackStack(
            FragmentA::class.java.name, // fragment key
            KEY // action key
        )
}
```
