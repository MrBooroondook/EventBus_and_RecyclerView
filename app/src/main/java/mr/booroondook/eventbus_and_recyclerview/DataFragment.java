package mr.booroondook.eventbus_and_recyclerview;

import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataFragment extends Fragment {
    private String[] surnames;
    private final List<String> arrayList = Collections.synchronizedList(new ArrayList<String>());
    private boolean isLoad = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        surnames = getResources().getStringArray(R.array.surnames);
        if (!isLoad) {
            isLoad = true;
            new LoadThread().start();
        }
    }

    class LoadThread extends Thread {
        @Override
        public void run() {
            for (String surname : surnames) {
                if (!isInterrupted()) {
                    arrayList.add(surname);
                    EventBus.getDefault().post(new SurnameLoadEvent(surname));
                    SystemClock.sleep(500);
                }
            }
        }
    }

    ArrayList<String> getArrayList() {
        return(new ArrayList<>(arrayList));
    }
}
