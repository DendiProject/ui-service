/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.ui.service.forms.listeners;

import com.netcracker.ui.service.graf.component.Node;
import com.netcracker.ui.service.receipe.view.basic.objects.Resource;
import java.util.List;

/**
 *
 * @author Artem
 */
public interface AddStepListener {
    void onCreate(Node node, List<Resource> ingredients);
}
