//package com.medhelp.medhelp.ui._chat.started_chats;
//
//import android.app.Activity;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.annotation.Nullable;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import androidx.appcompat.app.ActionBar;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.appcompat.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import com.medhelp.medhelp.R;
//import com.medhelp.medhelp.data.model.chat.Room;
//import com.medhelp.medhelp.ui._chat.all_rooms.AllRoomsActivity;
//import com.medhelp.medhelp.ui._chat.started_chats.recycler.StartedChatsAdapter;
//import com.medhelp.medhelp.ui._main_page.MainActivity;
//import com.medhelp.medhelp.ui._main_page.MainFragmentHelper;
//import com.medhelp.medhelp.ui.base.BaseFragment;
//import java.util.List;
//
//
//import it.sephiroth.android.library.xtooltip.ClosePolicy;
//import it.sephiroth.android.library.xtooltip.Tooltip;
//
//public class StartedChatsFragment extends BaseFragment implements StartedChatsHelper.View {
//
//    RecyclerView recy;
//    Toolbar toolbar;
//    FloatingActionButton fab;
//    ConstraintLayout rootEmpty;
//
//    private StartedChatsPresenter presenter;
//    MainFragmentHelper mainFragmentHelper;
//
//    Context context;
//    Activity activity;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        context =getContext();
//        activity =getActivity();
//        mainFragmentHelper=(MainFragmentHelper)context;
//
//        clearChatNotification();
//    }
//
//    @Override
//    protected void setUp(View view) {
//        setupToolbar();
//
//        fab.setOnClickListener(ocl);
//
//        presenter.getListRoom();
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        View rootView=inflater.inflate(R.layout.activity_chat,container,false);
//
//        recy=rootView.findViewById(R.id.recy);
//        toolbar=rootView.findViewById(R.id.toolbar);
//        fab=rootView.findViewById(R.id.fab);
//        rootEmpty=rootView.findViewById(R.id.rootEmpty);
//
//        presenter=new StartedChatsPresenter(context,this );
//        fab.post(()->{
//            testforShowTooltip(fab,getResources().getString(R.string.searchTooltipStartedRoom));
//        });
//        return rootView;
//    }
//
//
//    private void clearChatNotification()
//    {
////        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
////        notificationManager.cancel(ShowNotification.ID_CHAT_GROUP);
//    }
//
//
//    private void testforShowTooltip(View view,String msg)
//    {
//        //+
//        if (presenter.prefManager.getShowTooltipStartRoom()) {
//
//            Tooltip builder = new Tooltip.Builder(context)
//                    .anchor(view, 0, 0,false)
//                    .closePolicy(
//                            new ClosePolicy(300))
//                    .activateDelay(10)
//                    .text(msg)
//                    .maxWidth(600)
//                    .showDuration(40000)
//                    .arrow(true)
//                    .styleId(R.style.ToolTipLayoutCustomStyle)
//                    .overlay(true)
//                    .create();
//
//            builder.show(view, Tooltip.Gravity.LEFT,false);
//
//            presenter.prefManager.setShowTooltipStartRoom();
//        }
//    }
//
//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//
////        insteadS=InsteadSocket.getInstance();
////
////        insteadS.setListenerInsteadSocket(new InsteadSocket.InsteadSocketListener() {
////            @Override
////            public void saveExternalMsgRefresh() {
////                presenter.getListRoom();
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
////        },-1);
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
//
//
//    @Override
//    public void showError() {
//
//    }
//
//
//    @Override
//    public void updateRecy(List<Room> list) {
//
//        if(list.size()==0)
//        {
//            rootEmpty.setVisibility(View.VISIBLE);
//            return;
//        }
//
//        rootEmpty.setVisibility(View.GONE);
//
//        StartedChatsAdapter adapter = new StartedChatsAdapter(context, list, presenter.getUserToken());
//
//        recy.setLayoutManager(new LinearLayoutManager(context));
//        recy.setAdapter(adapter);
//    }
//
//
//    private void setupToolbar() {
//        ((MainActivity)context).setSupportActionBar(toolbar);
//        ActionBar actionBar = ((MainActivity)context).getSupportActionBar();
//
//        if (actionBar != null) {
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
//        }
//
//        toolbar.setNavigationOnClickListener(v -> mainFragmentHelper.showNavigationMenu());
//    }
//
//
//
//
//
//    public View.OnClickListener ocl=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if(v.getId()==fab.getId())
//            {
//                showAllRoomsActivity();
//            }
//        }
//    };
//
//    private void showAllRoomsActivity()
//    {
//        Intent intent=new Intent(context, AllRoomsActivity.class);
//        startActivity(intent);
//        ((MainActivity)context).finish();
//    }
//
//    @Override
//    public void userRefresh() {
//        presenter.getListRoom();
//    }
//}
