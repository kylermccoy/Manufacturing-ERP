package com.kennuware.erp.manufacturing.application.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

public class ItemController {

    /*
     * Request items from inventory
     */
    @RequestMapping
    public String requestItem(Long itemID, int quantity) {
        return null;
    }

    /*
     * Send items to inventory
     */
    @RequestMapping
    public String deliverItem(Long itemID, int quantity) {
        return null;
    }

}
