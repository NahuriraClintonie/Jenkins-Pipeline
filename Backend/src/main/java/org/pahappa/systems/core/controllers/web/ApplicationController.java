package org.pahappa.systems.core.controllers.web;

import org.sers.webutils.server.web.controllers.AbstractApplicationController;
import org.springframework.stereotype.Controller;

/**
 * This is the main controller for the application
 * This controller will be responsible for handling all the requests that are not handled by the other controllers
 * <p>
 * It is required by the application to have a controller that extends the {@link AbstractApplicationController} class
 * @see AbstractApplicationController
 * @version 1.0
 */
@Controller
public class ApplicationController extends AbstractApplicationController {
    // This controller does not have any methods because it is not responsible for handling any requests
}