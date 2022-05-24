//package com.medhelp.medhelp.ui._chat.room;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.NotificationManager;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.provider.MediaStore;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import androidx.core.content.FileProvider;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.appcompat.widget.Toolbar;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.medhelp.medhelp.Constants;
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.chat.InfoAboutDoc;
//import com.medhelp.medhelp.data.model.chat.Message;
//import com.medhelp.medhelp.ui._chat.info.InformationActivity;
//import com.medhelp.medhelp.ui._chat.room.recycler.RoomAdapter;
//import com.medhelp.medhelp.ui._chat.show.ShowImage;
//import com.medhelp.medhelp.ui._main_page.MainActivity;
//import com.medhelp.medhelp.ui.base.BaseActivity;
//import com.medhelp.medhelp.utils.main.MainUtils;
//import com.medhelp.medhelp.utils.workToFile.show_file.ShowFile2;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//
//
//import de.hdodenhof.circleimageview.CircleImageView;
//
//import static com.medhelp.medhelp.ui._main_page.MainActivity.POINTER_TO_PAGE;
//
//
//public class RoomActivity extends BaseActivity implements RoomHelper.View {
//
//    CircleImageView ico;
//    TextView title;
//    RecyclerView recy;
//    EditText editT;
//    FloatingActionButton sendMsg;
//    ImageView takeAPhoto;
//    ImageView openLibraryPhoto;
//
//    public InfoAboutDoc inf;
//    private Parcelable scroll=null;
//
//    RoomHelper.Presenter presenter;
//
//    RoomAdapter adapter;
//
//    private static final int KEY_FOR_OPEN_PHOTO=341;
//    private static final int KEY_FOR_OPEN_CAMERA=1;
//
//    //private InsteadSocket insteadS;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_room);
//
//        initValue();
//        initToolBar();
//
//        requestPermission();
//
//        setUp();
//
//        clearChatNotification();
//    }
//    private void initValue(){
//        ico=findViewById(R.id.ico);
//        title=findViewById(R.id.title);
//        recy=findViewById(R.id.recy);
//        editT=findViewById(R.id.editT);
//        sendMsg=findViewById(R.id.sendMsg);
//        takeAPhoto=findViewById(R.id.takeAPhoto);
//        openLibraryPhoto=findViewById(R.id.openLibraryPhoto);
//    }
//
//    private void clearChatNotification()
//    {
////        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
////        notificationManager.cancel(ShowNotification.ID_CHAT_GROUP);
//    }
//
//
//    @Override
//    protected void setUp() {
//        presenter =new RoomPresenter(this);
//
//        getInputData();
//        setTitle();
//
//        sendMsg.setOnClickListener(ocl);
//        takeAPhoto.setOnClickListener(ocl);
//        openLibraryPhoto.setOnClickListener(ocl);
//
//        KeyboardVisibilityListener kvl= keyboardVisible -> {
//            if(keyboardVisible)
//            {
//                recyScrollToStart();
//            }
//        };
//        setKeyboardVisibilityListener(this, kvl);
//
//        editT.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                testShowPhotoButtons(s.toString());
//            }
//        });
//
//        ico.setOnClickListener(v -> showInfo());
//
//        title.setOnClickListener(v -> showInfo());
//    }
//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//
//        refreshRecyItems();
//
////        insteadS=InsteadSocket.getInstance();
////        insteadS.setListenerInsteadSocket(new InsteadSocket.InsteadSocketListener() {
////            @Override
////            public void saveExternalMsgRefresh() {
////                refreshRecyItems();
////            }
////
////            @Override
////            public void saveExternalMsgError(String error) {
////            }
////
////            @Override
////            public void updateOurMsgIsReadToRealmDone() {
////            }
////
////            @Override
////            public void updateOurMsgIsReadToRealmError(String error) {
////            }
////
////            @Override
////            public void sendToServerOurMsgError(String error) {
////            }
////
////            @Override
////            public void testNoReadMessageError(String error) {
////            }
////
////            @Override
////            public void NotificationError(String error) {
////            }
////        },inf.getRoom());
//    }
//
//    @Override
//    public void onPause()
//    {
//        //insteadS.setListenerInsteadSocket(null,-1);
//        super.onPause();
//    }
//
//
//    @Override
//    public void refreshRecyItems()
//    {
//        presenter.getAllMessage(inf.getRoom(), inf.getIdDoc());
//    }
//
//
//    private void requestPermission()
//    {
//        if(!checkPermissions() && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.R)
//        {
//            setListener(new ListenerBaseActivity() {
//                @Override
//                public void permissionGranted() {
//                    adapter.notifyDataSetChanged();
//                    recyScrollToStart();
//                }
//
//                @Override
//                public void permissionDenied() {
//                    showAlertPermission();
//                }
//            });
//
//            requestPermissions();
//        }
//    }
//
//    private void showAlertPermission()
//    {
//        AlertDialog.Builder builder=new AlertDialog.Builder(this);
//        builder.setMessage("Для продолжения работы необходимо разрешение для работы с файловой системой")
//                .setCancelable(false)
//                .setPositiveButton("Ok", (dialog, which) -> {})
//                .show();
//    }
//
//
//    private void testShowPhotoButtons(String s)
//    {
//        if(s.trim().length()>0)
//        {
//            takeAPhoto.setVisibility(View.GONE);
//            openLibraryPhoto.setVisibility(View.GONE);
//        }
//        else
//        {
//            takeAPhoto.setVisibility(View.VISIBLE);
//            openLibraryPhoto.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void getInputData()
//    {
//        inf=getIntent().getParcelableExtra(InfoAboutDoc.class.getCanonicalName());
//        scroll=getIntent().getExtras().getParcelable(ShowImage.KEY_SCROLL);
//    }
//
//
//    //region toolbar
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_info, menu);
//        return true;
//    }
//
//    private void setTitle()
//    {
//        String d=inf.getName();
//
//        title.setText(d);
//
//        if(inf.getImgLink()==null)
//            return;
//
//        new ShowFile2.BuilderImage(this)
//                .setType(ShowFile2.TYPE_ICO)
//                .load(inf.getImgLink())
//                .token(presenter.getUserToken())
//                .imgError(R.drawable.sh_doc)
//                .into(ico)
//                .setListener(new ShowFile2.ShowListener() {
//                    @Override
//                    public void complete(File file) {
//                    }
//
//                    @Override
//                    public void error(String error) {
//                    }
//                })
//                .build();
//    }
//
//    private void initToolBar()
//    {
//        Toolbar toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//
//            case R.id.app_bar_info:
//                showInfo();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    private void showInfo()
//    {
//        Parcelable recyclerViewState = recy.getLayoutManager().onSaveInstanceState();
//
//        Intent intent=new Intent(this, InformationActivity.class);
//        intent.putExtra(InfoAboutDoc.class.getCanonicalName(),inf);
//        intent.putExtra(ShowImage.KEY_SCROLL, recyclerViewState);
//        startActivity(intent);
//        finish();
//    }
////endregion
//
//    @Override
//    public void onBackPressed() {
//        Intent intent=new Intent(this, MainActivity.class);
//        intent.putExtra(POINTER_TO_PAGE, Constants.MENU_CHAT);
//        startActivity(intent);
//        finish();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//    public boolean latchScrollToEnd=true;
//
//    @Override
//    public void updateRecy(List<Message> listMsg) {
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        linearLayoutManager.setReverseLayout(true);
//        recy.setLayoutManager(linearLayoutManager);
//
//        adapter=new RoomAdapter(this, listMsg, new RecyListener() {
//            @Override
//            public void finishLoading() {
//                if(latchScrollToEnd ) {
//                    if( scroll==null)
//                        recyScrollToStart();
//                    else
//                        recy.getLayoutManager().onRestoreInstanceState(scroll);
//                }
//            }
//
//            @Override
//            public void clickedTheButton(String msg, long id) {
//                Parcelable recyclerViewState = recy.getLayoutManager().onSaveInstanceState();
//
//                String[] mass=getAllImgPath(id);
//
//                Intent intent=new Intent(RoomActivity.this, ShowImage.class);
//                intent.putExtra(ShowImage.KEY_PATH,mass);
//                intent.putExtra(InfoAboutDoc.class.getCanonicalName(),inf);
//                intent.putExtra(ShowImage.KEY_SCROLL, recyclerViewState);
//                startActivity(intent);
//                finish();
//            }
//        },recy, presenter.getUserToken());
//        recy.setAdapter(adapter);
//
//        recyScrollToStart();
//    }
//
//    private String[] getAllImgPath(long id)
//    {
//        List<Message>mess=adapter.getMainList();
//
//        List<String> listPath=new ArrayList<>();
//        int tmpId=0;
//
//        for(int i=0; i<mess.size();i++)
//        {
//            if(mess.get(i).getType()==Message.Img)
//            {
//                listPath.add(mess.get(i).getMsg());
//                if(mess.get(i).getId()==id)
//                {
//                    tmpId=listPath.size()-1;
//                }
//            }
//        }
//
//        listPath.add(String.valueOf(tmpId));
//
//        return listPath.toArray(new String[listPath.size()]);
//    }
//
//    private void recyScrollToStart()
//    {
//        if(recy==null || adapter==null)
//            return;
//
//        recy.scrollToPosition(0);
//    }
//
//    @Override
//    public void updateItemRecy(List<Message> response) {
//
//        adapter.updateList(response);
//        recyScrollToStart();
//    }
//
//
//    @Override
//    public void setMessageToRealm(Message msg) {
//        adapter.addRealmMessage(msg);
//        recyScrollToStart();
//    }
//
//
//    public View.OnClickListener ocl= v -> {
//
//        switch(v.getId()) {
//            case R.id.sendMsg:
//                sendMessage();
//                break;
//
//            case R.id.takeAPhoto:
//                if(checkPermissions() || android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
//                    takeAPhoto();
//                else
//                    requestPermission();
//                break;
//
//            case R.id.openLibraryPhoto:
//                if(checkPermissions() || android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
//                    openLibraryPhoto();
//                else
//                    requestPermission();
//                break;
//        }
//    };
//
//
//    private void openLibraryPhoto()
//    {
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(galleryIntent, KEY_FOR_OPEN_PHOTO);
//    }
//
//    String mCurrentPhotoPath;
//    private void takeAPhoto()
//    {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//
//            File f=presenter.generateFileCamera();
//            mCurrentPhotoPath=f.getAbsolutePath();
//            if(f==null)
//                return;
//
//            Uri photoURI = FileProvider.getUriForFile(this, "com.medhelp.medhelp.fileprovider", f);
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//            startActivityForResult(takePictureIntent, KEY_FOR_OPEN_CAMERA);
//        }
//    }
//
//    private void sendMessage()
//    {
//        hideKeyboard();
//        String msg = editT.getText().toString();
//        editT.setText("");
//        presenter.sendOurMsgSaveToRealm(inf.getRoom(), msg, Message.MSG);
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == KEY_FOR_OPEN_PHOTO  &&resultCode == RESULT_OK) {
//            if (data != null) {
//                Uri contentURI = data.getData();
//                if(contentURI!=null)
//                {
//                    File file=new File(MainUtils.getRealPathFromURI(contentURI,this));
//                    Long f=file.length();
//                    if(file.length() > 1024*1024*9) {
//                        showError("Ограничение на размер изображения 9 мегабайт");
//                        return;
//                    }
//
//                    file=presenter.generateFilePhoto(file);
//
//                    if (file.exists()) {
//                        presenter.sendOurMsgSaveToRealm(inf.getRoom(),file.getAbsolutePath(), Message.Img);
//                    }
//                }
//            }
//        }
//
//        if (requestCode == KEY_FOR_OPEN_CAMERA ) {
//
//             presenter.clearEmptyFiles();
//
//            if(resultCode == RESULT_OK) {
//                galleryAddPic();
//
//                File file=new File(mCurrentPhotoPath);
////                if(file.length() > 1024*1024*9) {
////                    showError("Ограничение на размер изображения 9 мегабайт");
////                    return;
////                }
////
////                presenter.sendOurMsgSaveToRealm(inf.getRoom(), mCurrentPhotoPath, Message.Img);
//            }
//        }
//    }
//
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(mCurrentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }
//
//    @Override
//    public void userRefresh() {
//
//    }
//
//
//    public interface RecyListener{
//        void finishLoading();
//        void clickedTheButton(String msg,long id);
//    }
//
//
//
//
//    //region show/hide keyboard
//     int mAppHeight;
//
//     int currentOrientation = -1;
//
//    public void setKeyboardVisibilityListener(final Activity activity, final  KeyboardVisibilityListener keyboardVisibilityListener) {
//
//        final View contentView = activity.findViewById(android.R.id.content);
//
//        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            private int mPreviousHeight;
//
//            @Override
//
//            public void onGlobalLayout() {
//
//                int newHeight = contentView.getHeight();
//
//                if (newHeight == mPreviousHeight)
//
//                    return;
//
//                mPreviousHeight = newHeight;
//
//                if (activity.getResources().getConfiguration().orientation != currentOrientation) {
//
//                    currentOrientation = activity.getResources().getConfiguration().orientation;
//
//                    mAppHeight =0;
//
//                }
//
//                if (newHeight >= mAppHeight) {
//
//                    mAppHeight = newHeight;
//
//                }
//
//                if (newHeight != 0) {
//
//                    if (mAppHeight > newHeight) {
//
//// Height decreased: keyboard was shown
//
//                        keyboardVisibilityListener.onKeyboardVisibilityChanged(true);
//
//                    } else
//
//                    {
//
//// Height increased: keyboard was hidden
//
//                        keyboardVisibilityListener.onKeyboardVisibilityChanged(false);
//
//                    }
//
//                }
//
//            }
//
//        });
//    }
//
//    public interface KeyboardVisibilityListener {
//        void onKeyboardVisibilityChanged(boolean keyboardVisible);
//    }
//    //endregion
//
//}
