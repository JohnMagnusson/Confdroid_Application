package com.bykth.confdroid.confdroid_application.model;

import java.util.ArrayList;

/**
 * Policy about the device is stored in this class.
 */
public class DeviceManagementPolicy {
    private String policy;

    /**
     * @param policy
     */
    public DeviceManagementPolicy(String policy) {
        this.policy = policy;
    }

    /**
     * @return String
     */
    public String getPolicy() {
        return policy;
    }

}
