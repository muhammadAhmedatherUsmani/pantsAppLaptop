package com.erlystagestudios.pantsapplication.controller;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Created by Trikster on 7/8/2015.
 */
@Component(modules = {GameController.class,TurnDbModule.class})
@Singleton
public interface DebugGameComponent extends GameComponent {

}
