package com.leokorol.testlove.fire_base;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leokorol.testlove.TestApp;
import com.leokorol.testlove.base.IAnswersReceivedListener;
import com.leokorol.testlove.base.ISimpleListener;
import com.leokorol.testlove.model.AnswerVariant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class AuthManager {
//    private String _deviceId;
//    private String _code;
    private String _sessionCode = null;
    private static final AuthManager _instance = new AuthManager();
    private boolean _isInQueue;
    private boolean _isConnectedToPartner = false;
    private int _currentPart = 0;
    private boolean _isSubscribedToSessions;
    private ISimpleListener _partnerConnectedListener;
    private IAnswersReceivedListener _answers1ReceivedListener;
    private IAnswersReceivedListener _answers2ReceivedListener;
    private IAnswersReceivedListener _answers3ReceivedListener;

    public AuthManager() {
    }

    public boolean isInQueue() {
        return _isInQueue;
    }

    public boolean getIsConnectedToPartner() {
        return _isConnectedToPartner;
    }

    public void setPartnerConnectedListener(ISimpleListener listener) {
        _partnerConnectedListener = listener;
    }

    public void setAnswers1ReceivedListener(IAnswersReceivedListener listener) {
        _answers1ReceivedListener = listener;
    }

    public void setAnswers2ReceivedListener(IAnswersReceivedListener listener) {
        _answers2ReceivedListener = listener;
    }

    public void setAnswers3ReceivedListener(IAnswersReceivedListener listener) {
        _answers3ReceivedListener = listener;
    }

    public int getCurrentPart() {
        return _currentPart;
    }

    public void setCurrentPart(int currentPart) {
        _currentPart = currentPart;
    }

    // удалить если все ок
//    public void resetSession() {
//        _deviceId = java.util.UUID.randomUUID().toString().toUpperCase();
//        _code = generateCode();
//        _sessionCode = null;
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference deviceIdRef = database.getReference("queue").child(_deviceId);
//        deviceIdRef.setValue(_code);
//        _isInQueue = true;
//        _isConnectedToPartner = false;
//        _currentPart = 0;
//    }

    public void sendAnswers(AnswerVariant[][] answers, String answersBranch) {
        List<List<Object>> selfAnswers = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference sessionsRef = database.getReference("sessions");
        final DatabaseReference sessionRef = sessionsRef.child(_sessionCode);
        final DatabaseReference answersRef = sessionRef.child(answersBranch);
        final DatabaseReference idRef = answersRef.child(getDeviceId());
        for (int iQuestion = 0; iQuestion < answers.length; iQuestion++) {
            selfAnswers.add(getAnswerNumbers(answers[iQuestion]));
        }
        idRef.setValue(selfAnswers);
    }

    private List<Object> getAnswerNumbers(AnswerVariant[] answers) {
        List<Object> result = new ArrayList<>();
        for (int i = 0; i < answers.length; i++) {
            if (answers[i].getIsChecked()) {
                result.add(new Long(i));
            }
        }
        return result;
    }

    public void subscribeToSessions() {
        if (_isSubscribedToSessions) {
            return;
        }
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference sessionsRef = database.getReference("sessions");
        sessionsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists() || _isConnectedToPartner) {
                    return;
                }
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String sessionCode = snapshot.getKey();
                    if (sessionCode.contains(getCode())) {
                        _sessionCode = sessionCode;
                        sessionsRef.child(sessionCode).child("user1").setValue(231);
                        if (_partnerConnectedListener != null) {
                            _partnerConnectedListener.eventOccured();
                        }
                        subscribeToSession();
                        _isConnectedToPartner = true;
                        sessionsRef.child(sessionCode).child("user1").removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        _isSubscribedToSessions = true;
    }

    @SuppressWarnings("unchecked")
    private void subscribeToSession() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sessionsRef = database.getReference("sessions");
        DatabaseReference sessionRef = sessionsRef.child(_sessionCode);
        DatabaseReference answers1Ref = sessionRef.child("answers");
        DatabaseReference answers2Ref = sessionRef.child("answers2");
        DatabaseReference answers3Ref = sessionRef.child("answers3");
        answers1Ref.addValueEventListener(new AnswersListener("answers", _answers1ReceivedListener));
        answers2Ref.addValueEventListener(new AnswersListener("answers2", _answers2ReceivedListener));
        answers3Ref.addValueEventListener(new AnswersListener("answers3", _answers3ReceivedListener));
    }

    private class AnswersListener implements ValueEventListener {
        private String _answersBranch;
        private IAnswersReceivedListener _listener;

        public AnswersListener(String answersBranch, IAnswersReceivedListener listener) {
            _answersBranch = answersBranch;
            _listener = listener;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (!dataSnapshot.exists()) {
                return;
            }
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference sessionsRef = database.getReference("sessions");
            DatabaseReference sessionRef = sessionsRef.child(_sessionCode);
            DatabaseReference answersRef = sessionRef.child(_answersBranch);
            DatabaseReference isFinishedRef = sessionRef.child("isFinished");
            Object obj = dataSnapshot.getValue();
            if (obj instanceof HashMap) {
                HashMap<String, Object> hm = (HashMap<String, Object>)obj;
                if (hm.size() == 2) {
                    isFinishedRef.setValue(true);
                    answersRef.removeValue();
                    isFinishedRef.removeValue();
                    List<List<Object>> selfAnswers = new ArrayList<>();
                    List<List<Object>> partnerAnswers = new ArrayList<>();
                    for (Object key : hm.keySet()) {
                        if (key.toString().equals(getDeviceId())) {
                            selfAnswers = (List<List<Object>>)hm.get(key);
                        } else {
                            partnerAnswers = (List<List<Object>>)hm.get(key);
                        }
                    }
                    if (_listener != null) {
                        _listener.answersReceived(selfAnswers, partnerAnswers);
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

    public void tryMoveToSession(final String partnerCode, final ISimpleListener successListener, final ISimpleListener failListener) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference queueRef = database.getReference("queue");
        final DatabaseReference sessionsRef = database.getReference("sessions");
        _sessionCode = null;
        _isConnectedToPartner = false;
        _isInQueue = true;
        queueRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    return;
                }
                boolean found = false;
                String foundId = null;
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    String code = (String)snapshot.getValue();
                    if (partnerCode.equalsIgnoreCase(code)) {
                        found = true;
                        foundId = id;
                        break;
                    }
                }
                if (found) {
                    _sessionCode = getCode() + "_" + partnerCode;
                    queueRef.child(getDeviceId()).removeValue();
                    queueRef.child(foundId).removeValue();
                    sessionsRef.child(_sessionCode).child("user2").setValue(123);
                    if (successListener != null) {
                        successListener.eventOccured();
                    }
                    sessionsRef.child(_sessionCode).child("user2").removeValue();
                } else {
                    if (failListener != null) {
                        failListener.eventOccured();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public String getDeviceId() {
        return TestApp.getSharedPref().getString(TestApp.getDEVICE_ID(), "");
    }

    public String getCode() {
        return TestApp.getSharedPref().getString(TestApp.getCODE(), "");
    }

    public static AuthManager getInstance() {
        return _instance;
    }
}