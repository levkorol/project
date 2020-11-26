package com.leokorol.testlove.base;

import java.util.List;

public interface IAnswersReceivedListener {
    void answersReceived(List<List<Object>> selfAnswers, List<List<Object>> partnerAnswers);
}
