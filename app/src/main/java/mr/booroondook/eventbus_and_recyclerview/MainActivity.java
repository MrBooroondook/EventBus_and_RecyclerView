package mr.booroondook.eventbus_and_recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final String DATA_FRAGMENT_TAG = "data_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        DataFragment dataFragment = (DataFragment) fragmentManager.findFragmentByTag(DATA_FRAGMENT_TAG);

        if (dataFragment == null) {
            dataFragment = new DataFragment();
            fragmentTransaction.add(dataFragment, DATA_FRAGMENT_TAG);
        }

        RecyclerViewFragment recyclerViewFragment = (RecyclerViewFragment) fragmentManager
                .findFragmentById(android.R.id.content);

        if (recyclerViewFragment == null) {
            recyclerViewFragment = new RecyclerViewFragment();
            fragmentTransaction.add(android.R.id.content, recyclerViewFragment);
        }

        recyclerViewFragment.setData(dataFragment.getArrayList());

        if (!fragmentTransaction.isEmpty()) {
            fragmentTransaction.commit();
        }
    }
}
