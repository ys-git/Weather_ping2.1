package app.ys.weather_ping21;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splashload extends Activity {
    SharedPreferences sdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashload);
        sdata = getSharedPreferences("my", Context.MODE_PRIVATE);

        final ImageView tv1 = (ImageView) findViewById(R.id.imageView);
        final ImageView tv2 = (ImageView) findViewById(R.id.imageView2);
        final Animation a1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
        final Animation a2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);

        a1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv1.setVisibility(View.VISIBLE);
                tv1.startAnimation(a1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tv2.startAnimation(a1);

        a1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv1.startAnimation(a2);
                tv2.startAnimation(a2);
                finish();

                Intent intent = new Intent();
                intent.setClass(Splashload.this, Main.class);
                Splashload.this.startActivity(intent);
                Splashload.this.finish();
                // transition from splash to main menu
                overridePendingTransition(R.anim.activityfadein,
                        R.anim.splashfadeout);

                /*removing this code block cuz of google play developer policy voilation*/

                /*if (sdata.getString("Name", null)!=null) {

                    Intent intent = new Intent();
                    intent.setClass(Splashload.this, Main.class);
                    Splashload.this.startActivity(intent);
                    Splashload.this.finish();
                    // transition from splash to main menu
                    overridePendingTransition(R.anim.activityfadein,
                            R.anim.splashfadeout);
                }
                else{
                    Intent intent = new Intent();
                    intent.setClass(Splashload.this, Intro.class);
                    Splashload.this.startActivity(intent);
                    Splashload.this.finish();
                    // transition from splash to main menu
                    overridePendingTransition(R.anim.activityfadein,
                            R.anim.splashfadeout);}*/
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}