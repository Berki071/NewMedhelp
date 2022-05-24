package com.medhelp.medhelp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FCMResponse {
    @SerializedName("multicast_id") String multicastId;
    @SerializedName("success") String success;
    @SerializedName("failure") String failure;
    @SerializedName("canonical_ids") String canonicalIds;
    @SerializedName("results") List<Result> results;

    public String getMulticastId() {
        return multicastId;
    }

    public void setMulticastId(String multicastId) {
        this.multicastId = multicastId;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public String getCanonicalIds() {
        return canonicalIds;
    }

    public void setCanonicalIds(String canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    class Result{
        @SerializedName("message_id") String messageId;

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }
    }
}
