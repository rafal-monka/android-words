package rm.android.words;


import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.webkit.WebView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.List;

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
