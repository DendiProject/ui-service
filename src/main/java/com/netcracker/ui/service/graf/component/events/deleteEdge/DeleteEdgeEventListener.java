/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.ui.service.graf.component.events.deleteEdge;

/**
 * Реализация паттерна стратегия для обработки события удаления связи
 * @author Artem
 */
public interface DeleteEdgeEventListener {
    abstract public void onEventDo();
}