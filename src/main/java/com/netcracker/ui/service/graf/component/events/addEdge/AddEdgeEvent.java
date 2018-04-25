/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netcracker.ui.service.graf.component.events.addEdge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcracker.ui.service.beans.factory.BeansFactory;
import com.netcracker.ui.service.graf.component.Graf;
import com.netcracker.ui.service.graf.component.eventTypes.EventType;
import com.netcracker.ui.service.graf.component.events.BasicGrafEventHandler;
import elemental.json.JsonArray;
import org.json.JSONObject;

/**
 *
 * @author Artem
 */
public class AddEdgeEvent extends BasicGrafEventHandler{
    private AddEdgeState state;
    
    public AddEdgeEvent(Graf graf)
    {
        this.graf = graf;
    }
    
    public void setState(AddEdgeState state)
    {
        this.state = state;
    }
    
    @Override
    public void handleEvent(JsonArray arguments) {
        try
        {
            //Попытка распарсить данные, если не получается-отдать следующему
            BeansFactory<ObjectMapper> bf = BeansFactory.getInstance();
            ObjectMapper mapper = bf.getBean(ObjectMapper.class);
            state = mapper.readValue(arguments.getObject(0).toString(),AddEdgeState.class);
            if(state.stateReady)
            {
                
                graf.addEdge(state.newEdgesFrom, state.newEdgesTo);
                //Вначале нужно сделать запрос на GM для проверки возможности создания связи
                if(false){
                    //JSONObject eventStateInJSONFormat = new JSONObject();
                    //eventStateInJSONFormat = arguments.toJson();
                    graf.setEvent(EventType.addEdge, arguments.toJson());
                    //Оповещаю всех слушателей
                    graf.notifyEventListeners(graf.getAddEdgeListeners());
                }
                //Если связь нельзя создать, то она удаляется
                //Перед созданием связи нельзя проверить ее правильность из-за
                //особенностей работы js
                else{
                    //Иначе уведомление пользователя о том, что связь не может 
                    //быть создана
                    //JSONObject eventStateInJSONFormat = new JSONObject();
                    //eventStateInJSONFormat.put("deleteEdgeFrom", state.newEdgesFrom);
                    //eventStateInJSONFormat.put("deleteEdgeTo", state.newEdgesTo);
                    //graf.setEvent(EventType.deleteEdge, eventStateInJSONFormat.toString());
                }
            }
            else
            {
                giveNextHandlerWork(arguments);
            }
        }
        catch(Exception ex)
        {

        }
    }
}
