//package com.medhelp.medhelp.ui._chat.show;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.graphics.PointF;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Parcelable;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import android.view.GestureDetector;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//
//import com.davemorrissey.labs.subscaleview.ImageSource;
//import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.chat.InfoAboutDoc;
//import com.medhelp.medhelp.ui._chat.room.RoomActivity;
//
//
//
//
//public class ShowImage extends AppCompatActivity {
//    public final static String KEY_PATH="KEY_PATH";
//    public final static String KEY_SCROLL="KEY_SCROLL";
//
//     Toolbar toolbar;
//     SubsamplingScaleImageView image;
//     ConstraintLayout root;
//
//    protected InfoAboutDoc inf;
//
//    private GestureDetector gestureDetector=null;
//
//    private boolean latchClickImg=true;
//
//    float scaleCurrent;
//
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_image);
//
//        toolbar=findViewById(R.id.toolBar);
//        image=findViewById(R.id.imageView);
//        root=findViewById(R.id.root);
//
//        initToolBar();
//        showImg();
//
//        getInputData();
//
//        gestureDetector = new GestureDetector(this, new GestureListener());
//
//        image.setOnStateChangedListener(new SubsamplingScaleImageView.OnStateChangedListener() {
//            @Override
//            public void onScaleChanged(float newScale, int origin) {
//
//                scaleCurrent=newScale;
//
//                if(newScale==image.getMinScale())
//                {
//                    toolbar.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    toolbar.setVisibility(View.INVISIBLE);
//                }
//
//            }
//
//            @Override
//            public void onCenterChanged(PointF newCenter, int origin) {
//
//            }
//        });
//
//        image.setOnTouchListener((v, event) -> {
//            if(event.getAction()==MotionEvent.ACTION_DOWN)
//                latchClickImg=true;
//
//            if(event.getAction()==MotionEvent.ACTION_MOVE)
//                latchClickImg=false;
//
//           if(event.getAction()==MotionEvent.ACTION_UP  && latchClickImg)
//           {
//               if(toolbar!=null) {
//                   if (toolbar.getVisibility() == View.VISIBLE)
//                       toolbar.setVisibility(View.INVISIBLE);
//                   else
//                       toolbar.setVisibility(View.VISIBLE);
//               }
//           }
//
//
//            if(scaleCurrent==image.getMinScale()  || scaleCurrent==0.0f )
//                return gestureDetector.onTouchEvent(event);
//            else
//                return false;
//        });
//    }
//
//    private void getInputData() {
//        inf = getIntent().getParcelableExtra(InfoAboutDoc.class.getCanonicalName());
//
//    }
//
//
//    private void initToolBar()
//    {
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_share, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//
//            case R.id.btnShare:
//                clickShare();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void clickShare()
//    {
//        Intent toSendMessage=new Intent(Intent.ACTION_SEND);
//        toSendMessage.setType("image/*");
//        Uri uri=Uri.parse("file://"+path[currentItem]);
//        toSendMessage.putExtra(Intent.EXTRA_STREAM,uri);
//        startActivity(Intent.createChooser(toSendMessage,"MedHelper"));
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        Parcelable tmp=getIntent().getExtras().getParcelable(ShowImage.KEY_SCROLL);
//
//        Intent intent=new Intent(this, RoomActivity.class);
//        intent.putExtra(InfoAboutDoc.class.getCanonicalName(),inf);
//        intent.putExtra(KEY_SCROLL,tmp);
//        startActivity(intent);
//        finish();
//    }
//
//    String[] path;
//    int currentItem;
//    private void showImg()
//    {
//        path=getIntent().getExtras().getStringArray(KEY_PATH);
//        currentItem =Integer.valueOf(path[path.length-1]);
//
//        if(path==null  ||  path.equals(""))
//            return;
//
//        image.setImage(ImageSource.uri(path[currentItem]));
//    }
//
//
//    private void onSwipeBottom()
//    {
//        onBackPressed();
//    }
//
//    private void onSwipeTop()
//    {
//        onBackPressed();
//    }
//
//    private void onSwipeLeft()
//    {
//        if(currentItem<=path.length-3)
//        {
//            currentItem++;
//            image.setImage(ImageSource.uri(path[currentItem]));
//        }
//    }
//
//    private void onSwipeRight()
//    {
//        if(currentItem-1>=0)
//        {
//            currentItem--;
//            image.setImage(ImageSource.uri(path[currentItem]));
//        }
//    }
//
//    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
//
//        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
//        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
//
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//
//            if(e1==null ||  e2==null)
//                return false;
//
//            float distanceX = e2.getX() - e1.getX();
//            float distanceY = e2.getY() - e1.getY();
//
//            if (Math.abs(distanceY) > Math.abs(distanceX) && Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
//                if (distanceY > 0)
//                    onSwipeBottom();
//                else
//                    onSwipeTop();
//                return true;
//            }
//
//            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
//                if (distanceX > 0)
//                    onSwipeRight();
//                else
//                    onSwipeLeft();
//                return true;
//            }
//
//            return false;
//        }
//    }
//
//}
