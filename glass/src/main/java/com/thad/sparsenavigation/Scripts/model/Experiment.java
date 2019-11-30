package com.thad.sparsenavigation.Scripts.model;

import java.util.List;
import java.util.Map;

public class Experiment {
    private String participantId;
    private Map<String, List<Integer>> trainingPathOrder;
    private Map<String, List<Integer>> testingPathOrder;   //.get("")

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public void setTrainingPathOrder(Map<String, List<Integer>> trainingPathOrder) {
        this.trainingPathOrder = trainingPathOrder;
    }

    public void setTestingPathOrder(Map<String, List<Integer>> testingPathOrder) {
        this.testingPathOrder = testingPathOrder;
    }

    public String getParticipantId() {
        return participantId;
    }

    public Map<String, List<Integer>> getTrainingPathOrder() {
        return trainingPathOrder;
    }

    public Map<String, List<Integer>> getTestingPathOrder() {
        return testingPathOrder;
    }

    @Override
    public String toString() {
        String str;
        str = "ParticipantId: " + participantId + "\n";
        str += "Training: " + trainingPathOrder + "\n";
        str += "Testing: " + testingPathOrder + "\n";
        str += "********\n";
        return str;

    }
}
