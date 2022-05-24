//package com.medhelp.medhelp.bg.oldClass;
//
//public class SocketClient {
//
//  /*  private String mServerMessage;
//    private OnMessageReceived mMessageListener ;
//    private boolean mRun ; // флаг, определяющий, запущен ли сервер
//    private PrintWriter mBufferOut;
//    private BufferedReader mBufferIn;
//    private Socket socket;
//
//    private int centerID;
//    private int userID;
//
//    private ArrayList<String> messageExpect=new ArrayList<>();
//
//    public SocketClient( OnMessageReceived listener,int centerID,int userID) {
//        mMessageListener = listener;
//        this.centerID=centerID;
//        this.userID=userID;
//    }
//
//    public void sendMessage(final String message) {
//
//        Thread thread = new Thread(() -> {
//            try
//            {
//                if (mBufferOut != null && !mBufferOut.checkError()) {
//                    mBufferOut.println(message+"<EOF>");
//                    mBufferOut.flush();
//
//                    if(messageExpect.size()>0)
//                    {
//
//                        sendingMessagePending();
//                    }
//                }
//                else
//                {
//                    messageExpect.add(message+"<EOF>");
//                }
//            }
//            catch (Exception e)
//            {
//                Timber.tag("my").e(e);
//            }
//        });
//
//        try {
//            thread.sleep(500);
//        } catch (InterruptedException e) {
//            Timber.tag("my").e(e);
//        }
//        thread.start();
//    }
//
//    private void sendingMessagePending()
//    {
//        for(int i=0;i<messageExpect.size();i++) {
//
//            if (mBufferOut != null && !mBufferOut.checkError()) {
//                mBufferOut.println(messageExpect.get(i));
//                mBufferOut.flush();
//
//                messageExpect.remove(i);
//                i--;
//            }
//            else
//            {
//                return;
//            }
//        }
//    }
//
//    public void stopClient()
//    {
//        sendMessage(Constants.CLOSED_CONNECTION);
//
//        mRun = false;
//
//        if (mBufferOut != null)
//        {
//            mBufferOut.flush();
//            mBufferOut.close();
//        }
//
//        mBufferIn = null;
//        mBufferOut = null;
//        mServerMessage = "";
//    }
//
//    public void run() {
//        try {
//            InetAddress serverAddr = InetAddress.getByName(Constants.IP);
//
//            try {
//                socket = new Socket(serverAddr, Constants.SERVER_PORT);
//                mRun = true;
//                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//
//                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//                //сообщение серверу о старте
//                sendMessage(centerID+"."+userID);
//
//                mMessageListener.onConnected();
//
//                // ждем ответа
//                while (mRun) {
//                    if (mBufferOut.checkError()) {
//                        mRun = false;
//                       // mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
//
//                    }
//                    else {
//
//                        mServerMessage = mBufferIn.readLine();
//
//                        if (mServerMessage != null && mMessageListener != null) {
//                            mMessageListener.messageReceived(mServerMessage);
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                Timber.tag("my").e(e);
//                mMessageListener.errorRun1();
//            } finally {
//                if (socket != null && socket.isConnected()) {
//                    stopClient();
//                    socket.close();
//                }
//            }
//        } catch (Exception e) {
//            Timber.tag("my").e(e);
//        }
//    }
//
//    public boolean isConnected() {
//        return socket != null && socket.isConnected();
//    }
//
//    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
//    public boolean isRunning() {
//        return mRun;
//    }
//
//    public interface OnMessageReceived {
//        void messageReceived(String message);
//
//        void onConnected();
//
//        void errorRun1();
//    }*/
//}
