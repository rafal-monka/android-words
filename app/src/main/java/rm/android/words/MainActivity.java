package rm.android.words;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {
    private static final String logtag = "WORDS";
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "Phrase");
        adapter.addFragment(new Tab2Fragment(), "Dict");
        adapter.addFragment(new Tab3Fragment(), "Browse");
        adapter.addFragment(new Tab4Fragment(), "*ShopList");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                tabLayout.getTabAt(i).select();

                if (i == 1) {
                    try {
                        EditText eT = adapter.getItem(0).getView().findViewById(R.id.words);
                        String str = eT.getText().toString();
                        if (!str.isEmpty() && !GlobalVariables.DikiLastWord.equals(str)) {
                            GlobalVariables.DikiLastWord = str;
                            String url = GlobalVariables.DikiUrl + str;
                            GlobalVariables.webViewDiki.loadUrl(url);
                        }
                    } catch (Exception e) {
                        Log.e(logtag, e.toString());
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }
}
