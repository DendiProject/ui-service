/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.ui.service.receipe.view.basic.objects.interfaces;

import com.netcracker.ui.service.receipe.view.basic.objects.Receipe;

/**
 *
 * @author Artem
 */
public interface DataConverter<T extends Receipe> {
    T convert(Object object);
}
