package pengumuman;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tubes_02.R;
import com.example.tubes_02.databinding.FragmentPengumumanBinding;

public class PengumumanFragment extends Fragment {
    private FragmentPengumumanBinding binding;
    private Context context;

    public PengumumanFragment(){
    }

    public static PengumumanFragment newInstance(Context context) {
        PengumumanFragment fragment = new PengumumanFragment();
        fragment.context = context;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPengumumanBinding.inflate(inflater);
        View view = binding.getRoot();
//        this.binding.btnBuatPengumuman.setOnClickListener(this::onClick);
        String [] filter = getResources().getStringArray(R.array.sort);
        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, filter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.binding.spinner.setAdapter(adapter);
        return view;
    }

    private void onClick(View view) {
        Bundle result = new Bundle();
        result.putString("page","buat_pengumuman");
        this.getParentFragmentManager().setFragmentResult("changePage", result);
    }


}
