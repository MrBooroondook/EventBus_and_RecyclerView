package mr.booroondook.eventbus_and_recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

public class RecyclerViewFragment extends Fragment {
    private ArrayList<String> arrayList;
    private RecyclerAdapter recyclerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        recyclerAdapter = new RecyclerAdapter(arrayList, getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()),
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSurnameLoad(SurnameLoadEvent surnameLoadEvent) {
        recyclerAdapter.addSurname(surnameLoadEvent.getSurname());
    }

    void setData(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    private static class RecyclerAdapter extends RecyclerView.Adapter<Holder> {
        private final ArrayList<String> arrayList;
        private final LayoutInflater layoutInflater;

        private RecyclerAdapter(ArrayList<String> arrayList, LayoutInflater layoutInflater) {
            this.arrayList = arrayList;
            this.layoutInflater = layoutInflater;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.list_cell, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            holder.bindData(arrayList.get(position));
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private void addSurname(String surname) {
            arrayList.add(surname);
            notifyItemInserted(arrayList.size() - 1);
        }
    }

    private static class Holder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private final TextView unitName;
        private final TextView unitStatus;
        private final ImageView iconStatus;
        private String surname;

        Holder(@NonNull View itemView) {
            super(itemView);
            unitName = itemView.findViewById(R.id.unit_name);
            unitStatus = itemView.findViewById(R.id.unit_status);
            iconStatus = itemView.findViewById(R.id.icon_status);
            itemView.setOnClickListener(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        view.findViewById(R.id.linear_layout_cell).getBackground()
                                .setHotspot(motionEvent.getX(), motionEvent.getY());
                        return false;
                    }
                });
            }
        }

        void bindData(String surname) {
            this.surname = surname;
            unitName.setText(surname);

            if (surname.length() % 2 == 0) {
                unitStatus.setText(R.string.adopted);
                iconStatus.setImageResource(R.drawable.ic_check_24dp);
            } else {
                unitStatus.setText(R.string.fired);
                iconStatus.setImageResource(R.drawable.ic_close_24dp);
            }
        }

        @Override
        public void onClick(View view) {
            String status;
            if (surname.length() % 2 == 0) {
                status = view.getContext().getResources().getString(R.string.adopted);
            } else {
                status = view.getContext().getResources().getString(R.string.fired);
            }
            Toast.makeText(view.getContext(),
                    String.format("%s:\n%s", surname, status),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
