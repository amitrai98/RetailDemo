package android.com.retaildemo.activities;

import android.com.retaildemo.R;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NestedScrollView nestedScrollView = null;

    private LinearLayout layout_linear = null;
    private List<View> list_views = new ArrayList<>();

    private int prevy = 21;
    private float incremental_variable = 0.02f;
    private float lower_threashold = 2f;
    private float higher_threashold = 4f;
    private int current_view = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * inializing view elements
     */
    private void init(){
        layout_linear = (LinearLayout) findViewById(R.id.layout_scroll);
        nestedScrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        list_views.clear();



        for (int i =0 ; i<15 ; i++){
            View view = getLayoutInflater().inflate(R.layout.design_listitem, null);
            layout_linear.addView(view);
            list_views.add(view);
        }


        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {

                int scrollX = nestedScrollView.getScrollX(); //for horizontalScrollView
                int scrollY = nestedScrollView.getScrollY(); //for verticalScrollView
                //DO SOMETHING WITH THE SCROLL COORDINATES
                View view = null;
                if (current_view >= 0)
                    view = list_views.get(current_view);



                if (view == null)
                    return;

                float current_scaley = view.getScaleY();

                if (prevy < scrollY) {
                    // upward movement
//                    float scale_value = current_scaley - incremental_variable;
//                    if (current_scaley + incremental_variable < higher_threashold) {
//                        view.setScaleY(current_scaley + incremental_variable);
//                        Log.e("setting scale ", "" + scale_value);
//                    } else {
//                        // hiew has reached max height
//                        current_view++;
//                        view.setScaleY(current_scaley + incremental_variable);
//                        Log.e("setting scale ", "" + scale_value);
////                            expand(view);
//                            return;
//
//                    }
                    if(prevy - scrollY > 20)
                        expand(view);

                } else {
                    //downward movement
//                    float scale_value = current_scaley - incremental_variable;
//                    if (scale_value > lower_threashold) {
//                        view.setScaleY(current_scaley - incremental_variable);
//                        Log.e("setting scale ", "" + scale_value);
//                    } else {
//                        if (current_view != 0)
//                            current_view--;
//                        // view has reached min height
//                    }
                }

//                Log.e(" scorll x is " + scrollX, " scorll y is "+scrollY);

                if(prevy > 20 && scrollY - prevy < 20)
                    prevy = scrollY;

                if(prevy > 20 &&  scrollY - prevy > 20 )
                    prevy = scrollY;

                Log.e("prev is "+ prevy, "current is "+scrollY);

            }
        });
    }


    public static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = 200;//v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = 50;

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}
