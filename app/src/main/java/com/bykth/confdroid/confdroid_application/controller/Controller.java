package com.bykth.confdroid.confdroid_application.controller;

import com.bykth.confdroid.confdroid_application.model.ModelFacade;

/**
 * All the functions of the application is controlled from here.
 */
public class Controller
{
    private ModelFacade modelFacade;

    public Controller(ModelFacade modelFacade)
    {
        this.modelFacade = modelFacade;
    }

    /**
     * Updates all the apps on the device.
     * @return boolean
     */
    public boolean uppdateApps()
    {
        return true;
    }
}
